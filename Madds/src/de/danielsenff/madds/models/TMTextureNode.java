package de.danielsenff.madds.models;

import java.io.File;
import java.io.FileFilter;
import java.util.Enumeration;
import java.util.Vector;

import net.bouthier.treemapSwing.TMModelNode;
import net.bouthier.treemapSwing.TMModelUpdater;
import de.master.core.graph.base.Node;

public class TMTextureNode extends Node<Sizable> implements TMModelNode {

	private TMModelUpdater updater = null; // the updater for this node
	private File 		   root    = null; // root of the tree
	private FileFilter 		fileFilter;
	
	public TMTextureNode(File root) {
        this.root = root;
		this.fileFilter = new TextureNodeFileFilter(".dds");
	}

	@Override
	public Enumeration children(Object node) {
		Vector children = new Vector();
		if (node instanceof File) {
			File file = (File) node;
			if (file.isDirectory()) {
				File[] tabFichiers = file.listFiles(fileFilter);
				for (int i = 0; i < tabFichiers.length; i++) {
					//File fichier = new File(file.getPath() + File.separator + tabFichiers[i]);
					children.add(tabFichiers[i]);
				}
			}
		}
		return children.elements();
	}

	@Override
	public Object getRoot() {
		return this.root;
	}

	@Override
	public boolean isLeaf(Object node) {
		if (node instanceof File) {
			File file = (File) node;
			return (!file.isDirectory());
		}
		return false;
	}

	@Override
	public void setUpdater(TMModelUpdater updater) {
		this.updater = updater;
	}

}
