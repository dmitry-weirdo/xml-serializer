/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

/**
 * Объект, хранящий количества созданных, измененных, удаленных и&nbsp;неизменнынх записей при&nbsp;синхронизации БД с&nbsp;xml-файлом.
 */
public class ImportDataModifiedJobsCounts
{
	public int getInsertedJobsCount() {
		return insertedJobsCount;
	}
	public void setInsertedJobsCount(int insertedJobsCount) {
		this.insertedJobsCount = insertedJobsCount;
	}
	public int getUpdatedJobsCount() {
		return updatedJobsCount;
	}
	public void setUpdatedJobsCount(int updatedJobsCount) {
		this.updatedJobsCount = updatedJobsCount;
	}
	public int getDeletedJobsCount() {
		return deletedJobsCount;
	}
	public void setDeletedJobsCount(int deletedJobsCount) {
		this.deletedJobsCount = deletedJobsCount;
	}
	public int getUnmodifiedJobsCount() {
		return unmodifiedJobsCount;
	}
	public void setUnmodifiedJobsCount(int unmodifiedJobsCount) {
		this.unmodifiedJobsCount = unmodifiedJobsCount;
	}

	private int insertedJobsCount;
	private int updatedJobsCount;
	private int deletedJobsCount;
	private int unmodifiedJobsCount;
}