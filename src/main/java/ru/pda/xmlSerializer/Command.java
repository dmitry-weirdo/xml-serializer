/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

/**
 * Команда, выполняемая main-классом приложения.
 * Команда передается в&nbsp;первом аргументе командной строки.
 */
public enum Command
{
	/**
	 * Вставить тестовые данные.
	 * Все существующие данные в&nbsp;таблицах будут удалены.
	 */
	INSERT_TEST_DATA,

	/**
	 * Экспортировать данные из&nbsp;базы данных в&nbsp;xml-файл.
	 */
	EXPORT_DATA_TO_XML,


	/**
	 * Импортировать данные из&nbsp;xml-файла в&nbsp;базу данных.
	 */
	IMPORT_DATA_FROM_XML,
}