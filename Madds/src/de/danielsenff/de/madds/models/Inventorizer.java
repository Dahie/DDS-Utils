package de.danielsenff.de.madds.models;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import de.master.core.graph.base.Graph;
import de.master.core.graph.base.Node;


public class Inventorizer {

	private File rootDirectory;
	private FileFilter filter;
	private Graph<Sizable> fileTree;
	private Vector<TextureFile> textureFiles;
	
	public Inventorizer(File rootDirectory, final String extension) {
		this.rootDirectory = rootDirectory;
		this.filter = new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() || pathname.getName().toLowerCase().endsWith(extension);
			}
		};
		this.fileTree = new Graph<>();
		this.textureFiles = new Vector<>();
	}
	
	public void makeInventoryForDir(Node<Sizable> parentNode, File[] files) {
		for (int i = 0; i < files.length; i++) {
			File file = files[i];
			
			if(file.isDirectory()) {
				TextureFolder tfolder = new TextureFolder(file);
				Node folderNode = new Node((Sizable)tfolder);
				fileTree.insertAt(0.0, parentNode, folderNode);
				makeInventoryForDir(folderNode, file.listFiles(filter));
				parentNode.getData().addSize(tfolder.getSize());
			} else {
				// add file to tree
				try {
					TextureFile tfile = TextureFile.read(file);
					fileTree.insertAt(0.0, parentNode, new Node<>((Sizable)tfile));
					this.textureFiles.add(tfile);
					System.out.println(file.getName() + "   " + tfile.getSize());
					parentNode.getData().addSize(tfile.getSize());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void startInventoring(File rootDirectory) {
		TextureFolder tfolder = new TextureFolder(rootDirectory);
		Node rootFolderNode = new Node(tfolder);
		fileTree.insert(0, rootFolderNode);
		makeInventoryForDir(rootFolderNode, rootDirectory.listFiles(filter));
	}

	public Graph<Sizable> getFileSizeTree() {
		return this.fileTree;
	}
	
	public Collection<TextureFile> getTextureFiles() {
		return this.textureFiles;
	}
}
