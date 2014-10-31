/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

import ru.pda.xmlSerializer.DepartmentJob;
import ru.pda.xmlSerializer.UserMessageLogger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, выполняющий вставку заданных тестовых данных в таблицу должностей.
 * Все существующие данные в&nbsp;таблице должностей удаляются.
 */
public class InsertTestDataExecutor extends JdbcTransactionExecutor
{
	public InsertTestDataExecutor(List<DepartmentJob> jobs) {
		this.jobs = jobs;
	}

	@Override
	protected void execute(Connection connection) throws Exception {
		deleteStatement = connection.prepareStatement( QueryConstructor.getDeleteAllQuery() );
		deletedJobsCount = deleteStatement.executeUpdate();

		insertStatement = connection.prepareStatement( QueryConstructor.getInsertQuery() );

		for (DepartmentJob job : jobs)
		{
			insertStatement.setString(1, job.getDepartmentCode());
			insertStatement.setString(2, job.getJobName());
			insertStatement.setString(3, job.getDescription());

			insertStatement.addBatch();
//				insertStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
		}
		insertStatement.executeBatch();
	}

	@Override
	protected void closeStatements() throws SQLException {
		if (deleteStatement != null)
			deleteStatement.close();

		if (insertStatement != null)
			insertStatement.close();
	}

	@Override
	protected void logResults() {
		UserMessageLogger.log(logger, concat(deletedJobsCount, " DepartmentJob records deleted from database.") );
		UserMessageLogger.log(logger, concat(jobs.size(), " test DepartmentJob records inserted to database.") );
	}

	private List<DepartmentJob> jobs;

	private PreparedStatement deleteStatement;
	private int deletedJobsCount;

	private PreparedStatement insertStatement;
}