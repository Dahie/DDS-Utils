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
public class FlipCanvas implements Operation {

	public enum Orientation {
		vertical, horizontal, both
	}
	
	protected Orientation orientation;
	
	/**
	 * 
	 */
	public FlipCanvas(Orientation orientation) {
		this.orientation = orientation;
	}
	
	/* (non-Javadoc)
	 * @see Operations.Operation#run(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage run(BufferedImage bi) {

		WritableRaster raster = bi.getRaster();
		
		// horizonal flip
		int height = raster.getHeight();
		int width = raster.getWidth();
		switch (orientation) {
		case horizontal:
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int[] pixel = raster.getPixel(x, y, new int[4]);
					raster.setPixel(width-x, y, pixel);
				}
			}	
			break;
		case vertical:
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int[] pixel = raster.getPixel(x, y, new int[4]);
					raster.setPixel(x, height-y, pixel);
				}
			}
			break;
		case both:
			for (int y = 0; y < height; y++) {
				for (int x = 0; x < width; x++) {
					int[] pixel = raster.getPixel(x, y, new int[4]);
					raster.setPixel(width-x, height-y, pixel);
				}
			}
			break;
		default:
			break;
		}
		
		return bi;
	}

	public Orientation getOrientation() {
		return this.orientation;
	}

	public void setOrientation(Orientation orientation) {
		this.orientation = orientation;
	}

}
