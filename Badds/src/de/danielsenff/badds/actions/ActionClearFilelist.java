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
public class ActionClearFilelist extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionClearFilelist(Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		controller.getFilesListModel().clear();
		controller.getView().getActionClearFilelist().setEnabled(false);
		controller.getView().getActionSaveAll().setEnabled(false);
	}

}
