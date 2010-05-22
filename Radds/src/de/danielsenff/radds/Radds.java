/**
 * 
 */
package de.danielsenff.radds;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.danielsenff.radds.controller.Application;

/**
 * @author danielsenff
 *
 */
public class Radds {
	public static void main(String[] args) {

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
		new Application();
	}
}
