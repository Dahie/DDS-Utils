/**
 * 
 */
package de.danielsenff.badds.model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;

import model.DDSFile;



/**
 * @author danielsenff
 *
 */
public class PresetsComboModel extends DefaultComboBoxModel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * Construct default empty ComboModel
	 */
	public PresetsComboModel() {
		
	}
	
	/**
	 * Construct PresetsComboModel from File
	 * @param file
	 * @throws FileNotFoundException 
	 */
	public PresetsComboModel(final File file) throws FileNotFoundException {
		this(new FileInputStream(file));
	}
	
	
	/**
	 * Construct PresetsComboModel from {@link FileInputStream}
	 * @param is 
	 */
	public PresetsComboModel(final InputStream is) {
		
		try {
			
			final InputStreamReader isr = new InputStreamReader(is);
			final BufferedReader br = new BufferedReader(isr);

			String line;
			while( ( line = br.readLine()) != null ) {
				final Preset preset = readPreset(line);	

				if(preset==null)
					break;

				this.addElement(preset);		
			}
		} catch (final FileNotFoundException e) {
			e.printStackTrace();
		} catch (final IOException e) {
			e.printStackTrace();
		}

			
	}


	private Preset readPreset(final String line) {
				
		final StringTokenizer tokenizer = new StringTokenizer(line,";");

		final String name 		= tokenizer.nextToken();
		final String pixelformat 	= tokenizer.nextToken();
		final int width 			= Integer.parseInt(tokenizer.nextToken());
		final int height 			= Integer.parseInt(tokenizer.nextToken());
		final boolean mipmaps 	= Boolean.parseBoolean(tokenizer.nextToken());
		
		
		if(tokenizer.hasMoreTokens()){
			JOptionPane.showMessageDialog(null, "The following file seems to be damaged:\n" +
					"presets.csv has faulty data", "Corrupt file", JOptionPane.ERROR_MESSAGE);
		}
		
		return new Preset(name, width, height, DDSFile.verbosePixelformat(pixelformat), mipmaps);
				
	}
	
}
