/**
 * 
 */
package de.danielsenff.radds.actions;

import java.awt.event.ActionEvent;

import de.danielsenff.radds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class ActionCloseAll extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionCloseAll(Application controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		controller.getFilesListModel().removeAllElements();
	}
	
}
