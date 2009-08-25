/**
 * 
 */
package de.danielsenff.badds.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.danielsenff.badds.util.ResourceLoader;


/**
 * @author danielsenff
 *
 */
public class PresetsFactory {

	private static final String PRESETS_FILE = "presets.csv"; // filename in jar root
	

	
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
		PresetsComboModel model = new PresetsComboModel(getResourceStream());
		return model;
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
		final File localPresetsFile = new File(PRESETS_FILE);
		
		try {
			if ( localPresetsFile.exists() ) {
				// read from local directory
				fis = new FileInputStream(localPresetsFile.getAbsolutePath());
			} else {
				// read from jar
				fis = ResourceLoader.getResourceAsStream("/"+PRESETS_FILE);
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return fis;
	}
	
}
