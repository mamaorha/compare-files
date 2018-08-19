package co.il.nmh.compare.files.gui.panels.bottom;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

import co.il.nmh.easy.swing.components.gui.EasyPanel;

/**
 * @author Maor Hamami
 */

public class ProggressPanel extends EasyPanel
{
	private static final long serialVersionUID = -2684798645925699458L;

	protected JLabel progressLbl;
	protected JProgressBar progressBar;

	@Override
	protected void buildPanel()
	{
		progressLbl = new JLabel("Progress:");
		add(progressLbl);

		progressBar = new JProgressBar();
		progressBar.setStringPainted(true);
		add(progressBar);
	}

	public void updateProgress(int current, int maximum)
	{
		progressBar.setMaximum(maximum);
		progressBar.setValue(current);
		progressBar.setString(String.valueOf(current) + "/" + String.valueOf(maximum));
	}
}
