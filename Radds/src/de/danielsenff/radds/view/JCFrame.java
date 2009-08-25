/**
 * 
 */
package de.danielsenff.radds.view;

import java.util.ResourceBundle;

import javax.swing.JFrame;

import de.danielsenff.radds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class JCFrame extends JFrame {

	protected Application controller;
	protected ResourceBundle bundle = Application.getBundle();
	
	/**
	 * 
	 */
	public JCFrame(final Application controller) {
		this.controller = controller;
	}
}
