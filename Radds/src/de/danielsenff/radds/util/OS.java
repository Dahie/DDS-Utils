/**
 * 
 */
package de.danielsenff.radds.util;

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


	public static void openURLinDefaultBrowser(String url) {
		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

		if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {

			System.err.println( "Desktop doesn't support the browse action (fatal)" );
			System.exit( 1 );
		}


		try {

			java.net.URI uri = new java.net.URI( url );
			desktop.browse( uri );
		}
		catch ( Exception e ) {

			System.err.println( e.getMessage() );
		}
	}

}
