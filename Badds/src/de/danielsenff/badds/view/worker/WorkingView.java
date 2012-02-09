/**
 * 
 */
package de.danielsenff.badds.view.worker;

import java.awt.Cursor;

import de.danielsenff.badds.view.View;


/**
 * @author danielsenff
 *
 */
public class WorkingView {

	private View view;
	
	/**
	 * 
	 */
	public WorkingView(View view) {
		this.view = view;
	}

	public void startWorkingState(int numFiles) {
		
		// actions enabling
		view.getActionCancelSaveAll().setEnabled(true);
		
		view.getActionSaveAll().setEnabled(false);
		view.getActionRemoveFile().setEnabled(false);
		view.getActionAddFile().setEnabled(false);
		view.getActionImportFolder().setEnabled(false);
		
		view.getTabOperations().setEnabled(false);
		
		view.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		view.setEnabled(false);
		
		//progressbar
		
	}

	
	public void endWorkingState(final String message) {
		
		view.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		
		view.setEnabled(true);
		
		// buttons enabling
		view.getActionCancelSaveAll().setEnabled(false);
		
		view.getActionSaveAll().setEnabled(true);
		view.getActionRemoveFile().setEnabled(true);
		view.getActionAddFile().setEnabled(true);
		view.getActionImportFolder().setEnabled(true);
		
		
		view.getTabOperations().setEnabled(true);
		
	}
	
}
