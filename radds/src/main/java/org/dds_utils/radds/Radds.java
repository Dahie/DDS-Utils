/**
 * 
 */
package org.dds_utils.radds;

import javax.swing.UIManager;

import com.formdev.flatlaf.FlatLightLaf;
import org.dds_utils.dropps.models.ExportOptions;
import org.jogl.DDSImage;

import javax.swing.*;

/**
 * Review a DDS application
 * @author danielsenff
 *
 */
public class Radds {

	/**
	 * Main method launching the application.
	 */
	public static void main(final String[] args) {
		FlatLightLaf.setup();

		SwingUtilities.invokeLater(new RaddsView());

	}
}
