package co.il.nmh.compare.files.gui.panels.bottom;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * @author Maor Hamami
 */

public class ProggressPanel extends JPanel
{
	private static final long serialVersionUID = -2684798645925699458L;

	protected JLabel progressLbl;
	protected JProgressBar progressBar;

	public ProggressPanel()
	{
		buildPanel();
	}

	private void buildPanel()
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
