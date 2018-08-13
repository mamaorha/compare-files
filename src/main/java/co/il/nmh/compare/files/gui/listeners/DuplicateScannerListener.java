package co.il.nmh.compare.files.gui.listeners;

import java.util.List;
import java.util.Map;

import co.il.nmh.compare.files.data.CFile;

/**
 * @author Maor Hamami
 */

public interface DuplicateScannerListener
{
	void updateProgress(int current, int maximum);

	void updateProgress(int current, int maximum, String status);

	void done(Map<String, List<CFile>> duplicates);

	void failed(String failure);
}
