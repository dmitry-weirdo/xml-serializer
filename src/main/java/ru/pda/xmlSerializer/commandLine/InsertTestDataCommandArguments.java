/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.commandLine;

import static su.opencode.kefir.util.StringUtils.concat;

public class InsertTestDataCommandArguments extends BasicCommandArguments
{
	@Override
	public Command getCommand() {
		return Command.INSERT_TEST_DATA;
	}
	@Override
	public byte getExpectedArgumentsCount() {
		return 3;
	}
	@Override
	public String getExpectedFormat() {
		return concat(getCommand(), " departmentsCount jobsCount"
			, " ("
			, MIN_DEPARTMENTS_COUNT, " <= departmentsCount <= ", MAX_DEPARTMENTS_COUNT
			, ", ", MIN_JOBS_COUNT, " <= jobsCount <= ", MAX_JOBS_COUNT
			, ")"
		);
	}

	@Override
	protected CommandArguments parseCorrectArgumentsCount(String[] arguments) {
		try
		{
			departmentsCount = Integer.parseInt(arguments[1]);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException( concat("Incorrect departmentsCount: \"", arguments[1], "\"; departmentsCount must contain an int value") );
		}

		if ( (departmentsCount < MIN_DEPARTMENTS_COUNT) || (departmentsCount > MAX_DEPARTMENTS_COUNT) )
			throw new IllegalArgumentException( concat("Incorrect departmentsCount: ", arguments[1], "; departmentsCount must be not less than ", MIN_DEPARTMENTS_COUNT, " and not more than ", MAX_DEPARTMENTS_COUNT) );

		try
		{
			jobsCount = Integer.parseInt(arguments[2]);
		}
		catch (NumberFormatException e)
		{
			throw new IllegalArgumentException( concat("Incorrect jobsCount: \"", arguments[1], "\" departmentsCount must contain an int value") );
		}

		if ( (jobsCount < MIN_JOBS_COUNT) || (jobsCount > MAX_JOBS_COUNT) )
			throw new IllegalArgumentException( concat("Incorrect jobsCount: ", arguments[1], "; jobsCount must be not less than ", MIN_JOBS_COUNT, " and not more than ", MAX_JOBS_COUNT) );

		return this;
	}

	public int getDepartmentsCount() {
		return departmentsCount;
	}
	public void setDepartmentsCount(int departmentsCount) {
		this.departmentsCount = departmentsCount;
	}
	public int getJobsCount() {
		return jobsCount;
	}
	public void setJobsCount(int jobsCount) {
		this.jobsCount = jobsCount;
	}

	/**
	 * Количество отделов для&nbsp;генерации.
	 */
	private int departmentsCount;

	/**
	 * Количество должностей в&nbsp;каждом отделе для&nbsp;генерации.
	 */
	private int jobsCount;

	/**
	 * Минимальное допустимое количество отделов.
	 */
	public static final int MIN_DEPARTMENTS_COUNT = 1;

	/**
	 * Максимальное допустимое количество отделов.
	 */
	public static final int MAX_DEPARTMENTS_COUNT = 100000;

	/**
	 * Минимальное допустимое количество должностей в&nbsp;каждом отделе.
	 */
	public static final int MIN_JOBS_COUNT = 1;

	/**
	 * Максимальное допустимое количество должностей в&nbsp;каждом отделе.
	 */
	public static final int MAX_JOBS_COUNT = 1000;
}