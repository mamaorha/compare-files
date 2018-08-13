package co.il.nmh.compare.files.gui.panels.top;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JPanel;

import co.il.nmh.compare.files.gui.listeners.TableActionListener;

/**
 * @author Maor Hamami
 */

public class TableActionPanel extends JPanel
{
	private static final long serialVersionUID = -3550070229059969654L;

	protected JButton prevBtn;
	protected JButton nextBtn;
	protected JButton deleteAllBtn;

	protected Set<TableActionListener> tableActionListeners;

	public TableActionPanel()
	{
		tableActionListeners = new HashSet<>();

		buildPanel();
		addEvents();
	}

	private void buildPanel()
	{
		prevBtn = new JButton("Prev");
		prevBtn.setEnabled(false);

		nextBtn = new JButton("Next");
		nextBtn.setEnabled(false);

		deleteAllBtn = new JButton("Delete All");
		deleteAllBtn.setEnabled(false);

		add(prevBtn);
		add(nextBtn);
		add(deleteAllBtn);
	}

	private void addEvents()
	{
		prevBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (TableActionListener tableActionListener : tableActionListeners)
				{
					tableActionListener.prev();
				}
			}
		});

		nextBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (TableActionListener tableActionListener : tableActionListeners)
				{
					tableActionListener.next();
				}
			}
		});

		deleteAllBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				for (TableActionListener tableActionListener : tableActionListeners)
				{
					tableActionListener.deleteAll();
				}
			}
		});
	}

	public void addTableActionListener(TableActionListener tableActionListener)
	{
		tableActionListeners.add(tableActionListener);
	}

	public void lockGUI()
	{
		lockPrevBtn(true);
		lockNextBtn(true);
		lockDeleteAllBtn(true);
	}

	public void lockPrevBtn(boolean lock)
	{
		prevBtn.setEnabled(!lock);
	}

	public void lockNextBtn(boolean lock)
	{
		nextBtn.setEnabled(!lock);
	}

	public void lockDeleteAllBtn(boolean lock)
	{
		deleteAllBtn.setEnabled(!lock);
	}
}
