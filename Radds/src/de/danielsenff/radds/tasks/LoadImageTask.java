package de.danielsenff.radds.tasks;

import java.io.File;

import de.danielsenff.radds.controller.ImageController;

/**
 * Load Image file task
 * @author dahie
 *
 * @param <V>
 */
public class LoadImageTask extends LoadFileTask<ImageController, Void> {

	/**
     * Construct a LoadImageGraphTask.
     *
     * @param file the file to load from.
     */
	public LoadImageTask(final File file) {
		super(file);
	}
	
	@Override
	protected ImageController doInBackground() throws Exception {
		ImageController controller = new ImageController(getView());
		controller.openImage(getFile());
		return controller;
	}

	@Override
    protected void succeeded(final ImageController controller) {
    	super.succeeded(controller);
    }
	
}
