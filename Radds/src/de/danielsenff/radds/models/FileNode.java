package de.danielsenff.radds.models;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

class FileNode { 
	private static final NumberFormat FORMATTER = new DecimalFormat("#,##0"); 
	private final File file; 

	public FileNode(File file) {
		this.file = file;
	}
	
	public File getFile() {
		return this.file;
	}
	
	@Override
	public String toString() {
		String name = this.file.getName(); 
		if (this.file.isDirectory() == false) {
			name += " [" + FORMATTER.format(this.file.length()) + " bytes]"; 
		}
		return name;
	}
}