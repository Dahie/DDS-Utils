package de.danielsenff.radds.models;

import java.io.File;
import java.io.FileFilter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author danielsenff
 *
 */
public class FileNode { 
	private static final NumberFormat FORMATTER = new DecimalFormat("#,##0"); 
	private final File file; 
	private boolean isDirectory = false;
	private int childrenCount = 0;
	private final List<File> children;

	/**
	 * 
	 * @param file
	 * @param filter
	 */
	public FileNode(final File file, final FileFilter filter) {
		this.file = file;
		this.isDirectory = file.isDirectory();
		if(isDirectory) {
			this.children = Arrays.asList(file.listFiles(filter));
		} else {
			this.children = new ArrayList<File>();
		}
		this.childrenCount = children.size();
	}
	
	public File getFile() {
		return this.file;
	}
	
	@Override
	public int hashCode() { 
	    int hc = 17; 
	    int hashMultiplier = 59; 
	    hc = hc * hashMultiplier + this.toString().length(); 
	    hc = hc * hashMultiplier + this.toString().hashCode(); 
	    return hc; 
	}

	@Override
	public boolean equals(Object obj) {
		//System.out.println(obj.toString() + "  " + this.toString());
		//System.out.println(obj.toString().equals(this.toString()));
		return obj.toString().equals(this.toString());
	}
	
	@Override
	public String toString() {
		String name = this.file.getName(); 
		if (this.file.isDirectory() == false) {
			name += " [" + FORMATTER.format(this.file.length()) + " bytes]"; 
		}
		return name;
	}

	/**
	 * Returns the number of children ie files if this is a directory.
	 * For regular files this is always 0.
	 * @return
	 */
	public int getChildrenCount() {
		return this.childrenCount;
	}

	/**
	 * If it is a directory, it is a node. Otherwise if it is no directory, it is a leaf.
	 * @return
	 */
	public boolean isDirectory() {
		return this.isDirectory;
	}

	public List<File> getChildren() {
		return this.children;
	}

	public File getChild(int index) {
		return this.children.get(index);
	}

	public int getIndexOfChild(File file) {
		return isDirectory ? this.children.indexOf(file) : -1;
	}

	public boolean hasChildren() {
		return this.childrenCount > 0;
	}
}