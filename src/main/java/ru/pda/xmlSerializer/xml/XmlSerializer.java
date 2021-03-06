/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.xml;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.pda.xmlSerializer.DepartmentJob;
import ru.pda.xmlSerializer.DepartmentJobs;
import ru.pda.xmlSerializer.FileUtils;
import ru.pda.xmlSerializer.ObjectUtils;
import su.opencode.kefir.util.JaxbHelper;
import su.opencode.kefir.util.StringUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, выполняющий сериализацию и&nbsp;десериализацию должностей в&nbsp;отделе
 * в&nbsp;xml-файл и&nbsp;из&nbsp;xml-файла.
 */
public class XmlSerializer
{
	public static void serializeToXml(final DepartmentJobs jobs, final String fileName) throws SerializeToXmlFileException {
		try
		{
			FileUtils.deleteFile(fileName);
			JaxbHelper.jaxbObjectToFile(jobs, fileName);
		}
		catch (final Exception e)
		{
			throw new SerializeToXmlFileException(e);
		}
	}
	public static void serializeToXml(final List<DepartmentJob> jobs, final String fileName) throws SerializeToXmlFileException {
		final DepartmentJobs departmentJobs = new DepartmentJobs();
		departmentJobs.setJobs(jobs);
		serializeToXml(departmentJobs, fileName);
	}

	public static DepartmentJobs deserializeFromXml(final String fileName) throws IncorrectXmlFileException {
		final DepartmentJobs departmentJobs = JaxbHelper.jaxbObjectFromFile(DepartmentJobs.class, fileName);

		// prevent NPE failing in case of no departmentJob element inside root element
		if (departmentJobs.getJobs() == null)
			departmentJobs.setJobs( new ArrayList<>() );

		validateJobs(departmentJobs);

		return departmentJobs; // much better than DOM parsing, but the task is to do DOM parsing
	}

