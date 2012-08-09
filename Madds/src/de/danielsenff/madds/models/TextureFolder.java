package de.danielsenff.madds.models;

import java.io.File;

public class TextureFolder implements Sizable {

	File folder;
	long sumSize;
	
	public TextureFolder(File folder) {
		this.folder = folder;
	}
	
	@Override
	public void addSize(long diff) {
		this.sumSize += diff;
	}
	
	@Override
	public void setSize(long size) {
		this.sumSize = size;
	}
	
	@Override
	public long getSize() {
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
