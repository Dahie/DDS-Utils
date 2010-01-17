/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import model.DDSFile;

import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.controller.SaveOperationWorker;
import de.danielsenff.badds.model.ExportOptions;
import de.danielsenff.badds.operations.Operation;
import de.danielsenff.badds.operations.SourceOperation;



/**
 * @author danielsenff
 *
 */
public class ActionSaveAll extends BasicAction {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * @param controller
	 */
	public ActionSaveAll(Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		final Vector<DDSFile> openFiles = controller.getFilesListModel().getOpenFilesList();
		int numFiles = openFiles.size();
		
		if(numFiles > 0) {
			ExportOptions exportOptions = controller.getExportOptions();
			Collection<Operation> operations = new Vector<Operation>();
			operations.add(new SourceOperation());
			
			new SaveOperationWorker(controller.getView(), openFiles, exportOptions, operations).start();
		} else {
			JOptionPane.showMessageDialog(controller.getView(), 
					"<html>Please don't push this button again ... untill you added some files.</html>",	"Attention", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

}
