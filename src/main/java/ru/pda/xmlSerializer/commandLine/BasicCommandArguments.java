/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandLine;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Абстрактный класс, содержащий базовую общую логику проверки для&nbsp;всех команд.
 */
public abstract class BasicCommandArguments implements CommandArguments
{
	@Override
	public CommandArguments parseArguments(String[] arguments) throws IllegalArgumentException {
		if ( arguments == null || arguments.length <= 0 )
			throw new IllegalArgumentException( "No arguments passed" );

		if ( arguments.length != getExpectedArgumentsCount() )
			throw new IllegalArgumentException( concat("Incorrect arguments count: ", arguments.length, " (expected: ", getExpectedArgumentsCount(), ")") );

		Command command = Command.valueOf(arguments[0]);
		if ( command != getCommand() )
			throw new IllegalArgumentException( concat("Incorrect command: \"", command, "\" (expected \"", getCommand(),"\")") );

		return parseCorrectArgumentsCount(arguments);
	}

	/**
	 * Выполняет разбор параметров командной строки с&nbsp;заведомым знанием,
	 * что&nbsp; первый аргумент содержит {@linkplain #getCommand() нужную команду},
	 * а&nbsp;количество параметров строго равно {@linkplain #getExpectedArgumentsCount() необходимому}.
	 * @param arguments аргументы командной строки
	 * @return разобранные параметры командой строки для&nbsp;той или&nbsp;иной команды.
	 * @throws IllegalArgumentException если аргументы кома
	 */
	protected abstract CommandArguments parseCorrectArgumentsCount(String[] arguments) throws IllegalArgumentException;

	public static CommandArguments parseArgumentsDependingOnCommand(String[] arguments) {
		if ( arguments == null || arguments.length <= 0 || arguments[0] == null )
			throw new IllegalArgumentException( "No arguments passed" );

		Command command;
		try
		{
			command = Command.valueOf(arguments[0]);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException( concat("Incorrect command: \"", arguments[0], "\". First argument must be one of the commands: ", Command.INSERT_TEST_DATA, ", ", Command.EXPORT_DATA_TO_XML, ", ", Command.IMPORT_DATA_FROM_XML) );
		}

		CommandArguments commandArguments;
		switch (command)
		{
			case INSERT_TEST_DATA: commandArguments = new InsertTestDataCommandArguments(); break;
			case EXPORT_DATA_TO_XML: commandArguments = new ExportDataCommandArguments(); break;
			case IMPORT_DATA_FROM_XML: commandArguments = new ImportDataCommandArguments(); break;

			default:
				throw new IllegalArgumentException( concat("Incorrect command: \"", command, "\". First argument must be one of the commands: ", Command.INSERT_TEST_DATA, ", ", Command.EXPORT_DATA_TO_XML, ", ", Command.IMPORT_DATA_FROM_XML) );
		}

		try
		{
			return commandArguments.parseArguments(arguments);
		}
		catch (IllegalArgumentException e)
		{
			throw new IllegalArgumentException( concat("Incorrect command line arguments format. Expected format: ", commandArguments.getExpectedFormat()), e);
		}
	}
}