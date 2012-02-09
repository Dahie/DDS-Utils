package de.danielsenff.badds.view.worker;

import java.awt.BorderLayout;
import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;

import de.danielsenff.badds.view.View;


/**
 * @author danielsenff
 *
 */
public class ProgressDialog extends JDialog {

	protected JProgressBar progressbar;
	protected JPanel panel;
	protected int maxValue;
	
	private ProgressDialog() {}
	
	/**
	 * @param view
	 * @param numFiles
	 */
	public ProgressDialog(View view, int numFiles, String title) {
		super(view);
		init(numFiles, title);
	}
	
	public ProgressDialog(int maxValue, String title) {
		super();
		// Frame set up
		init(maxValue, title);
	}
	
	public ProgressDialog(View view, int numFiles) {
		this(view, numFiles, "Please wait ...");
	}

	private void init(int maxValue, String title) {
		
		JDialog.setDefaultLookAndFeelDecorated(true);
		getRootPane().setWindowDecorationStyle(JRootPane.PLAIN_DIALOG);
		for (int i = 0; i < this.getComponentCount(); i++) {
			System.out.println(super.getComponent(i) );	
		}
		
		this.setName(title);
		this.setTitle(title);
		this.setLocationByPlatform(true);
		
		setLayout(new BorderLayout());
		this.panel = new JPanel();
		add(panel, BorderLayout.CENTER);
		
		// progressbar set up
		this.progressbar = new JProgressBar();
		this.maxValue = maxValue;
		this.progressbar.setMaximum(maxValue);
		this.progressbar.setValue(0);
		this.progressbar.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
		this.progressbar.setVisible(true);
		add(progressbar, BorderLayout.PAGE_END);
		
	}
	

	public JProgressBar getProgressbar() {
		return this.progressbar;
	}

	public int getMaxValue() {
		return this.maxValue;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
		this.progressbar.setMaximum(maxValue);
	}
	
}
