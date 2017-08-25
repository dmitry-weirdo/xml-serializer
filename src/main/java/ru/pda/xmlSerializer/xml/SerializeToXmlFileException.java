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
	public SerializeToXmlFileException(final String message) {
		super(message);
	}
	public SerializeToXmlFileException(final String message, final Throwable cause) {
		super(message, cause);
	}
	public SerializeToXmlFileException(final Throwable cause) {
		super(cause);
	}
	public SerializeToXmlFileException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}