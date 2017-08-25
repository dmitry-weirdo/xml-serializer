/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;


import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static su.opencode.kefir.util.StringUtils.concat;

public final class DepartmentJobMapUtils
{
	private DepartmentJobMapUtils() {
		// private constructor for utils class
	}

	public static Map<DepartmentJobNaturalKey, DepartmentJob> createJobsMap(DepartmentJobs departmentJobs) throws NonUniqueNaturalKeyException {
		return createJobsMap( departmentJobs.getJobs() );
	}
	public static Map<DepartmentJobNaturalKey, DepartmentJob> createJobsMap(List<DepartmentJob> jobs) throws NonUniqueNaturalKeyException {
		if (ObjectUtils.empty(jobs) )
			return Collections.emptyMap();

		Map<DepartmentJobNaturalKey, DepartmentJob> map = new HashMap<>();
		DepartmentJobNaturalKey key;

		for (DepartmentJob job : jobs)
		{
			key = DepartmentJobNaturalKey.getInstance(job);
			if ( map.containsKey(key) )
				throw new NonUniqueNaturalKeyException( concat(
					  "List of DepartmentJobs contains more than one DepartmentJob with same natural key (departmentCode = \"", job.getDepartmentCode(), "\", jobName = \"" , job.getJobName(), "\"):"
					,   " DepartmentJob 1: ", map.get(key)
					, ",  DepartmentJob 2: ", job
				) );

			map.put(key, job);
		}

		return map;
	}
}