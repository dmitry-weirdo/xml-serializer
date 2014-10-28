/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

/**
 * Parameter-object, хранящий в&nbsp;себе конфигурационные данные, считываемые из properties-файла настроек.
 */
public class PropertiesConfig
{
	public String getConnectionUrl() {
		return connectionUrl;
	}
	public void setConnectionUrl(String connectionUrl) {
		this.connectionUrl = connectionUrl;
	}
	public String getDriverClass() {
		return driverClass;
	}
	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * JDBC-url для&nbsp;подключения к базе данных.
	 */
	private String connectionUrl;

	/**
	 * Полное наименование класса JDBC-драйвера для&nbsp;подключения к&nbsp;базе данных.
	 */
	private String driverClass;

	/**
	 * Имя пользователя для подключения к базе данных.
	 */
	private String userName;

	/**
	 * Пароль для&nbsp;подключения к&nbsp;базе данных.
	 */
	private String password;
}