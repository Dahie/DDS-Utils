/**
 * 
 */
package de.danielsenff.badds.controller;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import de.danielsenff.badds.util.OS;


/**
 * @author danielsenff
 *
 */
public class Badds {

	public static void main(String[] args) {
		
		
		// some System dependant things 
		if (OS.isMacOS()) {
			System.setProperty("apple.laf.useScreenMenuBar", "true"); 
			System.setProperty("apple.awt.brushMetalRounded", "true");
//			System.setProperty("apple.awt.fileDialogForDirectories", "true");
		} else if (OS.isWindows()) {
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
