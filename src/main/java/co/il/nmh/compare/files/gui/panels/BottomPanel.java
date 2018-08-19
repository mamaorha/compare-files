package co.il.nmh.compare.files.gui.panels;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.EtchedBorder;

import co.il.nmh.compare.files.gui.panels.bottom.ProggressPanel;
import co.il.nmh.easy.swing.components.gui.EasyPanel;

/**
 * @author Maor Hamami
 */

public class BottomPanel extends EasyPanel
{
	private static final long serialVersionUID = 7281231225552104317L;

	protected JLabel statusLbl;
	protected ProggressPanel proggressPanel;

	@Override
	protected void buildPanel()
	{
		setLayout(new GridBagLayout());
		setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));

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
