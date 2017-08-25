/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.loggerConfig;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.Appender;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.FileAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.filter.ThresholdFilter;
import org.apache.logging.log4j.core.layout.PatternLayout;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import su.opencode.kefir.util.StringUtils;

/**
 * Класс, выполняющий конфигурацию log4j логгера для&nbsp;приложения.
 * Настраивается {@linkplain org.apache.logging.log4j.core.appender.FileAppender FileAppender}
 * в&nbsp;файл с&nbsp;указанным именем.
 * Если имя файла логирования не&nbsp;указано, используется {@linkplain PropertiesConfig#DEFAULT_LOG_FILE_NAME имя файла по&nbsp;умолчанию}.
 * Остальные параметры логгера используются по&nbsp;умолчанию.
 */
public final class LoggerConfigurator
{
	private LoggerConfigurator() {
		// private constructor for utils class
	}

	public static void configureLogger(final PropertiesConfig config) throws LoggerConfiguringException {
		configureLogger( config.getLogFileName() );
	}
	public static void configureLogger(final String fileName) throws LoggerConfiguringException {
		try
		{
			String loggerFileName = fileName;
			if ( StringUtils.emptyIfTrimmed(fileName) )
				loggerFileName = PropertiesConfig.DEFAULT_LOG_FILE_NAME;

			final FileAppender fileAppender = FileAppender.newBuilder()
				.withName(FILE_APPENDER_NAME)
				.withFileName(loggerFileName)
				.withAppend(true)
				.withFilter( ThresholdFilter.createFilter(Level.INFO, Filter.Result.ACCEPT, Filter.Result.DENY) )
				.withLayout( PatternLayout.createDefaultLayout() ) // todo: use config with this#PATTERN_LAYOUT
				.build()
			;

			addAppenderToAnyLogger(fileAppender);
		}
		catch (final Exception e)
		{
			throw new LoggerConfiguringException(e);
		}
	}

	private static void addAppenderToAnyLogger(final Appender fileAppender) {
		// add appender to any Logger (here is root) // todo: think whether the following code will work the same way
		// add appender programmatically
		// copied from http://logging.apache.org/log4j/2.x/manual/customconfig.html#AppendingToWritersAndOutputStreams

		try (final LoggerContext context = LoggerContext.getContext(false)) {
			final Configuration config = context.getConfiguration();

			fileAppender.start();
			config.addAppender(fileAppender);
			updateLoggers(fileAppender, config);
		}
	}

	private static void updateLoggers(final Appender appender, final Configuration config) {
        final Level level = null;
        final Filter filter = null;
        for (final LoggerConfig loggerConfig : config.getLoggers().values()) {
            loggerConfig.addAppender(appender, level, filter);
        }
        config.getRootLogger().addAppender(appender, level, filter);
    }

	/**
	 * Название файлового аппендера для&nbsp;приложения
	 */
	private static final String FILE_APPENDER_NAME = "XmlSerializerFileAppender";

	/**
	 * Формат для&nbsp;вывода сообщений в&nbsp;лог.
	 */
	public static final String PATTERN_LAYOUT = "%d %-5p [%c{1}] %m%n";
}