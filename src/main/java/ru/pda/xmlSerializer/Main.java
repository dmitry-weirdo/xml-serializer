/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import org.apache.log4j.Logger;
import ru.pda.xmlSerializer.commandExecutor.ExportDataCommandExecutor;
import ru.pda.xmlSerializer.commandExecutor.InsertTestDataCommandExecutor;
import ru.pda.xmlSerializer.commandLine.*;
import ru.pda.xmlSerializer.jdbc.JdbcConnector;
import ru.pda.xmlSerializer.jdbc.JdbcDriverRegisterFailException;
import ru.pda.xmlSerializer.loggerConfig.LoggerConfigurator;
import ru.pda.xmlSerializer.loggerConfig.LoggerConfiguringException;
import ru.pda.xmlSerializer.propertiesConfig.IncorrectPropertiesFileException;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import ru.pda.xmlSerializer.xml.IncorrectXmlFileException;
import ru.pda.xmlSerializer.xml.SerializeToXmlFileException;
import ru.pda.xmlSerializer.xml.XmlSerializer;
import su.opencode.kefir.util.ObjectUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

public class Main
{
	public static void main(String[] args) throws IncorrectXmlFileException, NonUniqueNaturalKeyException, LoggerConfiguringException, IncorrectPropertiesFileException, JdbcDriverRegisterFailException, SQLException, SerializeToXmlFileException {
		PropertiesConfig config = parsePropertiesConfig(); // файл свойств нужен для конфигурации логгера
		configureLogger(config); // логгер может быть нужен для валидации аргументов командной строки
		CommandArguments arguments = parseArguments(args);
		executeCommand(arguments, config);

		if (true)
			return;

		logger.info("From null enum");


//		BasicConfigurator.configure();

		System.out.println("arguments: " + Arrays.toString(args));
		// todo: validate arguments

		// read data from properties file
//		String propertiesFileName = "xmlSerializer.properties"; // todo: possibly get it from program arguments
		String propertiesFileName = "C:\\java\\xmlSerializer-googleCode\\src\\main\\resources\\properties\\xmlSerializer.properties"; // todo: possibly get it from program arguments
//		PropertiesConfig config = ObjectFiller.createObject(propertiesFileName, PropertiesConfig.class);
		System.out.println("PropertiesConfig:\n" + ObjectUtils.instanceToString(config));

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

	private static PropertiesConfig parsePropertiesConfig() throws IncorrectPropertiesFileException {
		return PropertiesConfig.parseFromPropertiesFile(PROPERTIES_CONFIG_FILE_NAME);
	}
	private static void configureLogger(PropertiesConfig config) throws LoggerConfiguringException {
		LoggerConfigurator.configureLogger(config);
	}
	private static CommandArguments parseArguments(String[] arguments) {
		return BasicCommandArguments.parseArgumentsDependingOnCommand(arguments);
	}

	private static void executeCommand(CommandArguments arguments, PropertiesConfig config) throws JdbcDriverRegisterFailException, SQLException, SerializeToXmlFileException {
		switch (arguments.getCommand())
		{
			case INSERT_TEST_DATA:
				InsertTestDataCommandArguments insertTestDataArguments = (InsertTestDataCommandArguments) arguments;
				new InsertTestDataCommandExecutor().execute(insertTestDataArguments, config);
				break;

			case EXPORT_DATA_TO_XML:
				ExportDataCommandArguments exportDataCommandArguments = (ExportDataCommandArguments) arguments;
				new ExportDataCommandExecutor().execute(exportDataCommandArguments, config);
				break;

			case IMPORT_DATA_FROM_XML:
				ImportDataCommandArguments importDataCommandArguments = (ImportDataCommandArguments) arguments;
				break;

			default:
				throw new IllegalArgumentException(concat("Unknown command: \"", arguments.getCommand(), "\""));
		}
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

	private StringBuffer sb = new StringBuffer();
	private static final Logger logger = Logger.getLogger(Main.class);

	public static final String PROPERTIES_CONFIG_FILE_NAME = "xmlSerializer.properties";
}