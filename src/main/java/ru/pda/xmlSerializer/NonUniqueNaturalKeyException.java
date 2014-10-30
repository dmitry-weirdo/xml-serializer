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
	public NonUniqueNaturalKeyException(String message) {
		super(message);
	}
	public NonUniqueNaturalKeyException(String message, Throwable cause) {
		super(message, cause);
	}
	public NonUniqueNaturalKeyException(Throwable cause) {
		super(cause);
	}
	public NonUniqueNaturalKeyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}