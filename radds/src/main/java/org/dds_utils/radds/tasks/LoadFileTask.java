package org.dds_utils.radds.tasks;


import java.io.File;

import javax.swing.*;


import org.dds_utils.radds.Radds;
import org.dds_utils.radds.RaddsView;
import javax.swing.Action;


/**
 * Abstract Task for loading files.
 * @author danielsenff
 *
 */
public abstract class LoadFileTask extends Action {
	
	protected boolean modified = false;
	/**
     * File that is loaded.
     */
    protected final File file;
    protected RaddsView view;
	
    /* Construct the LoadFileTask object.  The constructor
     * will run on the EDT, so we capture a reference to the 
     * File to be loaded here.  To keep things simple, the 
     * resources for this Task are specified to be in the same 
     * ResourceMap as the DocumentEditorView class's resources.
     * They're defined in resources/DocumentEditorView.properties.
     */
    public LoadFileTask(final File file) {
    	super(Radds.getApplication());
		this.file = file;
    }

    /**
     * Called on the EDT if doInBackground completes without 
     * error and this Task isn't cancelled.  We update the
     * GUI as well as the file and modified properties here.
     * @param fileContents
     */
    @Override
	protected void succeeded(final T fileContents) {
        getView().setFile(getFile());
//        view.setGraphController((GraphController)fileContents);
        getProgressBar().setIndeterminate(false);
    	getProgressBar().setVisible(false);
//        textArea.setText(fileContents);
        getView().setModified(false);
    }

	protected RaddsView getView() {
		return view;
	}

    /**
     * File this task is handling.
     * @return
     */
    public File getFile() {
		return file;
	}

    @Override
    protected void cancelled() {
    	getProgressBar().setIndeterminate(false);
    	getProgressBar().setVisible(false);
    	super.cancelled();
    }

    private JProgressBar getProgressBar() {
        return RaddsView.getProgressBar();
    }

    /* Called on the EDT if doInBackground fails because
     * an uncaught exception is thrown.  We show an error
     * dialog here.  The dialog is configured with resources
     * loaded from this Tasks's ResourceMap.
     */
    @Override 
    protected void failed(final Throwable e) {
        logger.log(Level.WARNING, "couldn't load " + getFile(), e);
        final String msg = getResourceMap().getString("loadFailedMessage", getFile()) + e.getMessage();
        final String title = getResourceMap().getString("loadFailedTitle");
        final int type = JOptionPane.ERROR_MESSAGE;
        getProgressBar().setIndeterminate(false);
    	getProgressBar().setVisible(false);
        JOptionPane.showMessageDialog(getView().getFrame(), msg, title, type);
    }

}
