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
	public LoggerConfiguringException(String message) {
		super(message);
	}
	public LoggerConfiguringException(String message, Throwable cause) {
		super(message, cause);
	}
	public LoggerConfiguringException(Throwable cause) {
		super(cause);
	}
	public LoggerConfiguringException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}