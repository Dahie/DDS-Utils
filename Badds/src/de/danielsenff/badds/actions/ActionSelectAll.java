/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;

import javax.swing.ListSelectionModel;

import de.danielsenff.badds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class ActionSelectAll extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionSelectAll(Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		ListSelectionModel selectionModel = controller.getFilesListModel().getSelectionModel();
		int lowerIndex = selectionModel.getMinSelectionIndex();
		int upperIndex = selectionModel.getMaxSelectionIndex();
		selectionModel.addSelectionInterval(lowerIndex, upperIndex);
	}

}
