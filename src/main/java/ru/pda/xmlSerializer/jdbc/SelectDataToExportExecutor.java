/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

import ru.pda.xmlSerializer.DepartmentJob;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, выполняющий выборку всех записей из&nbsp;таблицы должностей.
 * В&nbsp;дальшейнем эти данные используются для&nbsp;сериализации в&nbsp;xml-файл.
 * Т.к. для&nbsp;сериализации {@linkplain ru.pda.xmlSerializer.DepartmentJob#id id должностей} не&nbsp;нужны, столбец id из&nbsp;базы данных не&nbsp;выбирается
 */
public class SelectDataToExportExecutor extends JdbcTransactionExecutor
{
	@Override
	protected void execute(Connection connection) throws Exception {
		selectStatement = connection.createStatement();

		String query = QueryConstructor.getSelectWithNoIdQuery();
		logger.info( concat("Select DeparmentJobs to export query: ", query) );
		selectStatementResultSet = selectStatement.executeQuery(query);

		selectedJobs = new ArrayList<>();

		while (selectStatementResultSet.next())
		{
			DepartmentJob job = new DepartmentJob();

			job.setDepartmentCode( selectStatementResultSet.getString(1) );
			job.setJobName( selectStatementResultSet.getString(2) );
			job.setDescription( selectStatementResultSet.getString(3) );

			selectedJobs.add(job);
		}
	}
	@Override
	protected void closeStatements() throws SQLException {
		if (selectStatementResultSet != null)
			selectStatementResultSet.close();

		if (selectStatement != null)
			selectStatement.close();
	}

	@Override
	protected void logResults() {
		logger.info( concat(selectedJobs.size(), " DepartmentJob records selected from database.") );
	}

	public List<DepartmentJob> getSelectedJobs() {
		return selectedJobs;
	}

	private Statement selectStatement;
	private ResultSet selectStatementResultSet;

	private List<DepartmentJob> selectedJobs;
}