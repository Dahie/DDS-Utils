package de.danielsenff.de.madds.models;

import java.io.File;
import java.io.FileFilter;

public class TextureNodeFileFilter implements FileFilter {

	private String extension;
	
	public TextureNodeFileFilter(String extension) {
		this.extension = extension;
	}

	@Override
	public boolean accept(File pathname) {
		return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(extension);
	}

}
