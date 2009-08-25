/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;

import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.util.ResourceLoader;
import de.danielsenff.badds.view.worker.OperationWorker;



/**
 * @author danielsenff
 *
 */
public class ActionCancelSaveAll extends BasicAction {

	OperationWorker worker;
	
	/**
	 * @param controller
	 */
	public ActionCancelSaveAll(Application controller) {
		super(controller);
	}

	public ActionCancelSaveAll(OperationWorker worker) {
		super(null);
		this.worker = worker;
	}
	
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		worker.interrupt();
//		worker.getWorkingView().endWorkingState("Operation aborted");
	}

}
