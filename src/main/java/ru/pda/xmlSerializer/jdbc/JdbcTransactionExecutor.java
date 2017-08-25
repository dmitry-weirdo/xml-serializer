/**
 * Copyright 2014 <a href="mailto:dmitry.weirdo@gmail.com">Dmitriy Popov</a>.
 $HeadURL$
 $Author$
 $Revision$
 $Date::                      $
 */
package ru.pda.xmlSerializer.jdbc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.pda.xmlSerializer.UserMessageLogger;
import ru.pda.xmlSerializer.propertiesConfig.PropertiesConfig;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Abstract superclass для&nbsp;классов, выполняющих sql-действия в&nbsp;одной транзакции.
 */
public abstract class JdbcTransactionExecutor
{
	public void processTransaction(final PropertiesConfig config) throws SQLException, JdbcDriverRegisterFailException {
		try
		{
			registerDriver(config);
		}
		catch (final JdbcDriverRegisterFailException e)
		{
			UserMessageLogger.logError(logger, "Registering the JDBC driver failed", e);
			throw e;
		}

		Connection connection = null;
		boolean transactionSucceeded = false;

		try
		{
			try
			{
				UserMessageLogger.log(logger, "Trying to create the connection...");
				connection = createConnection(config);
				UserMessageLogger.log(logger, "Connection created successfully.");
			}
			catch (final SQLException e)
			{
				UserMessageLogger.logError(logger, "Creating the connection failed", e);
				throw e; // prevent executing transactions
			}

			UserMessageLogger.log(logger, "Connection to database created successfully.");
			transactionSucceeded = executeTransaction(connection);
		}
		finally
		{
			try
			{
				closeStatements();
			}
			finally
			{
				closeConnection(connection);
			}

			// до сюда дойдет только, если стейтменты и коннект закрылись успешно
			if (transactionSucceeded)
			{
				logResults();
			}
		}
	}
	private static void closeConnection(final Connection connection) throws SQLException {
		if (connection == null)
			return;

		try
		{
			UserMessageLogger.log(logger, "Trying to close the connection...");
			connection.close();
			UserMessageLogger.log(logger, "Connection closed successfully.");
		}
		catch (final SQLException e)
		{
			UserMessageLogger.logError(logger, "Closing the connection failed", e);
			throw e;
			// todo: неясно, что делать в этом случае
		}
	}

	private static void registerDriver(final PropertiesConfig config) throws JdbcDriverRegisterFailException {
		// todo: register only if the driver is not yet registered
		Class driverClass = null;

		try
		{
			logger.info( "Getting JDBC driver class {}...", config.getDriverClass() );
			driverClass = Class.forName( config.getDriverClass() );
			logger.info( "JDBC driver class {} gotten successfully.", config.getDriverClass() );
		}
		catch (final ClassNotFoundException e)
		{
			throw new JdbcDriverRegisterFailException(e);
		}

		final Object driver;
		try
		{
			logger.info( "Instantiating JDBC driver class {}...", driverClass.getName() );
			driver = driverClass.newInstance();
			logger.info( "JDBC driver class {} instantiated successfully.", driverClass.getName() );
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			throw new JdbcDriverRegisterFailException(e);
		}

		if ( !(driver instanceof Driver) )
		{
			throw new JdbcDriverRegisterFailException( String.format("Class %s is not an instance of %s", driver.getClass().getName(), Driver.class.getName()) );
		}

		try
		{
			DriverManager.registerDriver((Driver) driver);
			logger.info( "JDBC driver class {} registered successfully.", driverClass.getName() );
		}
		catch (final SQLException e)
		{
			throw new JdbcDriverRegisterFailException(e);
		}
	}

	private static Connection createConnection(final PropertiesConfig config) throws SQLException {
		return DriverManager.getConnection(config.getConnectionUrl(), config.getUserName(), config.getPassword());
	}

	/**
	 * @param connection открытое соединение
	 * @return <code>true</code> &mdash; если транзакция успешно выполнилась и&nbsp;закоммитилась; <code>false</code> &mdash; в&nbsp;противном случае.
	 * @throws SQLException в&nbsp;случае ошибки при&nbsp;выполненин транзакции, либо ее коммите или&nbsp;откате.
	 */
	protected boolean executeTransaction(final Connection connection) throws SQLException {
		try
		{
			connection.setAutoCommit(false);
			execute(connection);
			connection.commit(); // commit only after all operations
			UserMessageLogger.log(logger, "Transaction committed successfully.");
			return true;
		}
		catch (final Exception e)
		{
			UserMessageLogger.logError(logger, "Transaction execution failed", e);

			try
			{
				UserMessageLogger.log(logger, "Trying to rollback the transaction...");
				connection.rollback();
				UserMessageLogger.log(logger, "Transaction rolled back successfully.");
			}
			catch (final SQLException e1)
			{
				UserMessageLogger.logError(logger, "Transaction rollback failed", e);
				throw e1;
				// todo: неясно, что делать в этом случае
			}

			return false;
		}
	}

	/**
	 * @param connection созданное этим классом соединение
	 * @throws Exception любое исключение приложения либо&nbsp;исключение работы с&nbsp;базой данных. При&nbsp;его выбросе транзакция будет откачена.
	 */
	protected abstract void execute(Connection connection) throws Exception;

	/**
	 * Закрывает все&nbsp;стейтменты запросов, созданных при&nbsp;выполнении транзакции.
	 * @throws SQLException при&nbsp;ошибке закрытие стейтментов
	 */
	protected abstract void closeStatements() throws SQLException;

	/**
	 * При необходимости выполняет логирование об&nbsp;успешных операциях.
	 * Выполняется только в&nbsp;случае успешного выполнения транзакции и&nbsp;закрытии соединения.
	 */
	protected void logResults() {
		// default do nothing
	}

	protected static final Logger logger = LogManager.getLogger(JdbcTransactionExecutor.class);
}