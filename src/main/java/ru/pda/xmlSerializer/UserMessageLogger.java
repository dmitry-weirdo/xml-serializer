/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;


import org.apache.logging.log4j.Logger;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, инкапсулирующий вывод сообщений пользователю для&nbsp;того, чтобы&nbsp;можно было поменять тип вывода
 * на&nbsp;другой в&nbsp;одном месте.
 * В&nbsp;текущей реализации просто выдает сообщения в&nbsp;консоль.
 */
public final class UserMessageLogger
{
	private UserMessageLogger() {
		// private constructor for utils class
	}

	public static void log(final Logger logger, final String message) {
		logger.info(message);
		log(message);
	}
	public static void log(final Logger logger, final String... messages) {
		final String concatenatedMessage = concat(messages);
		logger.info(concatenatedMessage);
		log(concatenatedMessage);
	}
	public static void log(final String message) {
		System.out.println(message);
	}
	public static void log(final String... messages) {
		System.out.println( concat(messages) );
	}

	public static void logError(final Logger logger, final String message, final Throwable e) {
		logger.error(message, e);

		System.out.println(message);
		e.printStackTrace(System.out);
	}
}