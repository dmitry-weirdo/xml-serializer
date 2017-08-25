/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

import ru.pda.xmlSerializer.*;

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
	public ImportDataExecutor(final Map<DepartmentJobNaturalKey, DepartmentJob> xmlJobsMap) {
		this.xmlJobsMap = xmlJobsMap;
	}

	@Override
	protected void execute(final Connection connection) throws Exception {
		if ( ObjectUtils.empty(xmlJobsMap) )
		{ // special case - no records in xml file -> delete all records from database
			UserMessageLogger.log(logger, "Xml file contains no DepartmentJob records -> delete all records from database.");
			deleteStatement = connection.prepareStatement( QueryConstructor.getDeleteAllQuery() );
			final int deletedJobsCount = deleteStatement.executeUpdate();

			modifiedJobsCounts = new ImportDataModifiedJobsCounts();
			modifiedJobsCounts.setInsertedJobsCount( 0 );
			modifiedJobsCounts.setUpdatedJobsCount( 0 );
			modifiedJobsCounts.setDeletedJobsCount( deletedJobsCount );
			modifiedJobsCounts.setUnmodifiedJobsCount( 0 );

			return;
		}

		final List<DepartmentJob> jobsFromDb = selectJobsFromDb(connection);
		UserMessageLogger.log(logger, concat(jobsFromDb.size(), " DepartmentJobs records successfully loaded from database."));

		final Map<DepartmentJobNaturalKey, DepartmentJob> dbJobsMap = DepartmentJobMapUtils.createJobsMap(jobsFromDb);
		UserMessageLogger.log(logger, concat(jobsFromDb.size(), " DepartmentJob records from database are valid (have no duplicate natural keys).") );

		final ImportDataModifyJobs jobsToModify = fillJobsToModify(xmlJobsMap, dbJobsMap);

		// delete -> reduces records count, so do it first
		final List<DepartmentJob> jobsToDelete = jobsToModify.getJobsToDelete();
		if ( ObjectUtils.notEmpty(jobsToDelete) )
		{
			UserMessageLogger.log(logger, concat("Deleting ", jobsToDelete.size(), " old DepartmentJob records"));

			// todo: use delete where id in (...) query, which is much faster than deleting single records
			deleteStatement = connection.prepareStatement( QueryConstructor.getDeleteByIdQuery() );
			for (final DepartmentJob job : jobsToDelete)
			{
				deleteStatement.setInt(1, job.getId());

				deleteStatement.addBatch();
//				updateStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
			}
			deleteStatement.executeBatch();
		}

		// update -> records count stays the same, do it second
		final List<DepartmentJob> jobsToUpdate = jobsToModify.getJobsToUpdate();
		if ( ObjectUtils.notEmpty(jobsToUpdate) )
		{
			UserMessageLogger.log(logger, concat("Updating ", jobsToUpdate.size(), " changed DepartmentJob records"));

			updateStatement = connection.prepareStatement( QueryConstructor.getUpdateByIdQuery() );
			for (final DepartmentJob job : jobsToUpdate)
			{
				updateStatement.setString(1, job.getDescription()); // only description changes
				updateStatement.setInt(2, job.getId());

				updateStatement.addBatch();
//				updateStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
			}
			updateStatement.executeBatch();
		}

		// insert -> adds records, do it last
		final List<DepartmentJob> jobsToInsert = jobsToModify.getJobsToInsert();
		if ( ObjectUtils.notEmpty(jobsToInsert) )
		{
			UserMessageLogger.log(logger, concat("Inserting ", jobsToInsert.size(), " new DepartmentJob records"));

			insertStatement = connection.prepareStatement( QueryConstructor.getInsertQuery() );

			for (final DepartmentJob job : jobsToInsert)
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
	private List<DepartmentJob> selectJobsFromDb(final Connection connection) throws SQLException {
		selectStatement = connection.createStatement();
		final String query = QueryConstructor.getSelectQuery();
		logger.info( concat("Select DepartmentJobs query: ", query) );
		selectStatementResultSet = selectStatement.executeQuery(query);

		final List<DepartmentJob> dbJobs = new ArrayList<>();
		while (selectStatementResultSet.next())
		{
			final DepartmentJob job = new DepartmentJob();

			job.setId(selectStatementResultSet.getInt(1));
			job.setDepartmentCode( selectStatementResultSet.getString(2) );
			job.setJobName( selectStatementResultSet.getString(3) );
			job.setDescription( selectStatementResultSet.getString(4) );

			dbJobs.add(job);
		}

		return dbJobs;
	}

	private static ImportDataModifyJobs fillJobsToModify(final Map<DepartmentJobNaturalKey, DepartmentJob> xmlJobsMap, final Map<DepartmentJobNaturalKey, DepartmentJob> dbJobsMap) {
		final List<DepartmentJob> jobsToInsert = new ArrayList<>();
		final List<DepartmentJob> jobsToUpdate = new ArrayList<>();
		final List<DepartmentJob> jobsToDelete = new ArrayList<>();
		int unmodifiedJobsCount = 0;

		for (final DepartmentJobNaturalKey xmlJobKey : xmlJobsMap.keySet())
		{
			final DepartmentJob xmlJob = xmlJobsMap.get(xmlJobKey);

			if (dbJobsMap.containsKey(xmlJobKey))
			{ // job is present both in xml and database
				final DepartmentJob dbJob = dbJobsMap.get(xmlJobKey);
				// todo: validate their natural keys equality

				final boolean recordHasChanged = !DepartmentJob.haveSameDescription(xmlJob, dbJob); // only description means that record has changed (id does not matter, natural keys are equal)
				if (recordHasChanged)
				{ // record has changed -> it has to be updated
					// set id, updating by id is faster than by natural key (which is 2 strings)
					xmlJob.setId( dbJob.getId() );
					jobsToUpdate.add(xmlJob);

					logger.debug( "DepartmentJob with id = {} has changed. Record from xml: {}. Record from database: {}. Add it update records.", dbJob.getId(), xmlJob, dbJob );
				}
				else
				{ // record has not changed -> do nothing with this job
					unmodifiedJobsCount++;

					logger.debug( "DepartmentJob with id = {} has not changed. Record from xml: {}. Record from database: {}. Do nothing with this record.", dbJob.getId(), xmlJob, dbJob );
				}
			}
			else
			{ // job is present only in xml -> it is to be created
				jobsToInsert.add(xmlJob);

				logger.debug( "DepartmentJob is not present in database. Record from xml: {}. Add it to insert records.", xmlJob );
			}
		}

		// get jobs present in db and not present in xml file -> they have to be deleted
		for (final DepartmentJobNaturalKey dbJobKey : dbJobsMap.keySet())
		{
			final DepartmentJob dbJob = dbJobsMap.get(dbJobKey);

			if ( !xmlJobsMap.containsKey(dbJobKey) )
			{ // record is present in db and not present in xml file -> it has to be deleted
				jobsToDelete.add(dbJob);

				logger.debug( "DepartmentJob with id = {} is present in database, but not present in xml file. Record from database: {}. Add it to delete records.", dbJob.getId(), dbJob );
			}
		}

		final ImportDataModifyJobs result = new ImportDataModifyJobs();
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

	private final Map<DepartmentJobNaturalKey,DepartmentJob> xmlJobsMap;

	private Statement selectStatement;
	private ResultSet selectStatementResultSet;

	private PreparedStatement insertStatement;

	private PreparedStatement updateStatement;

	private PreparedStatement deleteStatement;

	private ImportDataModifiedJobsCounts modifiedJobsCounts;
}