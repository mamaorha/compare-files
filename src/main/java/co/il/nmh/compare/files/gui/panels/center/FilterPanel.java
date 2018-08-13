package co.il.nmh.compare.files.gui.panels.center;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import co.il.nmh.compare.files.gui.listeners.DirectoryChangedListener;
import co.il.nmh.compare.files.gui.listeners.ScanListener;

/**
 * @author Maor Hamami
 */

public class FilterPanel extends JPanel implements DirectoryChangedListener
{
	private static final long serialVersionUID = 3634935066206209437L;

	private static final String SCAN = "Scan";
	private static final String STOP = "Stop";

	protected CheckboxFilterPanel checkboxFilterPanel;
	protected FileFilterPanel fileFilterPanel;
	protected JButton scanBtn;

	protected Set<ScanListener> scanListeners;
	protected String directory;

	public FilterPanel()
	{
		setLayout(new GridBagLayout());

		scanListeners = new HashSet<>();
		buildPanel();
		addEvents();
	}

	private void buildPanel()
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy++;

		checkboxFilterPanel = new CheckboxFilterPanel();
		add(checkboxFilterPanel, gridBagConstraints);

		fileFilterPanel = new FileFilterPanel();
		gridBagConstraints.gridy++;
		add(fileFilterPanel, gridBagConstraints);

		scanBtn = new JButton(SCAN);
		scanBtn.setEnabled(false);

		gridBagConstraints.gridy++;
		add(scanBtn, gridBagConstraints);
	}

	private void addEvents()
	{
		scanBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				scan();
			}
		});
	}

	@Override
	public void currentDirectory(String directory)
	{
		this.directory = directory;

		boolean valid = isValidDirectory();
		scanBtn.setEnabled(valid);
	}

	private boolean isValidDirectory()
	{
		boolean valid = false;

		try
		{
			File file = new File(directory);

			if (file.exists() && file.isDirectory())
			{
				valid = true;
			}
		}
		catch (Exception e)
		{
		}
		return valid;
	}

	private void scan()
	{
		if (SCAN.equals(scanBtn.getText()))
		{
			startScan();
		}

		else
		{
			stopScan("Scan manually aborted");
		}
	}

	public void addScanListener(ScanListener scanListener)
	{
		scanListeners.add(scanListener);
	}

	public void lockGUI(boolean lock)
	{
		checkboxFilterPanel.lockGUI(lock);
		fileFilterPanel.lockGUI(lock);
		scanBtn.setEnabled(!lock && isValidDirectory());
	}

	public void startScan()
	{
		scanBtn.setText(STOP);

		String filter = fileFilterPanel.getFilter();
		boolean subDirectories = checkboxFilterPanel.isSubDirectories();
		boolean fullSignature = checkboxFilterPanel.isFullSignature();

		for (ScanListener scanListener : scanListeners)
		{
			scanListener.startScan(directory, filter, subDirectories, fullSignature);
		}
	}

	public void stopScan(String status)
	{
		scanBtn.setText(SCAN);

		for (ScanListener scanListener : scanListeners)
		{
			scanListener.stopScan(status);
		}
	}
}
