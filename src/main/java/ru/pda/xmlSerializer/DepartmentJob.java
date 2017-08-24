/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import su.opencode.kefir.util.StringUtils;

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
	 * Проверяет корректность заполнения должности: заполненность необходимых полей и&nbsp;их длину.
	 * @return <code>true</code> &mdash; если сущность заполнена корректно;
	 * <br>
	 * <code>false</code> &mdash; в&nbsp;противном случае.
	 */
	public boolean isValid() {
		if ( StringUtils.emptyIfTrimmed(departmentCode) || (departmentCode.length() > DEPARTMENT_CODE_MAX_LENGTH))
			return false;

		if ( StringUtils.emptyIfTrimmed(jobName) || (jobName.length() > JOB_NAME_MAX_LENGTH))
			return false;

		if ( StringUtils.notEmpty(description) && (description.length() > DESCRIPTION_MAX_LENGTH) )
			return false;

		return true;
	}

	public static boolean haveSameDescription(DepartmentJob job1, DepartmentJob job2) {
		if (job1 == null || job2 == null)
			throw new IllegalArgumentException("Neither job1 nor job2 can be null");

		String description1 = job1.getDescription();
		String description2 = job2.getDescription();

		if (description1 == null)
		{
			return (description2 == null);
		}

		if (description2 == null)
		{
			return false;
		}

		return description1.equals(description2);
	}

	@Override
	public String toString() {
		return StringUtils.concat(
				"["
			,  " id: ", id
			, ", departmentCode: ", departmentCode
			, ", jobName: ", jobName
			, ", description: ", description
			, " ]"
		);
	}

	/**
	 * Идентификатор. Суррогатный ключ, заполняемый инкрементно с&nbsp;помощью генератора в&nbsp;базе данных.
	 * Поле не&nbsp;сериализуется в&nbsp;xml-файл.
	 */
	@XmlTransient
	private Integer id;

	/**
	 * Код отдела.
	 * Максимальная длина &mdash; {@linkplain #DEPARTMENT_CODE_MAX_LENGTH 20 символов}.
	 */
	@XmlElement(name = DEPARTMENT_CODE_XML_ELEMENT_NAME) // only to coincide with DOM parser deserialization
	private String departmentCode;

	/**
	 * Название должности в&nbsp;отделе.
	 * Максимальная длина &mdash; {@linkplain #JOB_NAME_MAX_LENGTH 100 символов}.
	 */
	@XmlElement(name = JOB_NAME_XML_ELEMENT_NAME) // only to coincide with DOM parser deserialization
	private String jobName;

	/**
	 * Комментарий к&nbsp;должности.
	 * Максимальная длина &mdash; {@linkplain #DESCRIPTION_MAX_LENGTH 255 символов}.
	 * Может быть пустым.
	 */
	@XmlElement(name = DESCRIPTION_XML_ELEMENT_NAME) // only to coincide with DOM parser deserialization
	private String description;

	/**
	 * Максимальная длина {@linkplain #departmentCode кода отдела}.
	 */
	public static final int DEPARTMENT_CODE_MAX_LENGTH = 20;

	/**
	 * Максимальная длина {@linkplain #jobName названия должности}.
	 */
	public static final int JOB_NAME_MAX_LENGTH = 100;

	/**
	 * Максимальная длина {@linkplain #description комментария к&nbsp;должности}.
	 */
	public static final int DESCRIPTION_MAX_LENGTH = 255;

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