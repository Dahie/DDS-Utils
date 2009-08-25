/**
 * 
 */
package de.danielsenff.badds.util;

/**
 * @author danielsenff
 *
 */
public class OS {

	public static boolean isMacOS() {
		return System.getProperty("os.name").toLowerCase().indexOf("mac") != -1;
	}
	
	public static boolean isWindows() {
		return System.getProperty("os.name").toLowerCase().indexOf("windows") != -1;
	}
}
