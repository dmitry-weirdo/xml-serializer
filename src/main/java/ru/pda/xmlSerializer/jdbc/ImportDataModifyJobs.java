/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

import ru.pda.xmlSerializer.DepartmentJob;

import java.util.List;

/**
 * Объект, хранящий списки для&nbsp;создания, изменения и&nbsp;удаления при&nbsp;синхронизации БД с&nbsp;xml-файлом.
 */
public class ImportDataModifyJobs
{
	public List<DepartmentJob> getJobsToInsert() {
		return jobsToInsert;
	}
	public void setJobsToInsert(List<DepartmentJob> jobsToInsert) {
		this.jobsToInsert = jobsToInsert;
	}
	public List<DepartmentJob> getJobsToUpdate() {
		return jobsToUpdate;
	}
	public void setJobsToUpdate(List<DepartmentJob> jobsToUpdate) {
		this.jobsToUpdate = jobsToUpdate;
	}
	public List<DepartmentJob> getJobsToDelete() {
		return jobsToDelete;
	}
	public void setJobsToDelete(List<DepartmentJob> jobsToDelete) {
		this.jobsToDelete = jobsToDelete;
	}
	public int getUnmodifiedJobsCount() {
		return unmodifiedJobsCount;
	}
	public void setUnmodifiedJobsCount(int unmodifiedJobsCount) {
		this.unmodifiedJobsCount = unmodifiedJobsCount;
	}

	private List<DepartmentJob> jobsToInsert;
	private List<DepartmentJob> jobsToUpdate;
	private List<DepartmentJob> jobsToDelete;
	private int unmodifiedJobsCount;
}