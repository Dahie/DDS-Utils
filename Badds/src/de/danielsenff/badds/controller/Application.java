package de.danielsenff.badds.controller;


import java.io.File;
import java.io.IOException;
import java.util.ResourceBundle;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import de.danielsenff.badds.model.ExportOptions;
import de.danielsenff.badds.model.FilesListModel;
import de.danielsenff.badds.model.PresetsComboModel;
import de.danielsenff.badds.model.PresetsFactory;
import de.danielsenff.badds.model.SwingWorker;
import de.danielsenff.badds.operations.Operation;
import de.danielsenff.badds.operations.SourceOperation;
import de.danielsenff.badds.view.View;

import JOGL.DDSImage;
import Model.DDSFile;

/**
 * 
 */

/**
 * @author danielsenff
 *
 */
public class Application  {

	

	
	//View
	private View view;
	private ImageFileChooser imageFileChooser;
	// List with all operations	
	private DefaultListModel operations;
	
	//Models
	private FilesListModel<DDSFile> openFilesTableModel;
	private SwingWorker swingWorker;
	private static ResourceBundle bundle = ResourceBundle.getBundle("Texte");
	private PresetsComboModel presetsModel;
	private ExportOptions options;
	
	
	/**
	 * 
	 */
	public Application() {
	
		System.out.println("Start application");
		System.out.println("Load Filechooser ...");
		this.imageFileChooser = new ImageFileChooser();
		
		this.options = new ExportOptions();
		
		System.out.println("Load FileTableModel ...");
		this.openFilesTableModel = new FilesListModel<DDSFile>();
		
		System.out.println("Load Presets ...");
		this.presetsModel = PresetsFactory.getInstanceFromDefaultLocalFile();
		
		System.out.println("Load View ...");
		this.view = new View(this);
		
		System.out.println("Load OperationsModel ...");
	}
	
	


	/**
	 * List of image operations that are run before saving
	 * @return
	 */
	public DefaultListModel getOperationsStack() {
		return this.operations;
	}
	

	/**
	 * add file to filesList
	 * @param file
	 */
	public void addFile(final File file) {
		this.openFilesTableModel.addFile(file);
	}
	
	
	public void removeFile(int index) {
		this.openFilesTableModel.remove(index);
//		this.view.getOpenFilesTable().getTable().updateUI();
	}
	
	public void addOperation(final Operation operation){
		this.operations.addElement(operation);
	}

	/* (non-Javadoc)
	 * @see main.Controller#getImageFileChooser()
	 */
	public ImageFileChooser showImageFileChooser() {
		return this.imageFileChooser;
	}


	public View getView() {
		return this.view;
	}

	
	public FilesListModel getFilesListModel() {
		return this.openFilesTableModel;
	}

	public static ResourceBundle getBundle() {
		return bundle;
	}

	public SwingWorker getSwingWorker() {
		return this.swingWorker;
	}

	public void setSwingWorker(SwingWorker swingWorker) {
		this.swingWorker = swingWorker;
	}




	/**
	 * 
	 * @return
	 */
	public PresetsComboModel getPresets() {
		return presetsModel;
	}


	public ExportOptions getExportOptions() {
		return this.options;
	}
	
}
