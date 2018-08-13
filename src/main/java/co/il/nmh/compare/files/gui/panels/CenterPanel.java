package co.il.nmh.compare.files.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import co.il.nmh.compare.files.gui.listeners.ScanListener;
import co.il.nmh.compare.files.gui.panels.center.DirectoryPanel;
import co.il.nmh.compare.files.gui.panels.center.FilterPanel;

/**
 * @author Maor Hamami
 */

public class CenterPanel extends JPanel
{
	private static final long serialVersionUID = 293004992088468591L;

	protected DirectoryPanel directoryPanel;
	protected FilterPanel filterPanel;

	public CenterPanel()
	{
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		buildPanel();
		addEvents();
	}

	private void buildPanel()
	{
		directoryPanel = new DirectoryPanel();
		filterPanel = new FilterPanel();

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.gridy++;

		add(directoryPanel, gridBagConstraints);

		gridBagConstraints.gridy++;
		add(filterPanel, gridBagConstraints);

		gridBagConstraints.gridy++;

		JPanel space = new JPanel();
		space.setSize(1, 5);
		add(space, gridBagConstraints);
	}

	private void addEvents()
	{
		directoryPanel.addDirectoryChangedListener(filterPanel);
	}

	public void addScanListener(ScanListener scanListener)
	{
		filterPanel.addScanListener(scanListener);
	}

	public void lockGUI(boolean lock)
	{
		directoryPanel.lockGUI(lock);
		filterPanel.lockGUI(lock);
	}

	public void stopScan(String status)
	{
		filterPanel.stopScan(status);
	}
}
