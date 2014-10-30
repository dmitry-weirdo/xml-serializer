/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

/**
 * Immutable ключ для&nbsp;помещения натурального ключа
 * {@linkplain DepartmentJob должности в&nbsp;департаменте}
 * в&nbsp;качестве ключа для&nbsp;{@linkplain java.util.HashMap HashMap}.
 */
public final class DepartmentJobNaturalKey
{
	private DepartmentJobNaturalKey(String departmentCode, String jobName) {
		this.departmentCode = departmentCode;
		this.jobName = jobName;
	}

	public static DepartmentJobNaturalKey getInstance(String departmentCode, String departmentJob) {
		return new DepartmentJobNaturalKey(departmentCode, departmentJob);
	}
	public static DepartmentJobNaturalKey getInstance(DepartmentJob job) {
		return new DepartmentJobNaturalKey( job.getDepartmentCode(), job.getJobName() );
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		DepartmentJobNaturalKey that = (DepartmentJobNaturalKey) o;

		if (!departmentCode.equals(that.departmentCode)) return false;
		if (!jobName.equals(that.jobName)) return false;

		return true;
	}
	@Override
	public int hashCode() {
		int result = departmentCode.hashCode();
		result = 31 * result + jobName.hashCode();
		return result;
	}

	/**
	 * Код& отдела.
	 * @see DepartmentJob#departmentCode
	 */
	private final String departmentCode;

	/**
	 * Название должости в&nbsp;отделе.
	 * @see DepartmentJob#jobName
	 */
	private final String jobName;
}