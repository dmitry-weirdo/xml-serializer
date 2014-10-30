/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandLine;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.Test;
import su.opencode.kefir.util.StringUtils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static su.opencode.kefir.util.StringUtils.concat;

public class CommandArgumentsTest
{
	@Before
	public void configureLogger() {
		BasicConfigurator.configure();
	}

	@Test(expected = IllegalArgumentException.class)
	public void testNoArguments() {
		parseArguments();
	}
	@Test(expected = IllegalArgumentException.class)
	public void testUnknownCommand() {
		parseArguments("Unknown_command");
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataIncorrectArgumentsCount() {
		parseArguments( Command.INSERT_TEST_DATA.toString(), "arg1" );
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataNotValidDepartmentsCount() {
		parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, "bad_departments_count"
			, Integer.toString(InsertTestDataCommandArguments.MIN_JOBS_COUNT)
		);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataDepartmentsCountLessThanMin() {
		parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MIN_DEPARTMENTS_COUNT - 1)
			, Integer.toString(InsertTestDataCommandArguments.MIN_JOBS_COUNT)
		);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataDepartmentsCountMoreThanMax() {
		parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MAX_DEPARTMENTS_COUNT + 1)
			, Integer.toString(InsertTestDataCommandArguments.MIN_JOBS_COUNT)
		);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataNotValidJobsCount() {
		parseArguments(
			Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MIN_DEPARTMENTS_COUNT)
			, "bad_jobs_count"
		);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataJobsCountLessThanMin() {
		parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MIN_DEPARTMENTS_COUNT)
			, Integer.toString(InsertTestDataCommandArguments.MIN_JOBS_COUNT - 1)
		);
	}
	@Test(expected = IllegalArgumentException.class)
	public void testInsertTestDataJobsCountMoreThanMax() {
		parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MIN_DEPARTMENTS_COUNT)
			, Integer.toString(InsertTestDataCommandArguments.MAX_JOBS_COUNT + 1)
		);
	}
	@Test
	public void testInsertTestDataCorrectArguments() {
		CommandArguments parsedArguments;
		InsertTestDataCommandArguments insertTestDataCommandArguments;

		parsedArguments = parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MIN_DEPARTMENTS_COUNT)
			, Integer.toString(InsertTestDataCommandArguments.MIN_JOBS_COUNT)
		);

		assertTrue(parsedArguments instanceof InsertTestDataCommandArguments);
		insertTestDataCommandArguments = (InsertTestDataCommandArguments) parsedArguments;

		assertEquals(InsertTestDataCommandArguments.MIN_DEPARTMENTS_COUNT, insertTestDataCommandArguments.getDepartmentsCount());
		assertEquals(InsertTestDataCommandArguments.MIN_JOBS_COUNT, insertTestDataCommandArguments.getJobsCount());


		parsedArguments = parseArguments(
			  Command.INSERT_TEST_DATA.toString()
			, Integer.toString(InsertTestDataCommandArguments.MAX_DEPARTMENTS_COUNT)
			, Integer.toString(InsertTestDataCommandArguments.MAX_JOBS_COUNT)
		);

		assertTrue(parsedArguments instanceof InsertTestDataCommandArguments);
		insertTestDataCommandArguments = (InsertTestDataCommandArguments) parsedArguments;

		assertEquals(InsertTestDataCommandArguments.MAX_DEPARTMENTS_COUNT, insertTestDataCommandArguments.getDepartmentsCount());
		assertEquals(InsertTestDataCommandArguments.MAX_JOBS_COUNT, insertTestDataCommandArguments.getJobsCount());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testExportDataIncorrectArgumentsCount() {
		parseArguments( Command.EXPORT_DATA_TO_XML.toString(), "arg1", "arg2" );
	}
	@Test(expected = IllegalArgumentException.class)
	public void testExportDataEmptyFileName() {
		parseArguments( Command.EXPORT_DATA_TO_XML.toString(), "    " );
	}
	@Test()
	public void testExportDataCorrectArguments() {
		final String fileName = "xmlData.xml";
		CommandArguments parsedArguments = parseArguments(Command.EXPORT_DATA_TO_XML.toString(), fileName);
		assertTrue(parsedArguments instanceof ExportDataCommandArguments);

		ExportDataCommandArguments exportDataCommandArguments = (ExportDataCommandArguments) parsedArguments;
		assertEquals(fileName, exportDataCommandArguments.getFileName());
	}

	@Test(expected = IllegalArgumentException.class)
	public void testImportDataIncorrectArgumentsCount() {
		parseArguments( Command.IMPORT_DATA_FROM_XML.toString(), "arg1", "arg2" );
	}
	@Test(expected = IllegalArgumentException.class)
	public void testImportDataEmptyFileName() {
		parseArguments( Command.IMPORT_DATA_FROM_XML.toString(), "    " );
	}
	@Test()
	public void testImportDataCorrectArguments() {
		final String fileName = "xmlData.xml";
		CommandArguments parsedArguments = parseArguments(Command.IMPORT_DATA_FROM_XML.toString(), fileName);
		assertTrue(parsedArguments instanceof ImportDataCommandArguments);

		ImportDataCommandArguments exportDataCommandArguments = (ImportDataCommandArguments) parsedArguments;
		assertEquals(fileName, exportDataCommandArguments.getFileName());
	}


	private CommandArguments parseArguments(String... arguments) {
		logger.info("");
		logger.info("====================================");
		logger.info( concat(sb, "arguments: ", StringUtils.getSeparatedString(" ", arguments)) );

		try
		{
			return BasicCommandArguments.parseArgumentsDependingOnCommand(arguments);
		}
		catch (RuntimeException e)
		{
			logger.error("Error while parsing arguments", e);
			throw e;
		}
	}

	private StringBuffer sb = new StringBuffer();
	private static final Logger logger = Logger.getLogger(CommandArgumentsTest.class);
}