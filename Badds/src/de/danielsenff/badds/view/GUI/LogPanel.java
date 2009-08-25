/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import java.awt.BorderLayout;

import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import de.danielsenff.badds.controller.Application;




/**
 * @author danielsenff
 *
 */
public class LogPanel extends JCPanel {

	private JTextArea logArea;

	/**
	 * @param controller
	 */
	public LogPanel(Application controller) {
		super(controller);
		super.setLayout(new BorderLayout());
		
		JLabel lblLog = new JLabel("Log:");
		super.add(lblLog, BorderLayout.PAGE_START);
		
		logArea = new JTextArea();
		logArea.setLineWrap(true);
		logArea.setEditable(false);
		logArea.setAutoscrolls(true);
		JScrollPane scrollLogArea = new JScrollPane(logArea);
		scrollLogArea.setAutoscrolls(true);
		super.add(scrollLogArea, BorderLayout.CENTER);
		
	}

	/**
	 * Append a string to the log-area
	 */
	public void append(String string) {
		this.logArea.append(string+"\n");
		logArea.setCaretPosition(logArea.getDocument().getLength());
	}
	
}
