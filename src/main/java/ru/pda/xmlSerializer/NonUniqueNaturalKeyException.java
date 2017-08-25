/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

/**
 * Исключение, выбрасываемое в&nbsp;случае, когда xml-файл
 * содержит должности с&nbsp;одинаковыми натуральными ключами.
 */
public class NonUniqueNaturalKeyException extends Exception
{
	public NonUniqueNaturalKeyException() {
	}
	public NonUniqueNaturalKeyException(final String message) {
		super(message);
	}
	public NonUniqueNaturalKeyException(final String message, final Throwable cause) {
		super(message, cause);
	}
	public NonUniqueNaturalKeyException(final Throwable cause) {
		super(cause);
	}
	public NonUniqueNaturalKeyException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}