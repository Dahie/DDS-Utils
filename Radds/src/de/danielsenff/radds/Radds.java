/**
 * 
 */
package de.danielsenff.radds;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.jdesktop.application.SingleFrameApplication;

/**
 * Review a DDS application
 * @author danielsenff
 *
 */
public class Radds extends SingleFrameApplication {

	/**
	 * At startup create and show the main frame of the application.
	 */
	@Override protected void startup() {
		show(new RaddsView(this));
	}

	/**
	 * A convenient static getter for the application instance.
	 * @return the instance of DocumentEditorApp
	 */
	public static Radds getApplication() {
		return Radds.getInstance(Radds.class);
	}

	/**
	 * Main method launching the application.
	 */
	public static void main(final String[] args) {

		// some System dependant things
		String vers = System.getProperty("os.name").toLowerCase(); 
		if (vers.indexOf("mac") != -1) {
			System.setProperty("apple.laf.useScreenMenuBar", "true"); 
			System.setProperty("apple.awt.brushMetalRounded", "true");
		} else if (vers.indexOf("windows") != -1) {
			//Look and feel
			//			String lnfName = "javax.swing.plaf.metal.MetalLookAndFeel";
			//			String lnfName = "com.sun.java.swing.plaf.motif.MotifLookAndFeel";
			String lnfName = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
			try {
				UIManager.setLookAndFeel(lnfName);
			} catch (ClassNotFoundException e) {
			} catch (InstantiationException e) {
			} catch (IllegalAccessException e) {
			} catch (UnsupportedLookAndFeelException e) {
			}
		}

		launch(Radds.class, args);
	}
}
