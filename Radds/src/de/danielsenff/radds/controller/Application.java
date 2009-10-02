package de.danielsenff.radds.controller;


import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import Model.DDSFile;
import Model.DDSImageFile;

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


	/**
	 * Evaluates the file and if possible loads the image.
	 * @param file
	 */
	public void setImage(File file) {
		String filename = file.getAbsolutePath();
		
			try {
				if(DDSImageFile.isValidDDSImage(file)) {

					DDSFile ddsfile = new DDSFile(filename);
					if(ddsfile.getTextureType() == DDSFile.TextureType.CUBEMAP ||
							ddsfile.getTextureType() == DDSFile.TextureType.VOLUME) {
						JOptionPane.showMessageDialog(null, 
								"<html>Error: This programm doesn't support cubemaps or volume textures." +
								"<br>"+ddsfile.getFile().getName()+" can not be loaded.</html>",	"Attention", 
								JOptionPane.INFORMATION_MESSAGE);
						return;
					} 
					System.out.println(filename);
					DDSImageFile image = new DDSImageFile(filename);
					getView().setImage(image);
					final long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					System.out.println(mem0);
				}
			} catch (final OutOfMemoryError ex) {
				ex.printStackTrace();
				final long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
				JOptionPane.showMessageDialog(getView(), 
						"<html>Error: Out of memory: " + mem0 +
						"<br>The operation is aborted. </html>",	"Error", 
						JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
}
