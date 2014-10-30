/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.propertiesConfig;

/**
 * Исключение, выбрасываемое в&nbsp;случае,
 * когда структура properties-файла с&nbsp;настройками приложения не&nbsp;является корректной.
 */
public class IncorrectPropertiesFileException extends Exception
{
	public IncorrectPropertiesFileException() {
	}
	public IncorrectPropertiesFileException(String message) {
		super(message);
	}
	public IncorrectPropertiesFileException(String message, Throwable cause) {
		super(message, cause);
	}
	public IncorrectPropertiesFileException(Throwable cause) {
		super(cause);
	}
	public IncorrectPropertiesFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}