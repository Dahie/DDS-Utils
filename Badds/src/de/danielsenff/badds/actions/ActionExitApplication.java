/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;

import de.danielsenff.badds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class ActionExitApplication extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionExitApplication(Application controller) {
		super(controller);
	}


	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		System.exit(0);
	}

}
