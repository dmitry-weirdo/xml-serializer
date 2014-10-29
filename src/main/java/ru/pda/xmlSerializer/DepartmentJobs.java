/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Корневой элемент для&nbsp;сериализации списка {@linkplain DepartmentJob должностей в&nbsp;отделах}.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = DepartmentJobs.DEPARTMENT_JOBS_XML_ELEMENT_NAME)
public class DepartmentJobs
{
	public List<DepartmentJob> getJobs() {
		return jobs;
	}
	public void setJobs(List<DepartmentJob> jobs) {
		this.jobs = jobs;
	}

	@XmlElement(name = DEPARTMENT_JOB_XML_ELEMENT_NAME)
	private List<DepartmentJob> jobs;

	/**
	 * Название корневого элемента при&nbsp;сериализации в&nbsp;xml.
	 */
	public static final String DEPARTMENT_JOBS_XML_ELEMENT_NAME = "departmentJobs"; // for using in DOM (non-JAXB) parser

	/**
	 * Название вложенных элементов с&nbsp;{@linkplain DepartmentJob должностями в&nbsp;отделах} при&nbsp;сериализации в&nbsp;xml.
	 */
	public static final String DEPARTMENT_JOB_XML_ELEMENT_NAME = "departmentJob"; // for using in DOM (non-JAXB) parser
}