package de.danielsenff.de.madds;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import de.danielsenff.de.madds.models.Inventorizer;

public class Madds {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File rootDirectory = openFile();
		
		Inventorizer inventorizer = new Inventorizer(rootDirectory, ".dds");
		inventorizer.startInventoring(rootDirectory);
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
