package co.il.nmh.compare.files.data;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.ToString;

/**
 * @author Maor Hamami
 */

@Getter
@ToString
public class DuplicateBySize
{
	protected int total;
	protected Map<String, List<CFile>> duplicatesBySize = new LinkedHashMap<String, List<CFile>>();;

	public void addDuplicate(String file, String location, String fileSize)
	{
		if (!duplicatesBySize.containsKey(fileSize))
		{
			duplicatesBySize.put(fileSize, new ArrayList<>());
		}

		duplicatesBySize.get(fileSize).add(new CFile(file, location, fileSize));
		total++;
	}
}
