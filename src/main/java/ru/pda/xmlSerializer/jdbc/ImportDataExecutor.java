/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

import ru.pda.xmlSerializer.DepartmentJob;
import ru.pda.xmlSerializer.DepartmentJobMapUtils;
import ru.pda.xmlSerializer.DepartmentJobNaturalKey;
import ru.pda.xmlSerializer.UserMessageLogger;
import su.opencode.kefir.util.ObjectUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, выполняющий импорт данных из&nbsp;xml-файла в&nbsp;базу данных.
 */
public class ImportDataExecutor extends JdbcTransactionExecutor
{
	public ImportDataExecutor(Map<DepartmentJobNaturalKey, DepartmentJob> xmlJobsMap) {
		this.xmlJobsMap = xmlJobsMap;
	}

	@Override
	protected void execute(Connection connection) throws Exception {
		if ( xmlJobsMap == null || xmlJobsMap.isEmpty() )
		{ // special case - no records in xml file -> delete all records from database
			UserMessageLogger.log(logger, "Xml file contains no DeparmentJob records -> delete all records from database.");
			deleteStatement = connection.prepareStatement( QueryConstructor.getDeleteAllQuery() );
			int deletedJobsCount = deleteStatement.executeUpdate();

			modifiedJobsCounts = new ImportDataModifiedJobsCounts();
			modifiedJobsCounts.setInsertedJobsCount( 0 );
			modifiedJobsCounts.setUpdatedJobsCount( 0 );
			modifiedJobsCounts.setDeletedJobsCount( deletedJobsCount );
			modifiedJobsCounts.setUnmodifiedJobsCount( 0 );

			return;
		}

		List<DepartmentJob> jobsFromDb = selectJobsFromDb(connection);
		UserMessageLogger.log(logger, concat(jobsFromDb.size(), " DepartmentJobs records successfully loaded from database."));

		Map<DepartmentJobNaturalKey, DepartmentJob> dbJobsMap = DepartmentJobMapUtils.createJobsMap(jobsFromDb);
		UserMessageLogger.log(logger, concat(jobsFromDb.size(), " DepartmentJob records from database are valid (have no duplicate natural keys).") );

		ImportDataModifyJobs jobsToModify = fillJobsToModify(xmlJobsMap, dbJobsMap);

		// delete -> reduces records count, so do it first
		List<DepartmentJob> jobsToDelete = jobsToModify.getJobsToDelete();
		if ( ObjectUtils.notEmpty(jobsToDelete) )
		{
			UserMessageLogger.log(logger, concat("Deleting ", jobsToDelete.size(), " old DepartmentJob records"));

			// todo: use delete where id in (...) query, which is much faster than deleting single records
			deleteStatement = connection.prepareStatement( QueryConstructor.getDeleteByIdQuery() );
			for (DepartmentJob job : jobsToDelete)
			{
				deleteStatement.setInt(1, job.getId());

				deleteStatement.addBatch();
//				updateStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
			}
			deleteStatement.executeBatch();
		}

		// update -> records count stays the same, do it second
		List<DepartmentJob> jobsToUpdate = jobsToModify.getJobsToUpdate();
		if ( ObjectUtils.notEmpty(jobsToUpdate) )
		{
			UserMessageLogger.log(logger, concat("Updating ", jobsToUpdate.size(), " changed DepartmentJob records"));

			updateStatement = connection.prepareStatement( QueryConstructor.getUpdateByIdQuery() );
			for (DepartmentJob job : jobsToUpdate)
			{
				updateStatement.setString(1, job.getDescription()); // only description changes
				updateStatement.setInt(2, job.getId());

				updateStatement.addBatch();
//				updateStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
			}
			updateStatement.executeBatch();
		}

		// insert -> adds records, do it last
		List<DepartmentJob> jobsToInsert = jobsToModify.getJobsToInsert();
		if ( ObjectUtils.notEmpty(jobsToInsert) )
		{
			UserMessageLogger.log(logger, concat("Inserting ", jobsToInsert.size(), " new DepartmentJob records"));

			insertStatement = connection.prepareStatement( QueryConstructor.getInsertQuery() );

			for (DepartmentJob job : jobsToInsert)
			{
				insertStatement.setString(1, job.getDepartmentCode());
				insertStatement.setString(2, job.getJobName());
				insertStatement.setString(3, job.getDescription());

				insertStatement.addBatch();
//				insertStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
			}
			insertStatement.executeBatch();
		}

		modifiedJobsCounts = new ImportDataModifiedJobsCounts();
		modifiedJobsCounts.setInsertedJobsCount( jobsToInsert.size() );
		modifiedJobsCounts.setUpdatedJobsCount( jobsToUpdate.size() );
		modifiedJobsCounts.setDeletedJobsCount( jobsToDelete.size() );
		modifiedJobsCounts.setUnmodifiedJobsCount( jobsToModify.getUnmodifiedJobsCount() );
	}
	private List<DepartmentJob> selectJobsFromDb(Connection connection) throws SQLException {
		selectStatement = connection.createStatement();
		String query = QueryConstructor.getSelectQuery();
		logger.info( concat("Select DepartmentJobs query: ", query) );
		selectStatementResultSet = selectStatement.executeQuery(query);

		List<DepartmentJob> dbJobs = new ArrayList<>();
		while (selectStatementResultSet.next())
		{
			DepartmentJob job = new DepartmentJob();

			job.setId(selectStatementResultSet.getInt(1));
			job.setDepartmentCode( selectStatementResultSet.getString(2) );
			job.setJobName( selectStatementResultSet.getString(3) );
			job.setDescription( selectStatementResultSet.getString(4) );

			dbJobs.add(job);
		}

		return dbJobs;
	}

