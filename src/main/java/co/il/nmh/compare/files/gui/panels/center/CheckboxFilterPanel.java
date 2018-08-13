package co.il.nmh.compare.files.gui.panels.center;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 * @author Maor Hamami
 */

public class CheckboxFilterPanel extends JPanel
{
	private static final long serialVersionUID = -5676162118281086301L;

	protected JCheckBox subDirectoriesCb;
	protected JCheckBox fullSignatureCb;

	public CheckboxFilterPanel()
	{
		buildPanel();
	}

	private void buildPanel()
	{
		subDirectoriesCb = new JCheckBox("Sub Directories");
		subDirectoriesCb.setSelected(true);
		add(subDirectoriesCb);

		fullSignatureCb = new JCheckBox("Full signature check");
		fullSignatureCb.setSelected(false);
		add(fullSignatureCb);
	}

	public boolean isSubDirectories()
	{
		return subDirectoriesCb.isSelected();
	}

	public boolean isFullSignature()
	{
		return fullSignatureCb.isSelected();
	}

	public void lockGUI(boolean lock)
	{
		subDirectoriesCb.setEnabled(!lock);
		fullSignatureCb.setEnabled(!lock);
	}
}
