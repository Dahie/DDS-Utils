package de.danielsenff.de.madds.models;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;


public class Inventorizer {

	private File rootDirectory;
	private FileFilter filter;
	
	public Inventorizer(File rootDirectory, final String extension) {
		this.rootDirectory = rootDirectory;
		this.filter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().endsWith(extension);
			}
		};
	}
	
	public void makeInventoryForDir(TextureFolder parentFolder, File[] files) {
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			// add file to tree
			try {
				TextureFile tfile = TextureFile.read(file);
				parentFolder.addSize(tfile.getSize());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			if(file.isDirectory()) {
				TextureFolder tfolder = new TextureFolder(file);
				
				makeInventoryForDir(tfolder, file.listFiles(filter));
			}
		}
	}

	public void startInventoring(File rootDirectory) {
		TextureFolder tfolder = new TextureFolder(rootDirectory);
		makeInventoryForDir(tfolder, rootDirectory.listFiles(filter));
	}
}
