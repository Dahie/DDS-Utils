package de.danielsenff.de.madds;

import java.awt.BorderLayout;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import net.bouthier.treemapSwing.TMView;
import net.bouthier.treemapSwing.TreeMap;
import net.bouthier.treemapSwing.fileViewer.TMFileModelDraw;
import net.bouthier.treemapSwing.fileViewer.TMFileModelSize;
import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.Sizable;
import de.danielsenff.de.madds.models.TMTextureModelDraw;
import de.danielsenff.de.madds.models.TMTextureNode;
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
			Inventorizer inventorizer = new Inventorizer(rootDirectory, ".dds");
			inventorizer.startInventoring(rootDirectory);
//			displaySizableNode(graph.getFirst());

			showTMApp(inventorizer, rootDirectory);
		}
	}

	private void showTMApp(Inventorizer inventorizer, File rootFile) {
		TMTextureNode model = new TMTextureNode(rootFile);
		if (model == null) {
			System.err.println(
					"Error : can't start treemap from "
							+ rootFile.getAbsolutePath());
			return;
		}

		TreeMap treeMap = new TreeMap(model);
		String name = rootFile.getAbsolutePath();

		TMFileModelSize fSize = new TMFileModelSize();
		TMTextureModelDraw fDraw = new TMTextureModelDraw();
		TMView view = treeMap.getView(fSize, fDraw);
//		view.getAlgorithm().setCushion(true);
		view.setLayout(new BorderLayout());

		MaddsView maddsView = new MaddsView(inventorizer, view);
		
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
