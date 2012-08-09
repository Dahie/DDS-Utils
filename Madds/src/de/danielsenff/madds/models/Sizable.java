package de.danielsenff.madds.models;

import java.io.File;

public interface Sizable {

	public File getFile();
	public String getFileName();
	public long getSize();
	public void setSize(long size);
	public void addSize(long diff);
}
