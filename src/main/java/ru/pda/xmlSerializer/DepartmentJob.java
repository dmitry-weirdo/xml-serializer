/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import javax.xml.bind.annotation.*;

/**
 * Должность в&nbsp;департаменте (отделе).
 * Является основным модельным классом приложения.
 *
 * Натуральным ключом является пара полей: {@linkplain #departmentCode код отдела}
 * и&nbsp;{@linkplain #jobName название должности в&nbsp;отделе}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = DepartmentJobs.DEPARTMENT_JOB_XML_ELEMENT_NAME)
public class DepartmentJob
{
	public DepartmentJob() {
	}
	public DepartmentJob(String departmentCode, String jobName, String description) {
		this.departmentCode = departmentCode;
		this.jobName = jobName;
		this.description = description;
	}
	public DepartmentJob(String departmentCode, String jobName) {
		this.departmentCode = departmentCode;
		this.jobName = jobName;
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
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Идентификатор. Суррогатный ключ, заполняемый инкрементно с&nbsp;помощью генератора в&nbsp;базе данных.
	 * Поле не сериализуется в&nbsp;xml-файл.
	 */
	@XmlTransient
	private Integer id;

	/**
	 * Код отдела.
	 * Максимальная длина &mdash; 20 символов.
	 */
	@XmlElement(name = DEPARTMENT_CODE_XML_ELEMENT_NAME) // only to coincide with DOM parser deserialization
	private String departmentCode;

	/**
	 * Название должности в&nbsp;отделе.
	 * Максимальная длина &mdash; 100 символов.
	 */
	@XmlElement(name = JOB_NAME_XML_ELEMENT_NAME) // only to coincide with DOM parser deserialization
	private String jobName;

	/**
	 * Комментарий к&nbsp;должности. Может быть пустым.
	 */
	@XmlElement(name = DESCRIPTION_XML_ELEMENT_NAME) // only to coincide with DOM parser deserialization
	private String description;

	/**
	 * Название элемента {@linkplain #departmentCode кода отдела} при&nbsp;сериализации в&nbsp;xml.
	 */
	public static final String DEPARTMENT_CODE_XML_ELEMENT_NAME = "departmentCode"; // for using in DOM (non-JAXB) parser

	/**
	 * Название элемента {@linkplain #jobName названия должности} при&nbsp;сериализации в&nbsp;xml.
	 */
	public static final String JOB_NAME_XML_ELEMENT_NAME = "jobName"; // for using in DOM (non-JAXB) parser

	/**
	 * Название элемента {@linkplain #description комментария к&nbsp;должности} при&nbsp;сериализации в&nbsp;xml.
	 */
	public static final String DESCRIPTION_XML_ELEMENT_NAME = "description"; // for using in DOM (non-JAXB) parser
}