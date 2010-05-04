package de.danielsenff.radds.models;

import java.io.File;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FileNode { 
	private static final NumberFormat FORMATTER = new DecimalFormat("#,##0"); 
	private final File file; 

	public FileNode(File file) {
		this.file = file;
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
}