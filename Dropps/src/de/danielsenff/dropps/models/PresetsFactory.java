/**
 * 
 */
package de.danielsenff.dropps.models;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import de.danielsenff.dropps.Dropps;

/**
 * @author danielsenff
 *
 */
public class PresetsFactory {

	private static final String PRESETS_FILE = "presets.csv";
	private static final String resourcesDir = "/de/danielsenff/dropps/resources/";

	
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
	 * @return 
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
	 * Either gets the InputStream of the in-jar presets
	 * @return 
	 */
	private static InputStream getResourceStream() {
		return Dropps.class.getResourceAsStream(resourcesDir + PRESETS_FILE);
	}
	
}
