/**
 * 
 */
package de.danielsenff.badds.operations;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;

/**
 * @author danielsenff
 *
 */
public abstract class ManipulateChannel implements Operation, RGBAFilter {

	
	/* (non-Javadoc)
	 * @see operations.Operation#run(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage run(BufferedImage bi) {
		
		WritableRaster raster = bi.getRaster();
		for (int y = 0; y < bi.getHeight(); y++) {
			for (int x = 0; x < bi.getWidth(); x++) {
				// read pixel
				int[] pixel = null;
				pixel = raster.getPixel(x, y, pixel);
				
				//run operation
				pixel = this.filterRGB(x, y, pixel);
				
				// write pixel back
				raster.setPixel(x, y, pixel );
			}
		}
		return bi;
	}

	

	
}
