/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer;

import org.apache.log4j.Logger;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;
import su.opencode.kefir.util.StringUtils;

import java.sql.*;
import java.util.List;

public class JdbcConnector
{
	public Connection createConnection(PropertiesConfig config) {
		registerDriver(config);
		return getConnection(config);
	}

	public void insertJobs(Connection connection, List<DepartmentJob> jobs) {
		try
		{
			connection.setAutoCommit(false);

			PreparedStatement insertStatement = connection.prepareStatement( QueryConstructor.getInsertQuery() );

			for (DepartmentJob job : jobs)
			{
				insertStatement.setString(1, job.getDepartmentCode());
				insertStatement.setString(2, job.getJobName());
				insertStatement.setString(3, job.getDescription());

				insertStatement.addBatch();
//				insertStatement.executeUpdate(); // does not really matter in case of one transaction (autoCommit = false)
			}
			insertStatement.executeBatch();

			// commit only after all operations
			connection.commit();
		}
		catch (SQLException e)
		{
			try
			{
				connection.rollback();
			}
			catch (SQLException e1)
			{
				e1.printStackTrace();  // todo: handle exception
			}

			e.printStackTrace();  // todo: handle exception
		}
	}

	private void registerDriver(PropertiesConfig config) {
		// todo: register only if the driver is not yet registered


		Class driverClass = null;

		try
		{
			driverClass = Class.forName( config.getDriverClass() );
		}
		catch (ClassNotFoundException e)
		{
			e.printStackTrace();  // todo: handle exception
		}

		Object driver = null;
		try
		{
			driver = driverClass.newInstance();
		}
		catch (InstantiationException e)
		{
			e.printStackTrace();  // todo: handle exception
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();  // todo: handle exception
		}

		if ( !(driver instanceof Driver) )
		{
			throw new IllegalStateException(StringUtils.concat("Class ", " is not an instance of ", Driver.class.getName()) );
		}

		try
		{
			DriverManager.registerDriver((Driver) driver);
		}
		catch (SQLException e)
		{
			e.printStackTrace();  // todo: handle exception
		}
	}

	private Connection getConnection(PropertiesConfig config) {
		Connection connection = null;

		try
		{

			connection = DriverManager.getConnection(config.getConnectionUrl(), config.getUserName(), config.getPassword());

		}
		catch (SQLException e)
		{
			if (connection != null)
				try
				{
					connection.close();
				}
				catch (SQLException e1)
				{
					e1.printStackTrace();  // todo: handle exception
				}

		}
		return connection;
	}

	private static final Logger logger = Logger.getLogger(JdbcConnector.class);
}