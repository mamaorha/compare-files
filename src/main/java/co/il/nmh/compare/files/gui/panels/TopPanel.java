package co.il.nmh.compare.files.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import co.il.nmh.compare.files.data.CFile;
import co.il.nmh.compare.files.data.TableItem;
import co.il.nmh.compare.files.gui.components.FilesTable;
import co.il.nmh.compare.files.gui.listeners.TableActionListener;
import co.il.nmh.compare.files.gui.listeners.TableItemDeleteListener;
import co.il.nmh.compare.files.gui.panels.top.TableActionPanel;
import co.il.nmh.easy.swing.components.EasyScrollPane;
import co.il.nmh.easy.swing.components.gui.EasyPanel;

/**
 * @author Maor Hamami
 */

public class TopPanel extends EasyPanel implements TableActionListener, TableItemDeleteListener
{
	private static final long serialVersionUID = -4028289385974735039L;

	protected FilesTable filesTable;
	protected JCheckBox askBeforeDeleteCb;
	protected TableActionPanel tableActionPanel;

	protected int max = 50;
	protected int curr;
	protected List<TableItem> tableItems;

	@Override
	protected void buildPanel()
	{
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		filesTable = new FilesTable();
		tableActionPanel = new TableActionPanel();

		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.fill = GridBagConstraints.BOTH;
		gridBagConstraints.gridy++;
		gridBagConstraints.weighty = 1;
		gridBagConstraints.weightx = 1;

		add(new EasyScrollPane(filesTable, 150, 150), gridBagConstraints);

		gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
		gridBagConstraints.weighty = 0;
		gridBagConstraints.gridy++;

		askBeforeDeleteCb = new JCheckBox("Ask before delete");
		askBeforeDeleteCb.setSelected(true);

		JPanel panel = new JPanel();
		panel.add(askBeforeDeleteCb);

		add(panel, gridBagConstraints);

		gridBagConstraints.gridy++;
		add(tableActionPanel, gridBagConstraints);
	}

	@Override
	protected void addEvents()
	{
		tableActionPanel.addTableActionListener(this);
		filesTable.addTableItemDeleteListener(this);
	}

	public void lockGUI(boolean lock)
	{
		askBeforeDeleteCb.setEnabled(!lock);

		if (lock)
		{
			tableActionPanel.lockGUI();
		}
	}

	public void clear()
	{
		filesTable.clear();
	}

	public void setDuplicates(Map<String, List<CFile>> duplicates)
	{
		clear();

		tableItems = new ArrayList<>();

		if (null != duplicates)
		{
			for (List<CFile> files : duplicates.values())
			{
				TableItem tableItem = new TableItem();

				for (CFile file : files)
				{
					tableItem.add(file.getName(), file.getLocation());
				}

				tableItems.add(tableItem);
			}
		}

		populateTable(0);
	}

	private void populateTable(int curr)
	{
		this.curr = curr;
		filesTable.clear();

		int end = curr + max;

		for (int start = curr; start < end && start < tableItems.size(); start++)
		{
			TableItem tableItem = tableItems.get(start);
			filesTable.add(tableItem);
		}

		tableActionPanel.lockDeleteAllBtn(filesTable.rowCount() < 1);
		tableActionPanel.lockNextBtn(curr + max >= tableItems.size());
		tableActionPanel.lockPrevBtn(curr == 0);
	}

	public void refresh()
	{
		int availableItems;

		do
		{
			availableItems = tableItems.size() - curr;

			if (availableItems > max)
			{
				availableItems = max;
			}

			if (curr > 0 && availableItems == 0)
			{
				curr--;
			}
		} while (curr > 0 && availableItems == 0);

		populateTable(curr);
	}

	@Override
	public void prev()
	{
		int start = curr - max;

		if (start < 0)
		{
			start = 0;
		}

		populateTable(start);
	}

	@Override
	public void next()
	{
		int start = curr + max;
		populateTable(start);
	}

	@Override
	public void deleteAll()
	{
		boolean ask = askBeforeDeleteCb.isSelected();
		boolean updated = false;

		Object[] options = { "Yes", "No", "Yes To All", "Stop" };

		List<TableItem> needToDelete = new ArrayList<>();

		for (TableItem tableItem : tableItems)
		{
			boolean cancelled = false;

			for (int i = 0; i < tableItem.size();)
			{
				String name = tableItem.getNames().get(i);
				String location = tableItem.getLocations().get(i);

				boolean allow = true;

				if (ask)
				{
					int result = JOptionPane.showOptionDialog(null, "Are you sure you want to delete the file " + name + "?", "Warning", JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);

					if (result == 1)
					{
						allow = false;
					}

					else if (result == 2)
					{
						ask = false;
					}

					else if (result == 3)
					{
						cancelled = true;
						break;
					}
				}

				if (allow)
				{
					File file = new File(location);

					if (!file.delete())
					{
						JOptionPane.showMessageDialog(null, "Failed to delete file " + file.getAbsolutePath() + ", maybe its in use", "Error", JOptionPane.ERROR_MESSAGE);
					}

					else
					{
						updated = true;
						tableItem.remove(i);

						if (tableItem.getNames().isEmpty() || tableItem.getNames().size() == 1)
						{
							needToDelete.add(tableItem);
							break;
						}

						continue;
					}
				}

				i++;
			}

			if (cancelled)
			{
				break;
			}
		}

		for (TableItem tableItem : needToDelete)
		{
			tableItems.remove(tableItem);
		}

		if (needToDelete.size() > 0)
		{
			populateTable(0);
		}

		else if (updated)
		{
			refresh();
		}
	}

	@Override
	public void delete(Object value, int row)
	{
		if (value instanceof TableItem)
		{
			TableItem tableItem = (TableItem) value;

			String name = tableItem.getName();

			boolean allow = true;

			if (askBeforeDeleteCb.isSelected())
			{
				allow = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete the file " + name + "?", "Warning", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
			}

			if (allow)
			{
				String location = tableItem.getLocation();

				File file = new File(location);

				if (!file.delete())
				{
					JOptionPane.showMessageDialog(null, "Failed to delete file " + file.getAbsolutePath() + ", maybe its in use", "Error", JOptionPane.ERROR_MESSAGE);
				}

				else
				{
					tableItem.remove();

					if (tableItem.getNames().isEmpty() || tableItem.getNames().size() == 1)
					{
						tableItems.remove(tableItem);
						refresh();
					}
				}
			}
		}
	}
}
