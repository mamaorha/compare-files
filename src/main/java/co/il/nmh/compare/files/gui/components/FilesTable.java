package co.il.nmh.compare.files.gui.components;

import java.awt.Component;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import co.il.nmh.compare.files.data.TableItem;
import co.il.nmh.compare.files.gui.listeners.TableItemDeleteListener;
import co.il.nmh.easy.swing.components.table.EasyTable;
import co.il.nmh.easy.swing.components.table.data.ComboBoxData;
import co.il.nmh.easy.swing.components.table.listeners.EasyTableButtonListener;
import co.il.nmh.easy.swing.components.table.listeners.EasyTableComboBoxRender;
import co.il.nmh.easy.swing.components.table.listeners.EasyTableComboChangedListener;

/**
 * @author Maor Hamami
 */

public class FilesTable extends EasyTable implements EasyTableComboBoxRender, EasyTableButtonListener, EasyTableComboChangedListener
{
	private static final String NAME_COLUMN = "Name";
	private static final String LOCATION_COLUMN = "Location";
	private static final String ACTION_COLUMN = "Action";
	private static final String OPEN_ACTION = "open";
	private static final String DELETE_ACTION = "delete";

	protected Set<TableItemDeleteListener> tableItemDeleteListeners;

	public FilesTable()
	{
		super(new String[] { NAME_COLUMN, LOCATION_COLUMN, ACTION_COLUMN });

		// table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		table.setRowSelectionAllowed(false);

		setColumnMinWidth(NAME_COLUMN, 150);

		initTable();

		tableItemDeleteListeners = new HashSet<>();
	}

	private void initTable()
	{
		setComboColumn(NAME_COLUMN, this);
		setComboColumn(LOCATION_COLUMN, this);

		Set<String> buttons = new LinkedHashSet<>();
		buttons.add(OPEN_ACTION);
		buttons.add(DELETE_ACTION);

		setButtonsColumn(ACTION_COLUMN, buttons);

		addButtonListener(this);
		addComboboxListener(this);
	}

	@Override
	public Component renderObject(String columnName, Object value)
	{
		return null;
	}

	@Override
	public ComboBoxData getComboBoxData(String columnName, Object value)
	{
		if (value instanceof TableItem)
		{
			TableItem tableItem = (TableItem) value;

			ComboBoxData comboBoxData = new ComboBoxData();
			comboBoxData.setSelectedIndex(tableItem.getActive());

			if (NAME_COLUMN.equals(columnName))
			{
				comboBoxData.setItems(tableItem.getNames());
			}

			else if (LOCATION_COLUMN.equals(columnName))
			{
				comboBoxData.setItems(tableItem.getLocations());
			}

			return comboBoxData;
		}

		return null;
	}

	@Override
	public void comboChanged(Object value, String columnName, int row, int col, int index)
	{
		if (value instanceof TableItem)
		{
			TableItem tableItem = (TableItem) value;
			tableItem.setActive(index);
		}
	}

	@Override
	public void buttonClicked(Object value, String columnName, int row, int col, String button)
	{
		if (OPEN_ACTION.equals(button))
		{
			open(value);
		}

		else if (DELETE_ACTION.equals(button))
		{
			for (TableItemDeleteListener tableItemDeleteListener : tableItemDeleteListeners)
			{
				tableItemDeleteListener.delete(value, row);
			}
		}
	}

	private void open(Object value)
	{
		if (value instanceof TableItem)
		{
			TableItem tableItem = (TableItem) value;

			String location = tableItem.getLocation();

			if (null != location)
			{
				try
				{
					Runtime.getRuntime().exec("cmd.exe /C start \"Open file\" \"" + location + "\"");
				}
				catch (IOException e)
				{
					JOptionPane.showMessageDialog(null, "Failed to open file at - " + location, "Error", JOptionPane.ERROR_MESSAGE);
				}
			}
		}
	}

	public void addTableItemDeleteListener(TableItemDeleteListener listener)
	{
		tableItemDeleteListeners.add(listener);
	}
}
