package de.danielsenff.de.madds;

import java.io.File;

import javax.swing.JFileChooser;

import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.Sizable;
import de.danielsenff.de.madds.util.ByteConverter;
import de.danielsenff.de.madds.util.Logger;
import de.danielsenff.de.madds.view.MaddsView;
import de.master.core.graph.base.Graph;
import de.master.core.graph.base.Node;

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
			Graph<Sizable> graph = inventorizer.getFileSizeTree();
			displaySizableNode(graph.getFirst());
			
			
			new MaddsView(inventorizer);
		}
	}

	private  void displaySizableNode(Node<Sizable> node) {
		Sizable file = node.getData();
		String message = file.getFileName() +": " +ByteConverter.bit2MibiByte(file.getSize())+ " MibiByte";

		Logger.getLogger(getClass()).log(message);
		
		for(Node<Sizable> neighbour : node.getNeighbours() ) {
			Logger.getLogger(getClass()).log(neighbour);
//			displaySizableNode(neighbour);
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
