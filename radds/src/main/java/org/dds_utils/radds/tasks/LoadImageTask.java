package org.dds_utils.radds.tasks;

import java.io.File;
import org.dds_utils.radds.controller.ImageController;

/**
 * Load Image file task
 * @author dahie
 *
 * @param <V>
 */
public class LoadImageTask extends LoadFileTask {

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
