package de.danielsenff.radds.tasks;


import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import org.jdesktop.application.Task;

import de.danielsenff.radds.Radds;
import de.danielsenff.radds.RaddsView;



/**
 * Abstract Task for loading files.
 * @author danielsenff
 *
 * @param <T>
 * @param <V>
 */
public abstract class LoadFileTask<T, V> extends Task<T, V> {
	
	protected boolean modified = false;
	private static final Logger logger = Logger.getLogger(RaddsView.class.getName());
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
		this.view = (RaddsView) Radds.getApplication().getMainView();
//        super(DocumentEditorView.this.getApplication(), file);
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
        RaddsView.getProgressBar().setIndeterminate(false);
    	RaddsView.getProgressBar().setVisible(false);
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
    	RaddsView.getProgressBar().setIndeterminate(false);
    	RaddsView.getProgressBar().setVisible(false);
    	super.cancelled();
    }
    
	/* Called on the EDT if doInBackground fails because
     * an uncaught exception is thrown.  We show an error
     * dialog here.  The dialog is configured with resources
     * loaded from this Tasks's ResourceMap.
     */
    @Override 
    protected void failed(final Throwable e) {
        logger.log(Level.WARNING, "couldn't load " + getFile(), e);
        final String msg = getResourceMap().getString("loadFailedMessage", getFile());
        final String title = getResourceMap().getString("loadFailedTitle");
        final int type = JOptionPane.ERROR_MESSAGE;
        RaddsView.getProgressBar().setIndeterminate(false);
    	RaddsView.getProgressBar().setVisible(false);
        JOptionPane.showMessageDialog(getView().getFrame(), msg, title, type);
    }

}
