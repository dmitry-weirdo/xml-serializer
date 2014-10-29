/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import static su.opencode.kefir.util.StringUtils.concat;

/**
 * Класс, создающий JDBC-запросы для&nbsp; работы с записями должностей в&nbsp;департаментах.
 * Запросы могут содержать неименованные параметры для&nbsp;использования в&nbsp;{@linkplain java.sql.PreparedStatement PreparedStatement}.
 */
public class QueryConstructor
{
	public static String getInsertQuery() {
		return concat(
			"insert into ", DEPARTMENT_JOB_TABLE_NAME
				, "("
					,       DEPARTMENT_JOB_ID_COLUMN_NAME
					, ", ", DEPARTMENT_JOB_DEPARTMENT_CODE_COLUMN_NAME
					, ", ", DEPARTMENT_JOB_JOB_NAME_COLUMN_NAME
					, ", ", DEPARTMENT_JOB_DESCRIPTION_COLUMN_NAME
				, ")"
				, " values"
				, "("
					,  " gen_id(", DEPARTMENT_JOB_GENERATOR_NAME, ", 1)" // id // firebird specific generation, not very good
					, ", ?" // department_code
					, ", ?" // department_job
					, ", ?" // description
				, ")"
			, ";"
		);
	}

	public static String getSelectQuery() {
		return concat(
			  "select"
			,  " ", DEPARTMENT_JOB_ID_COLUMN_NAME
			, ", ", DEPARTMENT_JOB_DEPARTMENT_CODE_COLUMN_NAME
			, ", ", DEPARTMENT_JOB_JOB_NAME_COLUMN_NAME
			, ", ", DEPARTMENT_JOB_DESCRIPTION_COLUMN_NAME
			, "from ", DEPARTMENT_JOB_TABLE_NAME
		); // no order by needed
	}
	public static String getUpdateByIdQuery() {
		return concat( // do not use table aliases to conform to standard SQL queries
			  "update ", DEPARTMENT_JOB_TABLE_NAME
			, " set"
				, " ", DEPARTMENT_JOB_DESCRIPTION_COLUMN_NAME, " = ?" // only description is updated actually
			, " where ", DEPARTMENT_JOB_ID_COLUMN_NAME, " = ?"
		);
	}
	public static String getDeleteByIdQuery() {
		return concat( // do not use table aliases to conform to standard SQL queries
			  "delete from ", DEPARTMENT_JOB_TABLE_NAME
			, " where ", DEPARTMENT_JOB_ID_COLUMN_NAME, " = ?"
		);
	}
	// todo: delete by 'in' by list of ids query



	public static final String DEPARTMENT_JOB_TABLE_NAME = "Department_job";
	public static final String DEPARTMENT_JOB_ID_COLUMN_NAME = "id";
	public static final String DEPARTMENT_JOB_DEPARTMENT_CODE_COLUMN_NAME = "department_code";
	public static final String DEPARTMENT_JOB_JOB_NAME_COLUMN_NAME = "department_job";
	public static final String DEPARTMENT_JOB_DESCRIPTION_COLUMN_NAME = "description";
	public static final String DEPARTMENT_JOB_GENERATOR_NAME = "department_job_gen";
}