	private ImportDataModifyJobs fillJobsToModify(Map<DepartmentJobNaturalKey, DepartmentJob> xmlJobsMap, Map<DepartmentJobNaturalKey, DepartmentJob> dbJobsMap) {
		StringBuilder sb = new StringBuilder();

		List<DepartmentJob> jobsToInsert = new ArrayList<>();
		List<DepartmentJob> jobsToUpdate = new ArrayList<>();
		List<DepartmentJob> jobsToDelete = new ArrayList<>();
		int unmodifiedJobsCount = 0;

		for (DepartmentJobNaturalKey xmlJobKey : xmlJobsMap.keySet())
		{
			DepartmentJob xmlJob = xmlJobsMap.get(xmlJobKey);

			if (dbJobsMap.containsKey(xmlJobKey))
			{ // job is present both in xml and database
				DepartmentJob dbJob = dbJobsMap.get(xmlJobKey);
				// todo: validate their natural keys equality

				boolean recordHasChanged = !DepartmentJob.haveSameDescription(xmlJob, dbJob); // only description means that record has changed (id does not matter, natural keys are equal)
				if (recordHasChanged)
				{ // record has changed -> it has to be updated
					// set id, updating by id is faster than by natural key (which is 2 strings)
					xmlJob.setId( dbJob.getId() );
					jobsToUpdate.add(xmlJob);

					logger.debug( concat(sb
						, "DepartmentJob with id = ", dbJob.getId(), " has changed."
						, " Record from xml: ", xmlJob
						, " Record from database: ", dbJob
						, " Add it update records."
					) );
				}
				else
				{ // record has not changed -> do nothing with this job
					unmodifiedJobsCount++;

					logger.debug(concat(sb
						, "DepartmentJob with id = ", dbJob.getId(), " has not changed."
						, " Record from xml: ", xmlJob
						, " Record from database: ", dbJob
						, " Do nothing with this record."
					));
				}
			}
			else
			{ // job is present only in xml -> it is to be created
				jobsToInsert.add(xmlJob);

				logger.debug( concat(sb
					, "DepartmentJob is not present in database."
					, " Record from xml: ", xmlJob
					, " Add it to insert records."
				) );
			}
		}

		// get jobs present in db and not present in xml file -> they have to be deleted
		for (DepartmentJobNaturalKey dbJobKey : dbJobsMap.keySet())
		{
			DepartmentJob dbJob = dbJobsMap.get(dbJobKey);

			if ( !xmlJobsMap.containsKey(dbJobKey) )
			{ // record is present in db and not present in xml file -> it has to be deleted
				jobsToDelete.add(dbJob);

				logger.debug( concat(sb
					, "DepartmentJob with id = ", dbJob.getId(), " is present in database, but not present in xml file."
					, " Record from database: ", dbJob
					, " Add it to delete records."
				) );
			}
		}

		ImportDataModifyJobs result = new ImportDataModifyJobs();
		result.setJobsToInsert(jobsToInsert);
		result.setJobsToUpdate(jobsToUpdate);
		result.setJobsToDelete(jobsToDelete);
		result.setUnmodifiedJobsCount(unmodifiedJobsCount);
		return result;
	}

	@Override
	protected void closeStatements() throws SQLException {
		if (selectStatementResultSet != null)
			selectStatementResultSet.close();

		if (selectStatement != null)
			selectStatement.close();

		if (insertStatement != null)
			insertStatement.close();

		if (updateStatement != null)
			updateStatement.close();

		if (deleteStatement != null)
			deleteStatement.close();
	}

	@Override
	protected void logResults() {
		UserMessageLogger.log(logger, concat(modifiedJobsCounts.getInsertedJobsCount(), " DepartmentJob records created in database.") );
		UserMessageLogger.log(logger, concat(modifiedJobsCounts.getUpdatedJobsCount(), " DepartmentJob records updated in database.") );
		UserMessageLogger.log(logger, concat(modifiedJobsCounts.getDeletedJobsCount(), " DepartmentJob records deleted from database.") );
		UserMessageLogger.log(logger, concat(modifiedJobsCounts.getUnmodifiedJobsCount(), " DepartmentJob records were not modified.") );
	}

	public ImportDataModifiedJobsCounts getModifiedJobsCounts() {
		return modifiedJobsCounts;
	}

	private Map<DepartmentJobNaturalKey,DepartmentJob> xmlJobsMap;

	private Statement selectStatement;
	private ResultSet selectStatementResultSet;

	private PreparedStatement insertStatement;

	private PreparedStatement updateStatement;

	private PreparedStatement deleteStatement;

	private ImportDataModifiedJobsCounts modifiedJobsCounts;
}