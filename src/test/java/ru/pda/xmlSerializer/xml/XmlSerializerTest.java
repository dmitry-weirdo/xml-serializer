/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.xml;

import org.junit.Before;
import org.junit.Test;
import ru.pda.xmlSerializer.DepartmentJob;
import ru.pda.xmlSerializer.DepartmentJobs;
import ru.pda.xmlSerializer.ObjectUtils;

import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class XmlSerializerTest
{
	@Before
	public void configureLogger() {
//		BasicConfigurator.configure();
		// log4j seems to be set to console output by default
	}

	@Test(expected = IncorrectXmlFileException.class)
	public void testNonExistingFile() throws IncorrectXmlFileException {
		final XmlSerializer serializer = new XmlSerializer();
		serializer.deserializeFromXmlUsingDomParser("nonExistingFile.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testEmptyXmlFile() throws IncorrectXmlFileException {
		serializeResource("/xml/empty.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testIncorrectRootElement() throws IncorrectXmlFileException {
		serializeResource("/xml/wrongRootTag.xml");
	}

	@Test(expected = IncorrectXmlFileException.class)
	public void testInnerDepartmentJobElements() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentJobInnerElements.xml");
	}

	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeMultipleElements() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeMultipleElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeNoElements() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeNoElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeInnerElements() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeInnerElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeMultipleChildElements() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeMultipleChildElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeNotTextChildElement() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeNotTextChildElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeEmptyElement() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeEmptyElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeEmptyIfTrimmedElement() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeEmptyIfTrimmedElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDepartmentCodeTooLong() throws IncorrectXmlFileException {
		serializeResource("/xml/departmentCodeTooLong.xml");
	}

	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameMultipleElements() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameMultipleElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameNoElements() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameNoElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameInnerElements() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameInnerElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameMultipleChildElements() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameMultipleChildElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameNotTextChildElement() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameNotTextChildElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameEmptyElement() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameEmptyElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameEmptyIfTrimmedElement() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameEmptyIfTrimmedElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testJobNameTooLong() throws IncorrectXmlFileException {
		serializeResource("/xml/jobNameTooLong.xml");
	}

	@Test(expected = IncorrectXmlFileException.class)
	public void testDescriptionMultipleElements() throws IncorrectXmlFileException {
		serializeResource("/xml/descriptionMultipleElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDescriptionInnerElements() throws IncorrectXmlFileException {
		serializeResource("/xml/descriptionInnerElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDescriptionMultipleChildElements() throws IncorrectXmlFileException {
		serializeResource("/xml/descriptionMultipleChildElements.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDescriptionNotTextChildElement() throws IncorrectXmlFileException {
		serializeResource("/xml/descriptionNotTextChildElement.xml");
	}
	@Test(expected = IncorrectXmlFileException.class)
	public void testDescriptionTooLong() throws IncorrectXmlFileException {
		serializeResource("/xml/descriptionTooLong.xml");
	}

	@Test
	public void testNoJobsXml() throws IncorrectXmlFileException {
		final DepartmentJobs departmentJobs = serializeResource("/xml/noJobs.xml");
		assertNotNull(departmentJobs);

		assertTrue( ObjectUtils.empty(departmentJobs.getJobs()) );
	}

	@Test
	public void testCorrectXml() throws IncorrectXmlFileException {
		final DepartmentJobs departmentJobs = serializeResource("/xml/correct.xml");
		assertNotNull(departmentJobs);

		final List<DepartmentJob> jobs = departmentJobs.getJobs();
		assertNotNull(jobs);
		assertEquals(4, jobs.size());

		final DepartmentJob job0 = jobs.get(0);
		assertNull( job0.getId() );
		assertEquals( "12345678901234567890", job0.getDepartmentCode() );
		assertEquals( "1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890", job0.getJobName() );
		assertNull( job0.getDescription() );

		final DepartmentJob job1 = jobs.get(1);
		assertNull( job1.getId() );
		assertEquals( "Department 1", job1.getDepartmentCode() );
		assertEquals( "Job 2", job1.getJobName() );
		assertNull(job1.getDescription());

		final DepartmentJob job2 = jobs.get(2);
		assertNull( job2.getId() );
		assertEquals( "Department 1", job2.getDepartmentCode() );
		assertEquals( "Job 3", job2.getJobName() );
		assertEquals( "123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345", job2.getDescription() );

		final DepartmentJob job3 = jobs.get(3);
		assertNull( job3.getId() );
		assertEquals("Department 1", job3.getDepartmentCode());
		assertEquals("Job 4", job3.getJobName());
		assertEquals("description to trim", job3.getDescription());
	}


	private DepartmentJobs serializeResource(final String resourceName) throws IncorrectXmlFileException {
		final URL resource = getClass().getResource(resourceName);
		final String fileName = resource.getPath();

		final XmlSerializer serializer = new XmlSerializer();
		return serializer.deserializeFromXmlUsingDomParser(fileName);
	}
}