/**
 * 
 */
package de.danielsenff.radds.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import de.danielsenff.radds.controller.Application;



/**
 * @author danielsenff
 *
 */
public class ActionAddFile extends BasicAction {
	
	/**
	 * @param controller
	 */
	public ActionAddFile(final Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		try {
			File[] files = controller.showImageFileChooser().showFilesDialog();
			if (files.length > 0)
				controller.getFilesListModel().addFiles(files);
		} catch (Exception ex) {

		}
	}



}
