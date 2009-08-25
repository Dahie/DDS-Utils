/**
 * 
 */
package de.danielsenff.radds.models;

import java.io.File;
import java.io.IOException;

import javax.swing.DefaultListModel;
import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;

import Model.DDSFile;
import Model.DDSImageFile;


/**
 * @author danielsenff
 *
 */
public class FilesListModel extends DefaultListModel {

	private ListSelectionModel selectionModel;
	
	/**
	 * 
	 */
	public FilesListModel() {
		this.selectionModel = new DefaultListSelectionModel();
		this.selectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	}
	
	/**
	 * @param item
	 */
	public void add(Object item) {
		this.addElement(item);
		int newSelectionIndex = this.getSize()-1; // hey it's indecies, stupid!
		this.selectionModel.setSelectionInterval(newSelectionIndex, newSelectionIndex);
	}

	public ListSelectionModel getSelectionModel() {
		return this.selectionModel;
	}

	public void setSelectionModel(ListSelectionModel selectionModel) {
		this.selectionModel = selectionModel;
	}

	
	/**
	 * @return
	 */
	public int getSelectedIndex() {
		return this.selectionModel.getAnchorSelectionIndex();
	}
	
	/* (non-Javadoc)
	 * @see main.Controller#addFile(main.BufferedDDSImage)
	 */
	public void addFile(File file) {
		try {

			if(!DDSFile.isValidDDSImage(file)) throw new IOException("DDSImage could not be read.");

			DDSImageFile  ddsfile = new DDSImageFile(file);
			if(!this.contains(ddsfile)) {

				if (ddsfile.getTextureType() == DDSFile.TextureType.CUBEMAP ||
					ddsfile.getTextureType() == DDSFile.TextureType.VOLUME) {
					JOptionPane.showMessageDialog(null, 
							"<html>Error: This programm doesn't support cubemaps or volume textures." +
							"<br>"+ddsfile.getFile().getName()+" can not be loaded.</html>",	"Error", 
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} 


				this.add(ddsfile);

		} else {
			// file already in list
			JOptionPane.showMessageDialog(null, 
					"<html>The file is already added.</html>",	"Information", 
					JOptionPane.INFORMATION_MESSAGE);
		}		
		
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					"<html>Error: File "+ file.getName()+" <br> "+e.getMessage() + "</html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}
	

	
	/**
	 * @param files
	 */
	public void addFiles(File[] files) {
		for (int i = 0; i < files.length; i++) {
			addFile(files[i]);
		}
	}
	
	/**
	 * @return
	 */
	public Object getSelectedItem() {
		int selectedIndex = this.selectionModel.getAnchorSelectionIndex();
		return  this.getElementAt(selectedIndex);
	}

	/**
	 * Removes the selected elements from the list
	 * @return
	 */
	public Object removeSelectedItem() {
		int selectedIndex = this.getSelectedIndex();
		Object object = this.remove(selectedIndex);
		
		if (selectedIndex-1 >= 0)  
			this.setSelectionIndex(selectedIndex-1);
		else 
			this.setSelectionIndex(selectedIndex+1); 
		return object;
	}

	/**
	 * Selects one element at index i
	 * @param i
	 */
	private void setSelectionIndex(int i) {
		if (this.getSize() <= i && i >= 0) {
			selectionModel.addSelectionInterval(i, i);
		}
		
	}

	/**
	 * 
	 */
	public boolean isSelectionEmpty() {
		return this.selectionModel.isSelectionEmpty();
	}
	
}
