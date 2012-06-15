package de.danielsenff.de.madds.models;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import de.danielsenff.de.madds.util.Logger;


public class Inventorizer {

	private File rootDirectory;
	private FileFilter filter;
	private Tree<SizableNode> fileTree;
	
	public Inventorizer(File rootDirectory, final String extension) {
		this.rootDirectory = rootDirectory;
		this.filter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(extension);
			}
		};
		this.fileTree = new Tree<>();
	}
	
	public void makeInventoryForDir(TextureFolder parentFolder, File[] files) {
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			if(file.isDirectory()) {
				TextureFolder tfolder = new TextureFolder(file);
				fileTree.insertNodeAt(parentFolder, tfolder);
				makeInventoryForDir(tfolder, file.listFiles(filter));
				parentFolder.addSize(tfolder.getSize());
			} else {
				// add file to tree
				try {
					TextureFile tfile = TextureFile.read(file);
					fileTree.insertNodeAt(parentFolder, tfile);
					System.out.println(file.getName() + "   " + tfile.getSize());
					parentFolder.addSize(tfile.getSize());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void startInventoring(File rootDirectory) {
		TextureFolder tfolder = new TextureFolder(rootDirectory);
		fileTree.insertNode(tfolder);
		makeInventoryForDir(tfolder, rootDirectory.listFiles(filter));
	}

	public Tree<SizableNode> getFileSizeTree() {
		return this.fileTree;
	}
}
