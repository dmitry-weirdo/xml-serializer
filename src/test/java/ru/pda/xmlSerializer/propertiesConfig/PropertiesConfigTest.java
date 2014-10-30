/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.propertiesConfig;

import org.junit.Test;

import java.net.URL;

import static org.junit.Assert.assertEquals;

public class PropertiesConfigTest
{
	@Test(expected = IncorrectPropertiesFileException.class)
	public void testNonExistingPropertiesFile() throws IncorrectPropertiesFileException {
		PropertiesConfig.parseFromPropertiesFile("nonExistingFile.properties");
	}
	@Test(expected = IncorrectPropertiesFileException.class)
	public void testNonValidPropertiesFile() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/nonValid.properties");
	}

	@Test(expected = IncorrectPropertiesFileException.class)
	public void testNoConnectionUrl() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/noConnectionUrl.properties");
	}
	@Test(expected = IncorrectPropertiesFileException.class)
	public void testEmptyConnectionUrl() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/emptyConnectionUrl.properties");
	}

	@Test(expected = IncorrectPropertiesFileException.class)
	public void testNoDriverClass() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/noDriverClass.properties");
	}
	@Test(expected = IncorrectPropertiesFileException.class)
	public void testEmptyDriverClass() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/emptyDriverClass.properties");
	}

	@Test(expected = IncorrectPropertiesFileException.class)
	public void testNoUserName() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/noUserName.properties");
	}
	@Test(expected = IncorrectPropertiesFileException.class)
	public void testEmptyUserName() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/emptyUserName.properties");
	}

	@Test(expected = IncorrectPropertiesFileException.class)
	public void testNoPassword() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/noPassword.properties");
	}
	@Test(expected = IncorrectPropertiesFileException.class)
	public void testEmptyPassword() throws IncorrectPropertiesFileException {
		parseProjectConfig("/propertiesConfig/emptyPassword.properties");
	}

	@Test()
	public void testNoLogFileName() throws IncorrectPropertiesFileException {
		PropertiesConfig config = parseProjectConfig("/propertiesConfig/noLogFileName.properties");
		assertEquals("jdbc:firebirdsql://localhost:3050/C:/_fb/xmlSerializer.FDB?encoding=UTF8", config.getConnectionUrl());
		assertEquals("org.firebirdsql.jdbc.FBDriver", config.getDriverClass());
		assertEquals("sysdba", config.getUserName());
		assertEquals("masterkey", config.getPassword());
		assertEquals(PropertiesConfig.DEFAULT_LOG_FILE_NAME, config.getLogFileName());
	}
	@Test()
	public void testEmptyLogFileName() throws IncorrectPropertiesFileException {
		PropertiesConfig config = parseProjectConfig("/propertiesConfig/emptyLogFileName.properties");
		assertEquals("jdbc:firebirdsql://localhost:3050/C:/_fb/xmlSerializer.FDB?encoding=UTF8", config.getConnectionUrl());
		assertEquals("org.firebirdsql.jdbc.FBDriver", config.getDriverClass());
		assertEquals("sysdba", config.getUserName());
		assertEquals("masterkey", config.getPassword());
		assertEquals(PropertiesConfig.DEFAULT_LOG_FILE_NAME, config.getLogFileName());
	}

	@Test()
	public void testFullyFilledConfig() throws IncorrectPropertiesFileException {
		PropertiesConfig config = parseProjectConfig("/propertiesConfig/fullyFilled.properties");
		assertEquals("jdbc:firebirdsql://localhost:3050/C:/_fb/xmlSerializer.FDB?encoding=UTF8", config.getConnectionUrl());
		assertEquals("org.firebirdsql.jdbc.FBDriver", config.getDriverClass());
		assertEquals("sysdba", config.getUserName());
		assertEquals("masterkey", config.getPassword());
		assertEquals("myLog.log", config.getLogFileName());
	}

	private PropertiesConfig parseProjectConfig(String resourceName) throws IncorrectPropertiesFileException {
		URL resource = getClass().getResource(resourceName);
		String fileName = resource.getPath();

		return PropertiesConfig.parseFromPropertiesFile(fileName);
	}
}