package co.il.nmh.compare.files.gui;

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import co.il.nmh.compare.files.core.DuplicateScanner;
import co.il.nmh.compare.files.data.CFile;
import co.il.nmh.compare.files.gui.listeners.DuplicateScannerListener;
import co.il.nmh.compare.files.gui.listeners.ScanListener;
import co.il.nmh.compare.files.gui.panels.BottomPanel;
import co.il.nmh.compare.files.gui.panels.CenterPanel;
import co.il.nmh.compare.files.gui.panels.TopPanel;

/**
 * @author Maor Hamami
 */

public class GUI extends JFrame implements ScanListener, DuplicateScannerListener
{
	private static final long serialVersionUID = -4834867658681140678L;

	protected TopPanel topPanel;
	protected CenterPanel centerPanel;
	protected BottomPanel bottomPanel;

	protected DuplicateScanner duplicateScanner;

	public GUI()
	{
		setTitle("Compare Files");

		buildPanel();
		addEvents();

		pack();
		setVisible(true);

		GraphicsDevice gd = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();
		int height = gd.getDisplayMode().getHeight();
		height -= height / 1.2;

		setMinimumSize(new Dimension(gd.getDisplayMode().getWidth() / 4, height));
		setLocationRelativeTo(null);
	}

	private void buildPanel()
	{
		setLayout(new GridBagLayout());

		topPanel = new TopPanel();
		centerPanel = new CenterPanel();
		bottomPanel = new BottomPanel();

		GridBagConstraints gridBagConstraints = new GridBagConstraints();

		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.weightx = 1;
		gridBagConstraints.gridy++;

		add(topPanel, gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridy++;
		add(centerPanel, gridBagConstraints);

		gridBagConstraints.gridy++;
		add(bottomPanel, gridBagConstraints);
	}

	private void addEvents()
	{
		this.addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				System.exit(0);
			}
		});

		centerPanel.addScanListener(this);
	}

	public void lockGUI(boolean lock)
	{
		topPanel.lockGUI(lock);
		centerPanel.lockGUI(lock);
	}

	@Override
	public void startScan(String directory, String filter, boolean subDirectories, boolean fullSignature)
	{
		topPanel.clear();
		lockGUI(true);

		try
		{
			duplicateScanner = new DuplicateScanner(this, directory, filter, subDirectories, fullSignature);
			duplicateScanner.start();
		}
		catch (Exception e)
		{
			centerPanel.stopScan(e.getMessage());
		}
	}

	@Override
	public void stopScan(String status)
	{
		if (null != duplicateScanner)
		{
			duplicateScanner.interrupt();
			duplicateScanner = null;
		}

		lockGUI(false);
		bottomPanel.updateStatus(status);
	}

	@Override
	public void updateProgress(int current, int maximum)
	{
		updateProgress(current, maximum, null);
	}

	@Override
	public void updateProgress(int current, int maximum, String status)
	{
		if (null != status)
		{
			bottomPanel.updateStatus(status);
		}

		bottomPanel.updateProgress(current, maximum);
	}

	@Override
	public void done(Map<String, List<CFile>> duplicates)
	{
		topPanel.setDuplicates(duplicates);
		centerPanel.stopScan("done");
	}

	@Override
	public void failed(String failure)
	{
		centerPanel.stopScan(failure);
	}
}
