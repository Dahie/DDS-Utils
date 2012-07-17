package de.danielsenff.de.madds.models;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Vector;

import de.master.core.graph.base.Graph;
import de.master.core.graph.base.Node;


public class Inventorizer {

	private FileFilter filter;
	private Graph<Sizable> fileTree;
	private HashMap<File, TextureFile> textureFiles;
	
	public Inventorizer(final String extension) {
		this.filter = new TextureNodeFileFilter(extension);
		this.fileTree = new Graph<>();
		this.textureFiles = TextureHashMap.getTextureHashMap();
	}
	
	public void makeInventoryForDir(Node<Sizable> parentNode, File[] files) {
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			if(file.isDirectory()) {
				TextureFolder tfolder = new TextureFolder(file);
				Node<Sizable> folderNode = new Node((Sizable)tfolder);
				fileTree.insertAt(0.0, parentNode, folderNode);
				makeInventoryForDir(folderNode, file.listFiles(filter));
				parentNode.getData().addSize(tfolder.getSize());
			} else {
				// add file to tree
				try {
					TextureFile tfile = TextureFile.read(file);
					fileTree.insertAt(0.0, parentNode, new Node<>((Sizable)tfile));
					this.textureFiles.put(file, tfile);
					parentNode.getData().addSize(tfile.getSize());
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void startInventoring(File rootDirectory) {
		TextureFolder tfolder = new TextureFolder(rootDirectory);
		Node<Sizable> rootFolderNode = new Node(tfolder);
		fileTree.insert(0, rootFolderNode);
		makeInventoryForDir(rootFolderNode, rootDirectory.listFiles(filter));
	}

	public Graph<Sizable> getFileSizeTree() {
		return this.fileTree;
	}
	
	public HashMap<File, TextureFile> getTextureFiles() {
		return this.textureFiles;
	}
}
