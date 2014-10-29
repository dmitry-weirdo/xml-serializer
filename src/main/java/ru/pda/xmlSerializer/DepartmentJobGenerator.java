/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, генерирующий тестовые данные: заданное количество отделов,
 * в&nbsp;каждом из&nbsp;которых находится заданное количество должностей.
 */
public class DepartmentJobGenerator
{
	public static List<DepartmentJob> generateJobs(int departmentsCount, int jobsCount, boolean generateDescription) {
		if (departmentsCount <= 0 || jobsCount <= 0)
			return Collections.emptyList(); // todo: think about logging these incorrect parameters

		List<DepartmentJob> jobs = new ArrayList<>();

		for (int i = 1; i <= departmentsCount; i++)
		{
			for (int j = 1; j <= jobsCount; j++)
			{
				jobs.add( generateJob(i, j, generateDescription) );
			}
		}

		return jobs;
	}
	public static List<DepartmentJob> generateJobs(int departmentsCount, int jobsCount) {
		return generateJobs(departmentsCount, jobsCount, true);
	}

	public static DepartmentJob generateJob(int departmentNumber, int jobNumber, String description) {
		StringBuilder sb = new StringBuilder();
		String departmentCode = concat(sb, "Department ", departmentNumber);
		String departmentJob = concat(sb, "Job ", jobNumber);

		return new DepartmentJob(departmentCode, departmentJob, description);
	}
	public static DepartmentJob generateJob(int departmentNumber, int jobNumber, boolean generateDescription) {
		String description = generateDescription ? concat("Comment for Job number ", jobNumber, " in Department number ", departmentNumber) : null;
		return generateJob(departmentNumber, jobNumber, description);
	}
	public static DepartmentJob generateJob(int departmentNumber, int jobNumber) {
		return generateJob(departmentNumber, jobNumber, true);
	}
}