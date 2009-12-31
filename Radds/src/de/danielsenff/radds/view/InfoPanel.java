/**
 * 
 */
package de.danielsenff.radds.view;

import java.awt.Font;
import java.awt.GridBagLayout;

import javax.swing.JLabel;

import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.view.GridBagConstraints.LabelConstraints;
import de.danielsenff.radds.view.GridBagConstraints.RemainderConstraints;

import Model.DDSFile;


/**
 * @author danielsenff
 *
 */
public class InfoPanel extends JCPanel {

	private JLabel lblNumMipMaps ;
	private JLabel numMipMaps;
	private JLabel lblWidth;
	private JLabel width;
	private JLabel lblHeight;
	private JLabel height;
	private JLabel lblPixelformat;
	private JLabel pixelformat;
	private JLabel lblType;
	private JLabel type;
	
	
	
	/**
	 * 
	 */
	public InfoPanel(Application controller) {
		super(controller);
		init();
	}
	
	
	private void init() {
		
		this.lblNumMipMaps = new JLabel(bundle.getString("MipMaps")+":");
		this.lblWidth = new JLabel(bundle.getString("Width")+":");
		this.lblHeight = new JLabel(bundle.getString("Height")+":");
		this.lblPixelformat = new JLabel(bundle.getString("Pixelformat")+":");
		this.lblType = new JLabel(bundle.getString("Type")+":");
		
		this.numMipMaps = new JLabel("-");
		this.width = new JLabel("-");
		this.height = new JLabel("-");
		this.pixelformat = new JLabel("-");
		this.type = new JLabel("-");
		
		
		this.setLayout(new GridBagLayout());
		this.add(lblNumMipMaps, new LabelConstraints());
		this.add(numMipMaps, new RemainderConstraints());
		this.add(lblWidth, new LabelConstraints());
		this.add(width, new RemainderConstraints());
		this.add(lblHeight, new LabelConstraints());
		this.add(height, new RemainderConstraints());
		this.add(lblPixelformat, new LabelConstraints());
		this.add(pixelformat, new RemainderConstraints());
		this.add(lblType, new LabelConstraints());
		this.add(type, new RemainderConstraints());
	}
	
	/**
	 * 
	 */
	private void setData(int numMipMaps, int width, int height, String pixelformat, String type) {
		this.numMipMaps.setText(numMipMaps+"");
		this.width.setText(width+"");
		this.height.setText(height+"");
		this.pixelformat.setText(pixelformat);
		this.type.setText(type);
		this.revalidate();
	}
	
	public void setTextureFile(DDSFile ddsfile) {
		this.setData(ddsfile.getNumMipMaps(), 
				ddsfile.getWidth(), 
				ddsfile.getHeight(),
				ddsfile.getPixelformatVerbose(), 
				ddsfile.getTextureType().toString());
	}
	
}
