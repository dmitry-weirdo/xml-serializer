/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

/**
 * Должность в&nbsp;департаменте (отделе).
 * Является основным модельным классом приложения.
 *
 * Натуральным ключом является пара полей: {@linkplain #departmentCode код отдела}
 * и&nbsp;{@linkplain #departmentJob название должности в&nbsp;отделе}.
 */
public class DepartmentJob
{
	public DepartmentJob() {
	}
	public DepartmentJob(String departmentCode, String departmentJob, String description) {
		this.departmentCode = departmentCode;
		this.departmentJob = departmentJob;
		this.description = description;
	}
	public DepartmentJob(String departmentCode, String departmentJob) {
		this.departmentCode = departmentCode;
		this.departmentJob = departmentJob;
		this.description = null;
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getDepartmentCode() {
		return departmentCode;
	}
	public void setDepartmentCode(String departmentCode) {
		this.departmentCode = departmentCode;
	}
	public String getDepartmentJob() {
		return departmentJob;
	}
	public void setDepartmentJob(String departmentJob) {
		this.departmentJob = departmentJob;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Идентификатор. Суррогатный ключ, заполняемый инкрементно с&nbsp;помощью генератора в&nbsp;базе данных.
	 */
	private Integer id;

	/**
	 * Код отдела.
	 * Максимальная длина &mdash; 20 символов.
	 */
	private String departmentCode;

	/**
	 * Название должности в&nbsp;отделе.
	 * Максимальная длина &mdash; 100 символов.
	 */
	private String departmentJob;

	/**
	 * Комментарий к&nbsp;должности. Может быть пустым.
	 */
	private String description;
}