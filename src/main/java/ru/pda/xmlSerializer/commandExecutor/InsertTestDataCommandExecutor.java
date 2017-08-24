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
import ru.pda.xmlSerializer.DepartmentJobGenerator;
import ru.pda.xmlSerializer.UserMessageLogger;
import ru.pda.xmlSerializer.commandLine.InsertTestDataCommandArguments;
import ru.pda.xmlSerializer.jdbc.InsertTestDataExecutor;
import ru.pda.xmlSerializer.jdbc.JdbcDriverRegisterFailException;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;

import java.sql.SQLException;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

public class InsertTestDataCommandExecutor
{
	public void execute(InsertTestDataCommandArguments config, PropertiesConfig propertiesConfig) throws JdbcDriverRegisterFailException, SQLException {
		if (config == null || !config.isValid())
			throw new IllegalArgumentException("Empty or incorrect InsertTestDataCommandArguments config");

		List<DepartmentJob> jobs = DepartmentJobGenerator.generateJobs(config.getDepartmentsCount(), config.getJobsCount(), true); // todo: параметр, генерировать ли коммент, можно тоже принимать из аргументов
		UserMessageLogger.log(logger, concat(sb, "Generated ", jobs.size(), " test DepartmentJobs: ", config.getDepartmentsCount(), " departments with ", config.getJobsCount(), " jobs in each department."));

		// todo: здесь можно было бы проверить валидность сгенерированных jobs, но непроверка их позволяет тестировать откат тразакций. Плюс к тому, можно доверять автогенеренным тестовым данным

		new InsertTestDataExecutor(jobs).processTransaction(propertiesConfig);
	}

	private final StringBuffer sb = new StringBuffer();
	private static final Logger logger = Logger.getLogger(InsertTestDataCommandExecutor.class);
}