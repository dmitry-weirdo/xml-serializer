/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.pda.xmlSerializer.commandExecutor.ExportDataCommandExecutor;
import ru.pda.xmlSerializer.commandExecutor.ImportDataCommandExecutor;
import ru.pda.xmlSerializer.commandExecutor.InsertTestDataCommandExecutor;
import ru.pda.xmlSerializer.commandLine.*;
import ru.pda.xmlSerializer.jdbc.JdbcDriverRegisterFailException;
import ru.pda.xmlSerializer.loggerConfig.LoggerConfigurator;
import ru.pda.xmlSerializer.loggerConfig.LoggerConfiguringException;
import ru.pda.xmlSerializer.propertiesConfig.IncorrectPropertiesFileException;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import ru.pda.xmlSerializer.xml.IncorrectXmlFileException;
import ru.pda.xmlSerializer.xml.SerializeToXmlFileException;

import java.sql.SQLException;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Main-класс приложения, запускаемый из&nbsp;командной строки с&nbsp;параметрами.
 */
public final class Main
{
	public static void main(final String[] args) {
		try
		{
			final PropertiesConfig config = parsePropertiesConfig(); // файл свойств нужен для конфигурации логгера
			configureLogger(config); // логгер может быть нужен для валидации аргументов командной строки
			final CommandArguments arguments = parseArguments(args);
			executeCommand(arguments, config);
		}
		catch (final Exception e)
		{ // todo: здесь можно написать отдельное сообщение об ошибке для каждого типа исключения, но не в 6.30 утра. Так исключения будут в стектрейсе
			UserMessageLogger.logError(logger, "Error while executing the program", e);
		}
	}

	private static PropertiesConfig parsePropertiesConfig() throws IncorrectPropertiesFileException {
		return PropertiesConfig.parseFromPropertiesFile(PROPERTIES_CONFIG_FILE_NAME);
	}
	private static void configureLogger(final PropertiesConfig config) throws LoggerConfiguringException {
		LoggerConfigurator.configureLogger(config);
	}
	private static CommandArguments parseArguments(final String[] arguments) {
		return BasicCommandArguments.parseArgumentsDependingOnCommand(arguments);
	}

	private static void executeCommand(final CommandArguments arguments, final PropertiesConfig config) throws JdbcDriverRegisterFailException, SQLException, SerializeToXmlFileException, NonUniqueNaturalKeyException, IncorrectXmlFileException {
		switch (arguments.getCommand())
		{
			case INSERT_TEST_DATA:
				final InsertTestDataCommandArguments insertTestDataArguments = (InsertTestDataCommandArguments) arguments;
				new InsertTestDataCommandExecutor().execute(insertTestDataArguments, config);
				break;

			case EXPORT_DATA_TO_XML:
				final ExportDataCommandArguments exportDataCommandArguments = (ExportDataCommandArguments) arguments;
				new ExportDataCommandExecutor().execute(exportDataCommandArguments, config);
				break;

			case IMPORT_DATA_FROM_XML:
				final ImportDataCommandArguments importDataCommandArguments = (ImportDataCommandArguments) arguments;
				new ImportDataCommandExecutor().execute(importDataCommandArguments, config);
				break;

			default:
				throw new IllegalArgumentException( concat("Unknown command: \"", arguments.getCommand(), "\"") );
		}
	}

	private static final Logger logger = LogManager.getLogger(Main.class);

	/**
	 * Имя файла настроек приложения.
	 * // todo: можно принимать его в аргументах командной строки
	 */
	private static final String PROPERTIES_CONFIG_FILE_NAME = "xmlSerializer.properties";
}