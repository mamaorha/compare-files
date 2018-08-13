package co.il.nmh.compare.files.persistence;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.h2.tools.DeleteDbFiles;

import co.il.nmh.compare.files.exceptions.PersistenceException;

/**
 * @author Maor Hamami
 */

public class H2Connection
{
	private String dbName;
	private Connection connection;

	public H2Connection(String dbName)
	{
		this.dbName = dbName;

		init();
	}

	private void init()
	{
		try
		{
			DeleteDbFiles.execute("~", dbName, true);

			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection("jdbc:h2:~/" + dbName + ";LOG=0;CACHE_SIZE=65536;LOCK_MODE=0;UNDO_LOG=0;");
		}
		catch (Exception e)
		{
			throw new PersistenceException(e);
		}
	}

	public void execute(String query)
	{
		try
		{
			Statement stat = connection.createStatement();
			stat.execute(query);
			stat.close();
		}

		catch (Exception e)
		{
			throw new PersistenceException(e);
		}
	}

	public Connection getConnection()
	{
		return connection;
	}

	public void close()
	{
		try
		{
			DeleteDbFiles.execute("~", dbName, true);
			connection.close();
		}

		catch (SQLException e)
		{
		}
	}
}
