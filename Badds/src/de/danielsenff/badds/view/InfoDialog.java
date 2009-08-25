package de.danielsenff.badds.view;

import java.awt.Dimension;
import java.util.ResourceBundle;

import javax.swing.JDialog;
import javax.swing.JLabel;

import de.danielsenff.badds.controller.Application;

import Model.DDSFile;

public class InfoDialog extends JDialog {

	public InfoDialog(DDSFile dds) {
		
		setSize(new Dimension(200,100));
		
		ResourceBundle bundle = Application.getBundle();
		
		JLabel lblFilename = new JLabel(bundle.getString("File"));
		JLabel lblWildCard = new JLabel(bundle.getString("Wildcard"));
		JLabel lblFormat = new JLabel(bundle.getString("Format"));
		JLabel lblWidth = new JLabel(bundle.getString("Width"));
		JLabel lblHeight = new JLabel(bundle.getString("Height"));
		JLabel lblMipmaps = new JLabel(bundle.getString("MipMaps"));
		

		
		
		
		
		pack();
		setVisible(true);
		
	}
	
}
