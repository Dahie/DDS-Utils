/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import java.util.ResourceBundle;

import javax.swing.JPanel;

import de.danielsenff.badds.controller.Application;



/**
 * @author danielsenff
 *
 */
public class JCPanel extends JPanel {
	protected Application controller;
	protected ResourceBundle bundle = Application.getBundle();
	
	/**
	 * 
	 */
	public JCPanel(final Application controller) {
		this.controller = controller;
	}
}
