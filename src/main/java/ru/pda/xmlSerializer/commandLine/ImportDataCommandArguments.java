/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandLine;

import su.opencode.kefir.util.StringUtils;

import static su.opencode.kefir.util.StringUtils.concat;

public class ImportDataCommandArguments extends BasicCommandArguments
{
	@Override
	public Command getCommand() {
		return Command.IMPORT_DATA_FROM_XML;
	}
	@Override
	public byte getExpectedArgumentsCount() {
		return 2;
	}
	@Override
	public String getExpectedFormat() {
		return concat(getCommand(), " fileNameToImportDataToDatabase");
	}

	@Override
	protected CommandArguments parseCorrectArgumentsCount(String[] arguments) {
		fileName = arguments[1];

		if (StringUtils.emptyIfTrimmed(fileName))
			throw new IllegalArgumentException("fileName cannot be empty");

		return this;
	}

	public boolean isValid() {
		if ( StringUtils.emptyIfTrimmed(fileName) )
			return false;

		return true;
	}

	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * Имя (полное или относительное) файла, из&nbsp;которого будет выполняться экспорт данных в&nbsp;базу данных.
	 */
	private String fileName;
}