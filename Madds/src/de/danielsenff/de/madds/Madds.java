package de.danielsenff.de.madds;

import java.io.File;

import javax.swing.JFileChooser;

import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.SizableNode;
import de.danielsenff.de.madds.models.Tree;
import de.danielsenff.de.madds.util.ByteConverter;
import de.danielsenff.de.madds.util.Logger;

public class Madds {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Madds().start();
	}

	public Madds() {}

	public void start() {
		File rootDirectory = openFile();
		if(rootDirectory != null) {
			Logger.getLogger(getClass()).log(rootDirectory);

			Inventorizer inventorizer = new Inventorizer(rootDirectory, ".dds");
			inventorizer.startInventoring(rootDirectory);
			Tree<SizableNode> tree = inventorizer.getFileSizeTree();
			displaySizableNode(tree, tree.getRoot());
		}	
	}

	private  void displaySizableNode(Tree<SizableNode> tree, SizableNode node) {
		String message = node.getFileName() +": " +ByteConverter.bit2MibiByte(node.getSize())+ " MibiByte";

		Logger.getLogger(getClass()).log(message);
		
		for (SizableNode child : tree.getSuccessors(node)) {
			displaySizableNode(tree, child);
		}
	}




	private static File openFile() {

		final JFileChooser fc = new JFileChooser();
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		//		fc.setFileFilter(new FileNameExtensionFilter("DirectDrawSurface", "DDS"));

		final int option = fc.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			File file = fc.getSelectedFile();
			return file;
		}
		return null;
	}
}
