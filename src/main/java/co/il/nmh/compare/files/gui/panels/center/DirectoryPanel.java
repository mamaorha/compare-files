package co.il.nmh.compare.files.gui.panels.center;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;

import co.il.nmh.compare.files.gui.listeners.DirectoryChangedListener;
import co.il.nmh.easy.swing.components.gui.EasyPanel;
import co.il.nmh.easy.swing.components.text.EasyTextField;
import co.il.nmh.easy.swing.components.text.listeners.TextChangedListener;

/**
 * @author Maor Hamami
 */

public class DirectoryPanel extends EasyPanel
{
	private static final long serialVersionUID = 2148749816771804492L;

	protected JLabel workDirLbl;
	protected EasyTextField dirTb;
	protected JButton changeDirBtn;

	protected String lastDirectory = ".";
	protected Set<DirectoryChangedListener> directoryChangedListeners;

	@Override
	protected void init(Object[] params)
	{
		directoryChangedListeners = new HashSet<>();
	}

	@Override
	protected void buildPanel()
	{
		workDirLbl = new JLabel("Working Dir:");
		add(workDirLbl);

		dirTb = new EasyTextField(25);
		add(dirTb);

		changeDirBtn = new JButton("Change");
		add(changeDirBtn);
	}

	@Override
	protected void addEvents()
	{
		changeDirBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				changeDir();
			}
		});

		dirTb.addTextChangedListener(new TextChangedListener()
		{
			@Override
			public void textChanged(String newText)
			{
				for (DirectoryChangedListener directoryChangedListener : directoryChangedListeners)
				{
					directoryChangedListener.currentDirectory(newText);
				}
			}
		});
	}

	private void changeDir()
	{
		JFileChooser chooser = new JFileChooser();
		chooser.setDialogTitle("Select Directory");
		chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setCurrentDirectory(new java.io.File(lastDirectory));

		if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
		{
			String dir = chooser.getSelectedFile().toString();
			lastDirectory = dir;

			dirTb.setText(dir);
		}
	}

	public void addDirectoryChangedListener(DirectoryChangedListener directoryChangedListener)
	{
		directoryChangedListeners.add(directoryChangedListener);
	}

	public void lockGUI(boolean lock)
	{
		dirTb.setEnabled(!lock);
		changeDirBtn.setEnabled(!lock);
	}
}
