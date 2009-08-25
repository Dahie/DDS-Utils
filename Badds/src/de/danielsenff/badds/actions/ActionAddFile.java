/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;
import java.io.File;

import de.danielsenff.badds.controller.Application;




/**
 * @author danielsenff
 *
 */
public class ActionAddFile extends BasicAction {

	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
			File[] files = controller.showImageFileChooser().openFilesDialogue();
			controller.getFilesListModel().addFiles(files);
		} catch (Exception ex) {

		}
	}



}
