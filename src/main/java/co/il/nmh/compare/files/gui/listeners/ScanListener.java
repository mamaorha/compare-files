package co.il.nmh.compare.files.gui.listeners;

/**
 * @author Maor Hamami
 */

public interface ScanListener
{
	void startScan(String directory, String filter, boolean subDirectories, boolean fullSignature);

	void stopScan(String status);
}
