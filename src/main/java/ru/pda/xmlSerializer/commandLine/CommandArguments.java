/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandLine;

/**
 * Интерфейс для&nbsp;объекта, хранящего параметры командной строки
 * для&nbsp;той или&nbsp;иной команды.
 */
public interface CommandArguments
{
	/**
	 * @return команда, выполняемая при&nbsp;заданных аргументах.
	 */
	Command getCommand();

	/**
	 * @return ожидаемое число аргументов. Переданное число аргументов
	 * должно точно соотвествовать ожидаемому.
	 * Число аргументов должно быть больше либо равно 1.
	 * Первый аргумент всегда содержит команду.
	 */
	byte getExpectedArgumentsCount();

	/**
	 * @return ожидаемый формат команды.
	 */
	String getExpectedFormat();

	/**
	 * @param arguments аргументы командной строки.
	 * @return объект, хранящий параметры командной строки
	 * @throws IllegalArgumentException если переданные аргументы некорректны.
	 */
	CommandArguments parseArguments(String[] arguments);

	/**
	 * Выполняет проверку внутреннего состояния считанных аргументов.
	 * @return <code>true</code> &mdash; если все считанные аргументы валидны, <code>false</code> &mdash; в&nbsp;противном случае.
	 */
	boolean isValid();
}