/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

/**
 * Исключение, выбрасываемое в&nbsp;случае,
 * когда возникает ошибка при&nbsp;регистрации JDBC-драйвера.
 *
 * Введено для&nbsp;того, чтобы&nbsp;использовать единое исключение для&nbsp;всех возможных ошибок при&nbsp;регистрации JDBC-драйвера.
 */
public class JdbcDriverRegisterFailException extends Exception
{
	public JdbcDriverRegisterFailException() {
	}
	public JdbcDriverRegisterFailException(final String message) {
		super(message);
	}
	public JdbcDriverRegisterFailException(final String message, final Throwable cause) {
		super(message, cause);
	}
	public JdbcDriverRegisterFailException(final Throwable cause) {
		super(cause);
	}
	public JdbcDriverRegisterFailException(final String message, final Throwable cause, final boolean enableSuppression, final boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}