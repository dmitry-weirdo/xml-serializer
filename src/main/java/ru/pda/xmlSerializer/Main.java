/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import org.apache.log4j.BasicConfigurator;
import su.opencode.kefir.gen.helper.ObjectFiller;
import su.opencode.kefir.util.ObjectUtils;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class Main
{
	public static void main(String[] args) throws IncorrectXmlFileException, NonUniqueNaturalKeyException {
		BasicConfigurator.configure();

		System.out.println("arguments: " + Arrays.toString(args));
		// todo: validate arguments

		// read data from properties file
//		String propertiesFileName = "xmlSerializer.properties"; // todo: possibly get it from program arguments
		String propertiesFileName = "C:\\java\\xmlSerializer-googleCode\\src\\main\\resources\\properties\\xmlSerializer.properties"; // todo: possibly get it from program arguments
		PropertiesConfig config = ObjectFiller.createObject(propertiesFileName, PropertiesConfig.class);
		System.out.println( "PropertiesConfig:\n" +  ObjectUtils.instanceToString(config) );

		// todo: validate fields

		JdbcConnector connector = new JdbcConnector();
		Connection connection = connector.createConnection(config);

//		List<DepartmentJob> jobsToInsert = getJobs();
		List<DepartmentJob> jobsToInsert = DepartmentJobGenerator.generateJobs(1000, 100, true);

/*
		System.out.println("time before insert:" + new Date() );
		connector.insertJobs(connection, jobsToInsert);
		System.out.println("time after insert:" + new Date() );
*/

		String xmlFileName = "c:\\java\\xmlSerializer\\jobs.xml";
/*
		DepartmentJobs jobsToSerialize = new DepartmentJobs();
		jobsToSerialize.setJobs(jobsToInsert);
		System.out.println( "serialized jobs size: " + jobsToSerialize.getJobs().size() );
*/

		XmlSerializer serializer = new XmlSerializer();
/*
		serializer.serializeToXml(jobsToSerialize, xmlFileName);

		DepartmentJobs jobsFromXml = serializer.deserializeFromXml(xmlFileName);
		System.out.println("deserialized jobs size (JAXB parser): " + jobsFromXml.getJobs().size());
*/

//		String xmlFileName1 = "c:\\java\\xmlSerializer\\jobs1.xml";
//		String xmlFileName1 = "C:\\java\\xmlSerializer\\src\\test\\resources\\xml\\departmentJobInnerElements.xml";
		DepartmentJobs jobsFromXmlDom = serializer.deserializeFromXmlUsingDomParser(xmlFileName);
		System.out.println("deserialized jobs size (Xml DOM parser): " + jobsFromXmlDom.getJobs().size());

		DepartmentJobMapUtils.createJobsMap(jobsFromXmlDom);
		System.out.println("HashMap created successfully");
	}

	private static List<DepartmentJob> getJobs() {
		List<DepartmentJob> jobs = new ArrayList<>();

		final String departmentCode = "New department";

		jobs.add( new DepartmentJob(departmentCode, "Работа раз") );
		jobs.add( new DepartmentJob(departmentCode, "Работа два") );
		jobs.add( new DepartmentJob(departmentCode, "Работа три") );
		jobs.add( new DepartmentJob(departmentCode, "Работа четыре", "А вот эта работа будет с комментом") ); // with no comments

		return jobs;
	}
}