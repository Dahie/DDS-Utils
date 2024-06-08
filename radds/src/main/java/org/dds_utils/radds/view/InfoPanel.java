/**
 * 
 */
package org.dds_utils.radds.view;

import java.awt.GridBagLayout;
import java.awt.image.BufferedImage;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.model.TextureImage;

import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import org.dds_utils.radds.Radds;
import org.dds_utils.radds.RaddsView;
import org.dds_utils.radds.view.GridBagConstraints.LabelConstraints;
import org.dds_utils.radds.view.GridBagConstraints.RemainderConstraints;


/**
 * Information panel for displaying image details.
 * @author danielsenff
 *
 */
public class InfoPanel extends JPanel {

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
	public InfoPanel() {
		init();
	}
	
	private ResourceMap getResourceMap() {
		final Radds instance = Radds.getInstance(Radds.class);
		final ApplicationContext context = instance.getContext();
		final org.jdesktop.application.ResourceMap resourceMap = context.getResourceMap(RaddsView.class);
		return resourceMap;
	}
	
	private void init() {
		
		this.lblNumMipMaps = new JLabel(getResourceMap().getString("MipMaps")+":");
		this.lblWidth = new JLabel(getResourceMap().getString("Width")+":");
		this.lblHeight = new JLabel(getResourceMap().getString("Height")+":");
		this.lblPixelformat = new JLabel(getResourceMap().getString("Pixelformat")+":");
		this.lblType = new JLabel(getResourceMap().getString("Type")+":");
		
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
	private void setData(final int numMipMaps, final int width, final int height, final String pixelformat, final String type) {
		this.numMipMaps.setText(String.valueOf(numMipMaps));
		this.width.setText(String.valueOf(width));
		this.height.setText(String.valueOf(height));
		this.pixelformat.setText(pixelformat);
		this.type.setText(type);
		this.revalidate();
	}
	
	/**
	 * Set the {@link TextureImage} whose information are to be displayed.
	 * @param texture
	 */
	public void setTextureFile(final TextureImage texture) {
		System.out.println(texture.getPixelformat());
		this.setData(texture.getNumMipMaps(), 
				texture.getWidth(), 
				texture.getHeight(),
				texture.getPixelformatVerbose(), 
				texture.getTextureType().toString());	
	}
	
	/**
	 * Set the {@link BufferedImage} whose information are to be displayed.
	 * @param texture
	 */
	public void setTextureFile(final BufferedImage texture) {
		this.setData(1, 
				texture.getWidth(), 
				texture.getHeight(),
                String.valueOf(texture.getType()),
				"-");	
	}
	
}
