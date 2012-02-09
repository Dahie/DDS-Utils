/**
 * 
 */
package de.danielsenff.badds.operations;

import java.awt.image.BufferedImage;

import ddsutil.ImageRescaler;
import ddsutil.Rescaler;


/**
 * @author danielsenff
 *
 */
public class ScaleOperation implements Operation{

	private int newHeight;
	private int newWidth;
	private boolean postprocess;
	
	private Rescaler rescaler;
	
	/**
	 * 
	 */
	private ScaleOperation() {
		this.newHeight = 100;
		this.newWidth  = 100;
		this.postprocess = false;
		this.rescaler = new ImageRescaler();
	}
	
	/**
	 * @param newWidth 
	 * @param newHight 
	 * 
	 */
	public ScaleOperation(final int newWidth, final int newHight) {
		this.newHeight = newHight;
		this.newWidth  = newWidth;
		this.postprocess = false;
		this.rescaler = new ImageScalingRescaler();
	}
	
	
	@Override
	public BufferedImage run(final BufferedImage bi) {
		return rescaler.rescaleBI(bi, newWidth, newHeight);
	}

	@Override
	public String toString() {
		return "Scale to width: " + this.newWidth + " and height: " + this.newHeight;
	}

	public int getNewHeight() {
		return this.newHeight;
	}

	public void setNewHeight(final int ght) {
		this.newHeight = ght;
	}

	public int getNewWidth() {
		return this.newWidth;
	}

	public void setNewWidth(final int newWidth) {
		this.newWidth = newWidth;
	}


}
