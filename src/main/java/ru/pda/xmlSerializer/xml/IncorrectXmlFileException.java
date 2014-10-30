/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.xml;

/**
 * Исключение, выбрасываемое в&nbsp;случае, когда структура сериализованного xml-файла не&nbsp;является корректной.
 */
public class IncorrectXmlFileException extends Exception
{
	public IncorrectXmlFileException() {
	}
	public IncorrectXmlFileException(String message) {
		super(message);
	}
	public IncorrectXmlFileException(String message, Throwable cause) {
		super(message, cause);
	}
	public IncorrectXmlFileException(Throwable cause) {
		super(cause);
	}
	public IncorrectXmlFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}