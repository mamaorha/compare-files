package co.il.nmh.compare.files.data;

import lombok.Data;

/**
 * @author Maor Hamami
 */

@Data
public class CFile
{
	private String name;
	private String location;
	private String size;

	public CFile(String name, String location, String size)
	{
		this.name = name;
		this.location = location;
		this.size = size;
	}
}
