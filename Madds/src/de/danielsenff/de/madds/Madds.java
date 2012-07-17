package de.danielsenff.de.madds;

import java.awt.Dimension;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

import net.bouthier.treemapSwing.TMView;
import net.bouthier.treemapSwing.TreeMap;
import net.bouthier.treemapSwing.fileViewer.TMFileModelSize;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.ciscavate.cjwizard.PageFactory;
import org.ciscavate.cjwizard.WizardContainer;
import org.ciscavate.cjwizard.WizardListener;
import org.ciscavate.cjwizard.WizardPage;
import org.ciscavate.cjwizard.WizardSettings;

import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.TMTextureModelDraw;
import de.danielsenff.de.madds.models.TMTextureNode;
import de.danielsenff.de.madds.view.WizardFactory;

public class Madds {

	private static Log log = LogFactory.getLog(Madds.class);
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new Madds().start();
	}

	public Madds() {}

	public void start() {
		
		// create the WizardContainer:
		final PageFactory pageFactory = new WizardFactory();
		final WizardContainer wizard = new WizardContainer(pageFactory);
		
		// stick the WizardContainer into a dialog:
		final JDialog dialog = new JDialog();
		wizard.addWizardListener(new WizardListener(){

			@Override
			public void onCanceled(List<WizardPage> path, WizardSettings settings) {
				log.debug("settings: "+wizard.getSettings());
				dialog.dispose();
			}

			@Override
			public void onFinished(List<WizardPage> path, WizardSettings settings) {
				log.debug("settings: "+wizard.getSettings());
				dialog.dispose();
				
				
//				File rootDirectory = new File((String) settings.get("rootDirectory"));
//				
//				if(rootDirectory != null && rootDirectory.exists()) {
//					Inventorizer inventorizer = new Inventorizer(rootDirectory, ".dds");
//					inventorizer.startInventoring(rootDirectory);
////					displaySizableNode(graph.getFirst());
//
//					View view = showTMApp(inventorizer, rootDirectory);
//				MaddsView maddsView = new MaddsView(inventorizer, view);
//				}
			}

			@Override
			public void onPageChanged(WizardPage newPage, List<WizardPage> path) {
				log.debug("settings: "+wizard.getSettings());
				// Set the dialog title to match the description of the new page:
				dialog.setTitle("Madds - "+newPage.getDescription());
			}
		});

		dialog.getContentPane().add(wizard);
		dialog.setPreferredSize(new Dimension(700, 500));
		dialog.pack();
		dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		dialog.setVisible(true);
		
	}
	
	public static TMView showTMApp(Inventorizer inventorizer, File rootFile) throws IllegalAccessException {
		
		TMTextureNode model = new TMTextureNode(rootFile);
		if (model == null) {
			throw new IllegalAccessException("Error : can't start treemap from "
					+ rootFile.getAbsolutePath());
		}

		TreeMap treeMap = new TreeMap(model);
		String name = rootFile.getAbsolutePath();

		TMFileModelSize fSize = new TMFileModelSize();
		TMTextureModelDraw fDraw = new TMTextureModelDraw();
		return treeMap.getView(fSize, fDraw);
	}

//	private  void displaySizableNode(Node<Sizable> node) {
//		Sizable file = node.getData();
//		String message = file.getFileName() +": " +ByteConverter.bit2MibiByte(file.getSize())+ " MibiByte";
//
//		Logger.getLogger(getClass()).log(message);
//
//		for(Node<Sizable> neighbour : node.getNeighbours() ) {
//			Logger.getLogger(getClass()).log(neighbour);
//			//			displaySizableNode(neighbour);
//		}
//	}




	public static File openFile(String startFolder) {

		final JFileChooser fc = new JFileChooser(startFolder);
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
