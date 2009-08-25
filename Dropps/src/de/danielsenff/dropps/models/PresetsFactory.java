/**
 * 
 */
package de.danielsenff.dropps.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import sun.rmi.rmic.newrmic.Resources;

import de.danielsenff.dropps.Dropps;
import de.danielsenff.dropps.util.ResourceLoader;

/**
 * @author danielsenff
 *
 */
public class PresetsFactory {

	private static final String PRESETS_FILE = "presets.csv";
	static File resourcesDir = new File(Dropps.getInstance().getContext().getResourceMap().getResourcesDir());

	
	/**
	 * Returns a {@link PresetsComboModel} based on a File
	 * @param file
	 * @return
	 */
	public static PresetsComboModel getInstanceFromFile(File file) {
		PresetsComboModel model = null;
		try {
			model = new PresetsComboModel(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return model;
	}
	
	/**
	 * Returns the {@link PresetsComboModel} from a default local file
	 */
	public static PresetsComboModel getInstanceFromDefaultLocalFile() {
		return new PresetsComboModel(getResourceStream());
	}
	
	
	/**
	 * Returns the default {@link PresetsComboModel}
	 * @return
	 */
	public static PresetsComboModel getDefaultInstance() {
		return new PresetsComboModel();
	}
	
	
	/**
	 * Either gets the Inputstream of the in-jar presets or of the local file in the directory
	 * @param presets_file2
	 * @return 
	 */
	private static InputStream getResourceStream() {
		InputStream fis = null;
		final File localPresetsFile = new File(resourcesDir + File.separator+ PRESETS_FILE);
		
		Dropps.getInstance().getContext().getResourceMap();
		
		/*try {
			if ( localPresetsFile.exists() ) {
				// read from local directory
				fis = new FileInputStream(localPresetsFile);
//			} else {
				// read from jar
				 */
				fis = Dropps.class.getResourceAsStream(File.separator+localPresetsFile);
		/*	 }
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}*/
		
		return fis;
	}
	
}
