/**
 * 
 */
package de.danielsenff.radds.actions;

import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import de.danielsenff.radds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class ActionRemoveFile extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionRemoveFile(Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		if(!controller.getFilesListModel().isSelectionEmpty()) 
			controller.getFilesListModel().removeSelectedItem();
	}

}
