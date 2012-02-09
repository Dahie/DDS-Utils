/**
 * 
 */
package de.danielsenff.badds.controller;

import de.danielsenff.badds.model.SwingWorker;
import de.danielsenff.badds.view.View;
import de.danielsenff.badds.view.worker.FileProgressDialog;
import de.danielsenff.badds.view.worker.ProgressDialog;

/**
 * @author danielsenff
 *
 */
public abstract class OperationWorker extends SwingWorker {

	protected View view;
	protected int maxValue;
	protected ProgressDialog dialog;
	
	/**
	 * 
	 */
	public OperationWorker(View view, int maxValue) {
		super();
		this.view = view;
		this.maxValue = maxValue;
		
		this.dialog = new FileProgressDialog(view, maxValue);
		this.dialog.setVisible(true);

		this.dialog.getProgressbar().setStringPainted(true);
	}
	
	/* (non-Javadoc)
	 * @see Model.SwingWorker#start()
	 */
	@Override
	public void start() {
		view.getWorkingView().startWorkingState(maxValue);
		super.start();
	}
	
	protected void setProgressValue(int i) {
		this.dialog.getProgressbar().setValue(i);
	}
	
	@Override
	public void finished() {
		this.view.getWorkingView().endWorkingState("All files done.");
		this.dialog.dispose();
    }
	
}
