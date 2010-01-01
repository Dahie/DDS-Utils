/**
 * 
 */
package de.danielsenff.badds.model;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JOptionPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

import util.FileUtil;
import Model.DDSFile;
import Model.TextureImage.TextureType;
import de.danielsenff.badds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class FilesListModel<E> extends AbstractTableModel implements Collection {

	// List with all open files
	private Vector<E> openFilesList;
	private DefaultListSelectionModel selectionModel;
	
	/**
	 * 
	 */
	public FilesListModel() {
		this.openFilesList = new Vector<E>();
		this.selectionModel = new DefaultListSelectionModel();
	}
	
	public FilesListModel(File file) {
		this();
		addFile(file);
	}
	
	public FilesListModel(File[] files) {
		this();
		addFiles(files);
	}
	
	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getColumnCount()
	 */
	public int getColumnCount() {
		return 6;
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getRowCount()
	 */
	public int getRowCount() {
		return this.size();
	}

	/* (non-Javadoc)
	 * @see javax.swing.table.TableModel#getValueAt(int, int)
	 */
	public Object getValueAt(int rowIndex, int columnIndex) {
		DDSFile item = (DDSFile) this.openFilesList.get(rowIndex);
		
		switch (columnIndex){
			 
			//Ausgabe von Start und Zielort sowie der benutzten Strasse
			case 0: return item.getFile().getName();
			case 1: return getWildcard(item.getFile().getName());
			case 2: return item.getPixelformatVerbose();
			case 3: return item.getWidth();
			case 4:	return item.getHeight();
			case 5:	return item.getNumMipMaps();
		}
		
		return null;
	}

	/**
	 * @param name
	 * @return
	 */
	private Object getWildcard(String name) {
		
		String lowerCaseName = name.toLowerCase();
		if(lowerCaseName.contains("extra0")) {
			return "Car";
		} else if (lowerCaseName.contains("extra2")) {
			return "Helmet";
		} else if (lowerCaseName.contains("extra1")) {
			return "Car tech";
		} else if (lowerCaseName.contains("driver")) {
			return "Driverbody";
		} else if (lowerCaseName.contains("wing")) {
			return "Car top";
		}
		
		return "Texture";
	}

	@Override
	public String getColumnName(int column){
			ResourceBundle bundle = Application.getBundle();
			switch (column){ 
				case 0: return bundle.getString("File");
				case 1: return bundle.getString("Wildcard");
				case 2: return bundle.getString("Format");
				case 3: return bundle.getString("Width");
				case 4: return bundle.getString("Height");
				case 5: return bundle.getString("MipMaps");
			}
			
			return "";		
		}
	
	
	public Object getRowItem(final int index) {
		return this.openFilesList.get(index);
	}
	
	public Vector<E> getOpenFilesList() {
		return this.openFilesList;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#add(java.lang.Object)
	 */
	public boolean add(Object arg0) {
		boolean add = this.openFilesList.add((E) arg0);
		this.fireTableDataChanged();
		return add;
	}

	public void addFile(File file) {
		
		if(!FileUtil.getFileSuffix(file).contains("dds") || file.isDirectory())
			return;
		try {
			if(!DDSFile.isValidDDSImage(file)) throw new IOException("DDSImage could not be read.");
			DDSFile ddsfile = new DDSFile(file);
			if(!this.contains(ddsfile)) {
				if(ddsfile.getTextureType() == TextureType.CUBEMAP ||
						ddsfile.getTextureType() == TextureType.VOLUME) {
					JOptionPane.showMessageDialog(null, 
							"<html>Error: This programm doesn't support cubemaps or volume textures." +
							"<br>"+ddsfile.getFile().getName()+" can not be loaded.</html>",	"Error", 
							JOptionPane.INFORMATION_MESSAGE);
					return;
				} 
				this.add(ddsfile);
			} else {
				JOptionPane.showMessageDialog(null, 
						"<html>The file is already added.</html>",	"Information", 
						JOptionPane.INFORMATION_MESSAGE);
			}

		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, 
					"<html>Error: File "+ file.getName()+" <br> "+e.getMessage() + "</html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
			return;
		}
	}
	
	public void addFiles(File[] files) {
		for (int i = 0; i < files.length; i++) {
			System.out.println(files[i]);
			addFile(files[i]);
		}
	}
	
	/* (non-Javadoc)
	 * @see java.util.Collection#addAll(java.util.Collection)
	 */
	public boolean addAll(Collection arg0) {
		boolean addAll = this.openFilesList.addAll(arg0);
		this.fireTableDataChanged();
		return addAll;
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#clear()
	 */
	public void clear() {
		this.openFilesList.clear();
		this.fireTableDataChanged();
		
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#contains(java.lang.Object)
	 */
	public boolean contains(Object item) {
		return this.openFilesList.contains(item);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#containsAll(java.util.Collection)
	 */
	public boolean containsAll(Collection arg0) {
		return this.openFilesList.containsAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#isEmpty()
	 */
	public boolean isEmpty() {
		return this.openFilesList.isEmpty();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#iterator()
	 */
	public Iterator<E> iterator() {
		return this.openFilesList.iterator();
	}

	
	/**
	 * 
	 */
	public Object remove(final int index) {
		
		E remove = this.openFilesList.remove(index);
		this.fireTableDataChanged();
		return remove;
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.util.Collection#remove(java.lang.Object)
	 */
	public boolean remove(Object arg0) {
		return this.openFilesList.remove(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#removeAll(java.util.Collection)
	 */
	public boolean removeAll(Collection arg0) {
		return this.openFilesList.removeAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#retainAll(java.util.Collection)
	 */
	public boolean retainAll(Collection arg0) {
		return this.openFilesList.retainAll(arg0);
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#size()
	 */
	public int size() {
		return this.openFilesList.size();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray()
	 */
	public Object[] toArray() {
		return this.openFilesList.toArray();
	}

	/* (non-Javadoc)
	 * @see java.util.Collection#toArray(T[])
	 */
	public Object[] toArray(Object[] arg0) {
		return this.openFilesList.toArray(arg0);
	}

	public ListSelectionModel getSelectionModel() {
		return this.selectionModel;
	}

	
}
