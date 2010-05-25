package de.danielsenff.radds.controller;


import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import model.DDSFile;
import model.TEXFile;
import model.TextureImage;
import util.FileUtil;
import util.ImageIOUtils;
import ddsutil.DDSUtil;
import de.danielsenff.radds.RaddsView;
import de.danielsenff.radds.models.FilesListModel;
import de.danielsenff.radds.models.TextureImageFormatLoaderTGA;

/**
 * 
 */

/**
 * @author danielsenff
 *
 */
public class Application extends org.jdesktop.application.SingleFrameApplication {

	//GUI
	private RaddsView view;
	private ImageFileChooser imageFileChooser;
	// List with all operations	

	

	private static ResourceBundle bundle = ResourceBundle.getBundle("Texte");

	/**
	 * 
	 */
	public Application() {
		this.openFilesModel = new FilesListModel();
		this.view = new RaddsView(this);
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

	public RaddsView getView() {
		return this.view;
	}

	public BufferedImage getCurrentImage() {
		BufferedImage image = view.getCanvas().getCanvas();
		return image;  
	}

	@Override
	protected void startup() {
	}

	
}
