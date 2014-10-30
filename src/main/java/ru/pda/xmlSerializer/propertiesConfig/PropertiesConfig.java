/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.propertiesConfig;

import su.opencode.kefir.gen.helper.ObjectFiller;
import su.opencode.kefir.util.StringUtils;

/**
 * Parameter-object, хранящий в&nbsp;себе конфигурационные данные, считываемые из&nbsp;properties-файла настроек.
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
	public String getLogFileName() {
		return logFileName;
	}
	public void setLogFileName(String logFileName) {
		this.logFileName = logFileName;
	}

	/**
	 * Заполняет конфиг приложения из заданного properties-файла.
	 * Проверяется заполнение всех необходимых полей.
	 * Если {@linkplain #logFileName имя файла логирования} в&nbsp;файле пустое, оно заполняется {@linkplain #DEFAULT_LOG_FILE_NAME именем файла по&nbsp;умолчанию}.
	 * @param fileName имя properties-файла, абсолютное или&nbsp;относительное
	 * @return заполненный конфиг приложения
	 * @throws IncorrectPropertiesFileException если произошла ошибка при&nbsp;разборе файла, или&nbsp;наполнение файла невалидно.
	 */
	public static PropertiesConfig parseFromPropertiesFile(String fileName) throws IncorrectPropertiesFileException {
		PropertiesConfig config = null;
		try
		{
			config = ObjectFiller.createObject(fileName, PropertiesConfig.class);
		}
		catch (Exception e)
		{
			throw new IncorrectPropertiesFileException(e);
		}

		config.validate();

		if ( StringUtils.emptyIfTrimmed(config.getLogFileName()) )
			config.setLogFileName(DEFAULT_LOG_FILE_NAME);

		return config;
	}

	public void validate() throws IncorrectPropertiesFileException {
		if ( StringUtils.emptyIfTrimmed(connectionUrl) )
			throw new IncorrectPropertiesFileException("connectionUrl cannot be null or empty");

		if ( StringUtils.emptyIfTrimmed(driverClass) )
			throw new IncorrectPropertiesFileException("driverClass cannot be null or empty");

		if ( StringUtils.emptyIfTrimmed(userName) )
			throw new IncorrectPropertiesFileException("userName cannot be null or empty");

		if ( StringUtils.emptyIfTrimmed(password) )
			throw new IncorrectPropertiesFileException("password cannot be null or empty");

		// logFileName cannot be empty
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

	/**
	 * Имя файла (абсолютное или&nbsp;относительное), в&nbsp;который выполняется логирование.
	 * Может отсутстов или&nbsp;быть пустым, в&nbsp;этом случае используется {@linkplain #DEFAULT_LOG_FILE_NAME имя файла по&nbsp;умолчанию}.
	 */
	private String logFileName;

	/**
	 * Имя файла для&nbsp;логов по&nbsp;умолчанию.
	 */
	public static final String DEFAULT_LOG_FILE_NAME = "xmlSerializer.log";
}