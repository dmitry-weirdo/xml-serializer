/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandExecutor;

import org.apache.log4j.Logger;
import ru.pda.xmlSerializer.*;
import ru.pda.xmlSerializer.commandLine.ImportDataCommandArguments;
import ru.pda.xmlSerializer.jdbc.ImportDataExecutor;
import ru.pda.xmlSerializer.jdbc.ImportDataModifiedJobsCounts;
import ru.pda.xmlSerializer.jdbc.JdbcDriverRegisterFailException;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import ru.pda.xmlSerializer.xml.IncorrectXmlFileException;
import ru.pda.xmlSerializer.xml.XmlSerializer;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.concat;

public class ImportDataCommandExecutor
{
	public void execute(ImportDataCommandArguments config, PropertiesConfig propertiesConfig) throws JdbcDriverRegisterFailException, SQLException, IncorrectXmlFileException, NonUniqueNaturalKeyException {
		if (config == null || !config.isValid())
			throw new IllegalArgumentException("Empty or incorrect ImportDataCommandArguments config");

		XmlSerializer serializer = new XmlSerializer();
//		DepartmentJobs departmentJobs = serializer.deserializeFromXml(config.getFileName()); // JAXB parsing
		DepartmentJobs departmentJobs = serializer.deserializeFromXmlUsingDomParser(config.getFileName()); // DOM parsing
		List<DepartmentJob> jobs = departmentJobs.getJobs();
		UserMessageLogger.log(logger, concat(sb, jobs.size(), " DepartmentJob records successfully read from xml file \"", config.getFileName(), "\"") );

		Map<DepartmentJobNaturalKey,DepartmentJob> jobsMap = DepartmentJobMapUtils.createJobsMap(jobs);
		UserMessageLogger.log(logger, concat(sb, "DepartmentJob records from xml file \"", config.getFileName(), "\" are valid (have no duplicate natural keys)") );

		ImportDataExecutor importDataExecutor = new ImportDataExecutor(jobsMap);
		importDataExecutor.processTransaction(propertiesConfig);

		ImportDataModifiedJobsCounts modifiedJobsCounts = importDataExecutor.getModifiedJobsCounts();
		UserMessageLogger.log(logger, concat(sb
			, jobs.size(), " DepartmentJob records from xml file \"", config.getFileName(), "\" successfully imported to database."
			,  " ", modifiedJobsCounts.getInsertedJobsCount(), " records were inserted"
			, ", ", modifiedJobsCounts.getUpdatedJobsCount(), " records were updated"
			, ", ", modifiedJobsCounts.getDeletedJobsCount(), " records were deleted"
			, ", ", modifiedJobsCounts.getUnmodifiedJobsCount(), " records were not modified"
			, "."
		) );
	}

	private StringBuilder sb = new StringBuilder();
	private static final Logger logger = Logger.getLogger(ImportDataCommandExecutor.class);
}