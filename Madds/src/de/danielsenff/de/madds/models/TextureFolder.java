package de.danielsenff.de.madds.models;

import java.io.File;

public class TextureFolder implements SizableNode {

	File folder;
	int sumSize;
	
	public TextureFolder(File folder) {
		this.folder = folder;
	}
	
	public void addSize(int diff) {
		this.sumSize += diff;
	}
	
	public void setSize(int size) {
		this.sumSize = size;
	}
	
	@Override
	public int getSize() {
		return this.sumSize;
	}

	@Override
	public File getFile() {
		return this.folder;
	}

	@Override
	public String getFileName() {
		return this.folder.getName();
	}

}
