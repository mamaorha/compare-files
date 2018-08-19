package co.il.nmh.compare.files.gui.panels.center;

import java.awt.Font;

import javax.swing.JComboBox;
import javax.swing.JLabel;

import co.il.nmh.easy.swing.components.gui.EasyPanel;

/**
 * @author Maor Hamami
 */

public class FileFilterPanel extends EasyPanel
{
	private static final long serialVersionUID = -3868020860006882808L;

	protected JLabel typeLbl;
	protected JComboBox<String> typeBox;

	@Override
	protected void buildPanel()
	{
		typeLbl = new JLabel("File type:");
		add(typeLbl);

		typeBox = new JComboBox<String>();
		typeBox.setFont(new Font("Arial", Font.BOLD, 14));
		typeBox.addItem("");
		typeBox.addItem("jpg");
		typeBox.addItem("png");
		typeBox.addItem("gif");
		typeBox.addItem("bmp");
		typeBox.addItem("avi");
		typeBox.addItem("exe");
		typeBox.addItem("jpg | png | gif | bmp");
		typeBox.setEditable(true);
		add(typeBox);
	}

	public String getFilter()
	{
		return typeBox.getSelectedItem().toString();
	}

	public void lockGUI(boolean lock)
	{
		typeBox.setEnabled(!lock);
	}
}
