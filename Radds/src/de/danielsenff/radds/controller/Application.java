package de.danielsenff.radds.controller;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import de.danielsenff.radds.models.FilesListModel;
import de.danielsenff.radds.view.View;

import Model.DDSFile;
import Model.DDSImageFile;

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
	
		
//		this.imageFileChooser = new ImageFileChooser();
		this.openFilesModel = new FilesListModel();
		
		this.view = new View(this);
		/*
		File[] firstfiles = this.showImageFileChooser().showFilesDialog();
		if (firstfiles != null) 
				this.openFilesModel.addFiles(firstfiles);	
*/
		
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
