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
	public JdbcDriverRegisterFailException(String message) {
		super(message);
	}
	public JdbcDriverRegisterFailException(String message, Throwable cause) {
		super(message, cause);
	}
	public JdbcDriverRegisterFailException(Throwable cause) {
		super(cause);
	}
	public JdbcDriverRegisterFailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}
}