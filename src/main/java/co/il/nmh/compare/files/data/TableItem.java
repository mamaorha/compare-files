package co.il.nmh.compare.files.data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.ToString;

/**
 * @author Maor Hamami
 */
@ToString
public class TableItem
{
	private String uniqueId;
	private int active;
	private List<String> names = new ArrayList<>();
	private List<String> locations = new ArrayList<>();

	public TableItem()
	{
		uniqueId = UUID.randomUUID().toString();
	}

	public void add(String name, String location)
	{
		names.add(name);
		locations.add(location);
	}

	public void remove()
	{
		remove(active);
	}

	public void remove(int index)
	{
		if (index < names.size())
		{
			names.remove(index);
			locations.remove(index);

			if (active > 0 && active >= names.size())
			{
				active--;
			}
		}
	}

	public int getActive()
	{
		return active;
	}

	public void setActive(int active)
	{
		this.active = active;
	}

	public List<String> getNames()
	{
		return names;
	}

	public List<String> getLocations()
	{
		return locations;
	}

	public String getName()
	{
		if (active < names.size())
		{
			return names.get(active);
		}

		return null;
	}

	public String getLocation()
	{
		if (active < locations.size())
		{
			return locations.get(active);
		}

		return null;
	}

	public int size()
	{
		return names.size();
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uniqueId == null) ? 0 : uniqueId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TableItem other = (TableItem) obj;
		if (uniqueId == null)
		{
			if (other.uniqueId != null)
				return false;
		}
		else if (!uniqueId.equals(other.uniqueId))
			return false;
		return true;
	}
}
