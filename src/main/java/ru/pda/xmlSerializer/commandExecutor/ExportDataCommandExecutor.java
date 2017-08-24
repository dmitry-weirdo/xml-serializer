/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandExecutor;

import org.apache.log4j.Logger;
import ru.pda.xmlSerializer.DepartmentJob;
import ru.pda.xmlSerializer.UserMessageLogger;
import ru.pda.xmlSerializer.commandLine.ExportDataCommandArguments;
import ru.pda.xmlSerializer.jdbc.JdbcDriverRegisterFailException;
import ru.pda.xmlSerializer.jdbc.SelectDataToExportExecutor;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import ru.pda.xmlSerializer.xml.SerializeToXmlFileException;
import ru.pda.xmlSerializer.xml.XmlSerializer;

import java.sql.SQLException;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

public class ExportDataCommandExecutor
{
	public void execute(ExportDataCommandArguments config, PropertiesConfig propertiesConfig) throws JdbcDriverRegisterFailException, SQLException, SerializeToXmlFileException {
		if (config == null || !config.isValid())
			throw new IllegalArgumentException("Empty or incorrect ExportDataCommandArguments config");

		SelectDataToExportExecutor selectDataToExportExecutor = new SelectDataToExportExecutor();
		selectDataToExportExecutor.processTransaction(propertiesConfig);

		List<DepartmentJob> jobs = selectDataToExportExecutor.getSelectedJobs();
		UserMessageLogger.log(logger, concat(sb, jobs.size(), " DepartmentJob records successfully read from database"));

		// todo: здесь можно было бы проверить валидность сгенерированных jobs, но эта корректность контролируется базой данных

		new XmlSerializer().serializeToXml(jobs, config.getFileName());
		UserMessageLogger.log(logger, concat(sb, jobs.size(), " DepartmentJob records successfully serialized to xml file \"", config.getFileName(), "\"") );
	}

	private final StringBuilder sb = new StringBuilder();
	private static final Logger logger = Logger.getLogger(ExportDataCommandExecutor.class);
}