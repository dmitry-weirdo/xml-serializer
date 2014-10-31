/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.xml;

/**
 * Исключение, выбрасываемое в&nbsp;случае возникновения ошибки при&nbsp;сериализации объектов в&nbsp;xml-файл.
 */
public class SerializeToXmlFileException extends Exception
{
	public SerializeToXmlFileException() {
	}
	public SerializeToXmlFileException(String message) {
		super(message);
	}
	public SerializeToXmlFileException(String message, Throwable cause) {
		super(message, cause);
	}
	public SerializeToXmlFileException(Throwable cause) {
		super(cause);
	}
	public SerializeToXmlFileException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}