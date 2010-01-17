/**
 * 
 */
package de.danielsenff.radds.util;

import java.io.InputStream;
import java.net.URL;

import javax.swing.ImageIcon;


/**
 * @author danielsenff
 *
 */
public class ResourceLoader {

	/**
	 * 
	 */
	private ResourceLoader() {	}
	
	
	public static URL getResource(String file) {
		return Class.class.getResource(file);
	}

	public static InputStream getResourceAsStream(String file) {
		return Class.class.getResourceAsStream(file);
	}

	public static ImageIcon getResourceIcon(String file){
		return new ImageIcon(Class.class.getResource(file));
	}
	
	

}
