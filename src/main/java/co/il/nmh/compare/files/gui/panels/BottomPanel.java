package co.il.nmh.compare.files.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import co.il.nmh.compare.files.gui.panels.bottom.ProggressPanel;

/**
 * @author Maor Hamami
 */

public class BottomPanel extends JPanel
{
	private static final long serialVersionUID = 7281231225552104317L;

	protected JLabel statusLbl;
	protected ProggressPanel proggressPanel;

	public BottomPanel()
	{
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

		buildPanel();
	}

	private void buildPanel()
	{
		GridBagConstraints gridBagConstraints = new GridBagConstraints();
		gridBagConstraints.gridy++;

		statusLbl = new JLabel("Waiting");
		add(statusLbl, gridBagConstraints);

		proggressPanel = new ProggressPanel();
		gridBagConstraints.gridy++;
		add(proggressPanel, gridBagConstraints);
	}

	public void updateStatus(String status)
	{
		statusLbl.setText(status);
	}

	public void updateProgress(int current, int maximum)
	{
		proggressPanel.updateProgress(current, maximum);
	}
}
