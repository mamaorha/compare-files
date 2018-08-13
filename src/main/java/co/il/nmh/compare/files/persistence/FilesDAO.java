package co.il.nmh.compare.files.persistence;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import co.il.nmh.compare.files.data.DuplicateBySize;
import co.il.nmh.compare.files.exceptions.PersistenceException;

/**
 * @author Maor Hamami
 */

public class FilesDAO
{
	protected H2Connection h2Connection;

	protected String insertQuery;
	protected String selectedDuplicateBySizeQuery;

	public FilesDAO()
	{
		h2Connection = new H2Connection("files");

		h2Connection.execute("create table files(file varchar(255), location varchar(255) primary key, fileSize varchar(255))");
		h2Connection.execute("CREATE INDEX IDX_NAME ON files(fileSize);");

		buildQueries();
	}

	private void buildQueries()
	{
		insertQuery = "insert into files (file, location, fileSize) values (?, ?, ?)";
		selectedDuplicateBySizeQuery = "select x.* from files x join(select t.fileSize from files t group by t.fileSize having count(t.fileSize) > 1) y on y.fileSize = x.fileSize";
	}

	public void insert(String fileName, String fileLocation, String fileSize)
	{
		try
		{
			PreparedStatement insertPs = h2Connection.getConnection().prepareStatement(insertQuery);

			int i = 1;
			insertPs.setString(i++, fileName);
			insertPs.setString(i++, fileLocation);
			insertPs.setString(i++, fileSize);

			insertPs.executeUpdate();
			insertPs.close();
		}
		catch (Exception e)
		{
			throw new PersistenceException(e);
		}
	}

	public DuplicateBySize getDuplicateBySize()
	{
		try
		{
			PreparedStatement selectedDuplicateBySizePs = h2Connection.getConnection().prepareStatement(selectedDuplicateBySizeQuery);

			ResultSet rs = selectedDuplicateBySizePs.executeQuery();

			DuplicateBySize duplicateBySize = new DuplicateBySize();

			while (rs.next())
			{
				String file = rs.getString("file");
				String location = rs.getString("location");
				String fileSize = rs.getString("fileSize");

				duplicateBySize.addDuplicate(file, location, fileSize);
			}

			rs.close();
			selectedDuplicateBySizePs.close();

			return duplicateBySize;
		}
		catch (Exception e)
		{
			throw new PersistenceException(e);
		}
	}

	public void close()
	{
		h2Connection.close();
	}
}