	public DepartmentJobs deserializeFromXmlUsingDomParser(final String fileName) throws IncorrectXmlFileException {
		final File file = new File(fileName);

		try
		{
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			final DocumentBuilder builder = factory.newDocumentBuilder();
			final Document document = builder.parse(file);

			final Element documentElement = document.getDocumentElement();
			documentElement.normalize();

			final String rootNodeName = documentElement.getNodeName();
			logger.info( concat(sb, "documentElement name: ", rootNodeName) );
			if ( !rootNodeName.equals(DepartmentJobs.DEPARTMENT_JOBS_XML_ELEMENT_NAME) )
				throw new IncorrectXmlFileException( concat(sb, "Incorrect node name for rootElement: \"", rootNodeName, "\" (expected \"", DepartmentJobs.DEPARTMENT_JOBS_XML_ELEMENT_NAME, "\")") );

			final NodeList jobsNodes = document.getElementsByTagName(DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME);
			logger.info( concat(sb, "Total \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" elements: ", jobsNodes.getLength()) );

			final List<DepartmentJob> jobs = new ArrayList<>();
			DepartmentJob job;
			String departmentCode;
			String jobName;
			String description;

			for (int i = 0; i < jobsNodes.getLength(); i++)
			{
				final Node departmentJobNode = jobsNodes.item(i);

				if ( (departmentJobNode.getNodeType() == Node.ELEMENT_NODE) && (departmentJobNode instanceof Element) && (departmentJobNode.getParentNode().equals(documentElement)) )
				{
					final Element departmentJobElement = (Element) departmentJobNode;

					// fill department code
					final NodeList departmentCodeNodes = departmentJobElement.getElementsByTagName(DepartmentJob.DEPARTMENT_CODE_XML_ELEMENT_NAME);
					if ( departmentCodeNodes.getLength() != 1 ) // no check for null, because it returns length = 0 when no such elements are found
					{
						throw new IncorrectXmlFileException( concat(sb, "Incorrect quantity of \"", DepartmentJob.DEPARTMENT_CODE_XML_ELEMENT_NAME, "\" elements in \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" element: ", departmentCodeNodes.getLength(), " (expected 1)") );
					}

					final Node departmentCodeNode = departmentCodeNodes.item(0);
					if ( (departmentCodeNode.getNodeType() == Node.ELEMENT_NODE) && (departmentCodeNode instanceof Element) && (departmentCodeNode.getParentNode().equals(departmentJobNode)) )
					{
						final NodeList departmentCodeNodeChildNodes = departmentCodeNode.getChildNodes();
						if (departmentCodeNodeChildNodes.getLength() != 1)
						{
							throw new IncorrectXmlFileException( concat(sb, "Incorrect quantity of child elements in \"", DepartmentJob.DEPARTMENT_CODE_XML_ELEMENT_NAME, "\" element: ", departmentCodeNodeChildNodes.getLength(), " (expected 1)") );
						}

						final Node departmentCodeChildNode = departmentCodeNodeChildNodes.item(0);
						if ( (departmentCodeChildNode.getNodeType() == Node.TEXT_NODE) )
						{
							departmentCode = departmentCodeChildNode.getTextContent();
							if ( StringUtils.emptyIfTrimmed(departmentCode) )
								throw new IncorrectXmlFileException( concat(sb, DepartmentJob.DEPARTMENT_CODE_XML_ELEMENT_NAME, " element cannot be empty.") );

							departmentCode = departmentCode.trim(); // todo: think about whether trimming the string is correct
						}
						else
						{
							throw new IncorrectXmlFileException( concat(sb, "Child element of \"", DepartmentJob.DEPARTMENT_CODE_XML_ELEMENT_NAME, "\" element is not a text node") );
						}
					}
					else
					{ // departmentCode not directly inside departmentJob
						throw new IncorrectXmlFileException( concat(sb, "Element with name = \"", DepartmentJob.DEPARTMENT_CODE_XML_ELEMENT_NAME, "\" found, but it is not an element or is not a direct child of \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" element.") );
					}

					// fill job name
					final NodeList jobNameNodes = departmentJobElement.getElementsByTagName(DepartmentJob.JOB_NAME_XML_ELEMENT_NAME);
					if ( jobNameNodes.getLength() != 1 ) // no check for null, because it returns length = 0 when no such elements are found
					{
						throw new IncorrectXmlFileException( concat(sb, "Incorrect quantity of \"", DepartmentJob.JOB_NAME_XML_ELEMENT_NAME, "\" elements in \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" element: ", jobNameNodes.getLength(), " (expected 1)") );
					}

					final Node jobNameNode = jobNameNodes.item(0);
					if ( (jobNameNode.getNodeType() == Node.ELEMENT_NODE) && (jobNameNode instanceof Element) && (jobNameNode.getParentNode().equals(departmentJobNode)) )
					{
						final NodeList jobNameNodeChildNodes = jobNameNode.getChildNodes();
						if (jobNameNodeChildNodes.getLength() != 1)
						{
							throw new IncorrectXmlFileException( concat(sb, "Incorrect quantity of child elements in \"", DepartmentJob.JOB_NAME_XML_ELEMENT_NAME, "\" element: ", jobNameNodeChildNodes.getLength(), " (expected 1)") );
						}

						final Node jobNameChildNode = jobNameNodeChildNodes.item(0);
						if ( (jobNameChildNode.getNodeType() == Node.TEXT_NODE) )
						{
							jobName = jobNameChildNode.getTextContent();
							if ( StringUtils.emptyIfTrimmed(departmentCode) )
								throw new IncorrectXmlFileException( concat(sb, DepartmentJob.JOB_NAME_XML_ELEMENT_NAME, " element cannot be empty.") );

							jobName = jobName.trim(); // todo: think about whether trimming the string is correct
						}
						else
						{
							throw new IncorrectXmlFileException( concat(sb, "Child element of \"", DepartmentJob.JOB_NAME_XML_ELEMENT_NAME, "\" element is not a text node") );
						}
					}
					else
					{ // jobName not directly inside departmentJob
						throw new IncorrectXmlFileException( concat(sb, "Element with name = \"", DepartmentJob.JOB_NAME_XML_ELEMENT_NAME, "\" found, but it is not an element or is not a direct child of \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" element.") );
					}

					// fill description (if it is present)
					final NodeList descriptionNodes = departmentJobElement.getElementsByTagName(DepartmentJob.DESCRIPTION_XML_ELEMENT_NAME);
					if ( descriptionNodes.getLength() == 0 )
					{ // no description - ok
						description = null;
					}
					else if ( descriptionNodes.getLength() > 1 )
					{
						throw new IncorrectXmlFileException( concat(sb, "Incorrect quantity of \"", DepartmentJob.DESCRIPTION_XML_ELEMENT_NAME, "\" elements in \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" element: ", descriptionNodes.getLength(), " (expected 0 or 1)") );
					}
					else
					{ // non-empty description
						final Node descriptionNode = descriptionNodes.item(0);
						if ( (descriptionNode.getNodeType() == Node.ELEMENT_NODE) && (descriptionNode instanceof Element) && (descriptionNode.getParentNode().equals(departmentJobNode)) )
						{
							final NodeList descriptionChildNodes = descriptionNode.getChildNodes();
							if (descriptionChildNodes.getLength() == 0)
							{
								description = null;
							}
							else if (descriptionChildNodes.getLength() > 1)
							{
								throw new IncorrectXmlFileException( concat(sb, "Incorrect quantity of child elements in \"", DepartmentJob.DESCRIPTION_XML_ELEMENT_NAME, "\" element: ", descriptionChildNodes.getLength(), " (expected 0 or 1)") );
							}
							else
							{
								final Node descriptionChildNode = descriptionChildNodes.item(0);
								if ( (descriptionChildNode.getNodeType() == Node.TEXT_NODE) )
								{
									description = descriptionChildNode.getTextContent();

									if (StringUtils.emptyIfTrimmed(description))
										description = null; // description can be empty
//									throw new IncorrectXmlFileException( concat(sb, DepartmentJob.DESCRIPTION_XML_ELEMENT_NAME, " element cannot be empty.") );

									// todo: think about trimming the string
									if (description != null)
										description = description.trim();
								}
								else
								{
									throw new IncorrectXmlFileException( concat(sb, "Child element of \"", DepartmentJob.DESCRIPTION_XML_ELEMENT_NAME, "\" element is not a text node") );
								}
							}
						}
						else
						{ // description not directly inside departmentJob
							throw new IncorrectXmlFileException( concat(sb, "Element with name = \"", DepartmentJob.DESCRIPTION_XML_ELEMENT_NAME, "\" found, but it is not an element or is not a direct child of \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" element.") );
						}
					}

					job = new DepartmentJob();
					job.setDepartmentCode(departmentCode);
					job.setJobName(jobName);
					job.setDescription(description);

					jobs.add(job);
				}
				else
				{ // departmentJob not directly inside root departmentJobsElement
					throw new IncorrectXmlFileException( concat(sb, "Element with name = \"", DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME, "\" found, but it is not an element or is not a child of root \"", DepartmentJobs.DEPARTMENT_JOBS_XML_ELEMENT_NAME, "\" element.") );
				}
			}

			validateJobs(jobs);

			final DepartmentJobs result = new DepartmentJobs();
			result.setJobs(jobs);
			return result;
		}

		catch (ParserConfigurationException | SAXException | IOException e)
		{
			throw new IncorrectXmlFileException(e);
		}
	}

	/**
	 * Выполняет проверку корректности заполнения должностей. Проверка на&nbsp;уникальность натуральных ключей не&nbsp;производится.
	 * @param jobs список {@linkplain DepartmentJob должностей}, десериалиованных из&nbsp;xml-файла.
	 * @throws IncorrectXmlFileException если хотя бы в&nbsp;одной из&nbsp;должностей найдена ошибка.
	 */
	private static void validateJobs(final Collection<DepartmentJob> jobs) throws IncorrectXmlFileException {
		if ( ObjectUtils.empty(jobs) )
			return;

		for (final DepartmentJob job : jobs)
		{
			// departmentCode
			if ( StringUtils.emptyIfTrimmed( job.getDepartmentCode() ) )
				throw new IncorrectXmlFileException( concat("DepartmentJob has empty departmentCode. DepartmentJob: ", job) );

			if ( ( job.getDepartmentCode().length() > DepartmentJob.DEPARTMENT_CODE_MAX_LENGTH ))
				throw new IncorrectXmlFileException( concat("DepartmentJob has departmentCode \"", job.getDepartmentCode(), "\" which is longer than allowable max length (", DepartmentJob.DEPARTMENT_CODE_MAX_LENGTH, " characters). DepartmentJob: ", job) );

			// jobName
			if ( StringUtils.emptyIfTrimmed( job.getJobName() ) )
				throw new IncorrectXmlFileException( concat("DepartmentJob has empty jobName. DepartmentJob: ", job) );

			if ( job.getJobName().length() > DepartmentJob.JOB_NAME_MAX_LENGTH )
				throw new IncorrectXmlFileException( concat("DepartmentJob has jobName \"", job.getJobName(), "\" which is longer than allowable max length (", DepartmentJob.JOB_NAME_MAX_LENGTH, " characters). DepartmentJob: ", job) );

			// description
			if ( StringUtils.notEmpty(job.getDescription()) && (job.getDescription().length() > DepartmentJob.DESCRIPTION_MAX_LENGTH) )
				throw new IncorrectXmlFileException( concat("DepartmentJob has description \"", job.getJobName(), "\" which is longer than allowable max length (", DepartmentJob.DESCRIPTION_MAX_LENGTH, " characters). DepartmentJob: ", job) );
		}
	}

	/**
	 * @param departmentJobs десерализованный из xml-файла список {@linkplain DepartmentJob должностей}.
	 * @throws IncorrectXmlFileException если хотя бы в&nbsp;одной из&nbsp;должностей найдена ошибка.
	 *
	 * @see #validateJobs(java.util.Collection)
	 */
	private static void validateJobs(final DepartmentJobs departmentJobs) throws IncorrectXmlFileException {
		validateJobs( departmentJobs.getJobs() );
	}

	private final StringBuilder sb = new StringBuilder();
	private static final Logger logger = LogManager.getLogger(XmlSerializer.class);
}