/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;
import java.util.ArrayList;

import javax.swing.ListSelectionModel;

import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.util.SelectionHelper;
import de.danielsenff.badds.view.GUI.FilesTable;



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
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(controller.getFilesListModel().size() != 0) {
			FilesTable openFilesTable = controller.getView().getOpenFilesTable();
			ListSelectionModel selModel = openFilesTable.getTable().getSelectionModel();
			
			// get the selected items
			ArrayList<Integer> selectedIndecies = (ArrayList<Integer>) SelectionHelper.getSelectedIndecies(selModel);
			
			// deleted these items
			for (int i = 0; i < selectedIndecies.size(); i++) {
				int removeIndex = selectedIndecies.get(i).intValue()-i; // -i to compensate already removed items
				controller.getFilesListModel().remove(removeIndex);
			}
	
		}
	}

}
