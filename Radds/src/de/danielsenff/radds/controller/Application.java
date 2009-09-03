package de.danielsenff.radds.controller;


import java.awt.image.BufferedImage;
import java.util.ResourceBundle;

import javax.swing.JProgressBar;

import de.danielsenff.radds.models.FilesListModel;
import de.danielsenff.radds.view.View;

/**
 * 
 */

/**
 * @author danielsenff
 *
 */
public class Application extends org.jdesktop.application.SingleFrameApplication {

	
	//GUI
	private View view;
	private ImageFileChooser imageFileChooser;
	// List with all operations	
	
	private FilesListModel openFilesModel;
	
	private static ResourceBundle bundle = ResourceBundle.getBundle("Texte");
	
	/**
	 * 
	 */
	public Application() {
		
		this.openFilesModel = new FilesListModel();
		
		this.view = new View(this);
	}


	
	public void removeFile(int index) {
		this.openFilesModel.remove(index);
	}
	
	

	/* (non-Javadoc)
	 * @see main.Controller#getImageFileChooser()
	 */
	public ImageFileChooser showImageFileChooser() {
		return this.imageFileChooser;
	}


	public JProgressBar getProgressBar() {
		return this.view.getProgressbar();
	};

	public FilesListModel getFilesListModel() {
		return this.openFilesModel;
	}

	public static ResourceBundle getBundle() {
		return bundle;
	}

	public View getView() {
		return this.view;
	}


	

	/**
	 * @return
	 */
	/*public DDSImageFile getCurrentDDSImage() {
		DDSImageFile image = view.getCanvas().getCanvas();
//		DDSImageFile image = this.openFilesModel.getSelectedItem();
		return image;  
	}*/
	
	public BufferedImage getCurrentImage() {
		BufferedImage image = view.getCanvas().getCanvas();
//		DDSImageFile image = this.openFilesModel.getSelectedItem();
		return image;  
	}



	@Override
	protected void startup() {
		
	}
	
}
