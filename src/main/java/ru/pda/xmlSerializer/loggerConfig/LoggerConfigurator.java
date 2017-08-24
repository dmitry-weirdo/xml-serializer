/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.loggerConfig;

import org.apache.log4j.*;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import su.opencode.kefir.util.StringUtils;

/**
 * Класс, выполняющий конфигурацию log4j логгера для&nbsp;приложения.
 * Настраивается {@linkplain org.apache.log4j.FileAppender FileAppender}
 * в&nbsp;файл с&nbsp;указанным именем.
 * Если имя файла логирования не&nbsp;указано, используется {@linkplain PropertiesConfig#DEFAULT_LOG_FILE_NAME имя файла по&nbsp;умолчанию}.
 * Остальные параметры логгера используются по&nbsp;умолчанию.
 */
public final class LoggerConfigurator
{
	private LoggerConfigurator() {
		// private constructor for utils class
	}

	public static void configureLogger(PropertiesConfig config) throws LoggerConfiguringException {
		configureLogger( config.getLogFileName() );
	}
	public static void configureLogger(String fileName) throws LoggerConfiguringException {
		try
		{
			String loggerFileName = fileName;
			if ( StringUtils.emptyIfTrimmed(fileName) )
				loggerFileName = PropertiesConfig.DEFAULT_LOG_FILE_NAME;

			FileAppender fileAppender  = new FileAppender();
			fileAppender.setName(FILE_APPENDER_NAME);
			fileAppender.setFile(loggerFileName);
			fileAppender.setAppend(true);
			fileAppender.setThreshold(Level.INFO);
			fileAppender.activateOptions();
			fileAppender.setLayout( new PatternLayout(PATTERN_LAYOUT) );

			// add appender to any Logger (here is root)
			Logger.getRootLogger().addAppender(fileAppender);
		}
		catch (Exception e)
		{
			throw new LoggerConfiguringException(e);
		}
	}

	/**
	 * Название файлового аппендера для&nbsp;приложения
	 */
	public static final String FILE_APPENDER_NAME = "XmlSerializerFileAppender";

	/**
	 * Формат для&nbsp;вывода сообщений в&nbsp;лог.
	 */
	public static final String PATTERN_LAYOUT = "%d %-5p [%c{1}] %m%n";
}