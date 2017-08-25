/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.loggerConfig;

/**
 * Исключение, выбрасываемое в&nbsp;случае,
 * когда возникает ошибка при&nbsp;конфигурированнии log4j логгера для&nbsp;приложения.
 */
public class LoggerConfiguringException extends Exception
{
	public LoggerConfiguringException() {
	}
	public LoggerConfiguringException(final String message) {
		super(message);
	}
	public LoggerConfiguringException(final String message, final Throwable cause) {
		super(message, cause);
	}
	public LoggerConfiguringException(final Throwable cause) {
		super(cause);
	}
	public LoggerConfiguringException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}