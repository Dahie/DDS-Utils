/**
 * 
 */
package org.dds_utils.dropps.models;

import org.jogl.DDSImage;

/**
 * @author danielsenff
 *
 */
public class ExportOptions {

	// DDS Operation settings
	private int newHeight = 0;
	private int newWidth = 0;
	private int newPixelformat = DDSImage.D3DFMT_UNKNOWN;
	private boolean generateMipMaps = true;
	private boolean paintWhiteAlpha = false;
	private boolean keepDimension = true;
	private boolean keepPixelformat = true;
	private boolean makeBackup = true;
	private boolean keepOriginal = true;

	/**
	 * 
	 */
	public ExportOptions() {}
	
	/**
	 * @param pixelformat
	 * @param generateMipMaps
	 */
	public ExportOptions(final int pixelformat, final boolean generateMipMaps) {
		setKeepDimension(true);
		setKeepPixelformat(false);
		this.newPixelformat = pixelformat;
		this.generateMipMaps = generateMipMaps;
	}
	
	/**
	 * @param width
	 * @param height
	 * @param pixelformat
	 * @param generateMipMaps
	 */
	public ExportOptions(final int width, 
			final int height, 
			final int pixelformat, 
			final boolean generateMipMaps) {
		this.setKeepDimension(false);
		this.newWidth = width;
		this.newHeight = height;
		this.setKeepPixelformat(false);
		this.newPixelformat = pixelformat;
		this.generateMipMaps = generateMipMaps;
	}
	
	public boolean hasGeneratedMipMaps() {
		return this.generateMipMaps;
	}

	public void setGenerateMipMaps(boolean generateMipMaps) {
		this.generateMipMaps = generateMipMaps;
	}
	

	public boolean isPaintWhiteAlpha() {
		return this.paintWhiteAlpha;
	}

	public void setPaintWhiteAlpha(boolean paintWhiteAlpha) {
		this.paintWhiteAlpha = paintWhiteAlpha;
	}
	

	public boolean isMakeBackup() {
		return this.makeBackup;
	}

	public void setMakeBackup(boolean makeBackup) {
		this.makeBackup = makeBackup;
	}


	public void setKeepOriginal(boolean keepOriginal) {
		this.keepOriginal = keepOriginal;
	}

	public int getNewHeight() {
		return this.newHeight;
	}

	public void setNewHeight(final int newHeight) {
		this.newHeight = newHeight;
	}
	
	public int getNewPixelformat() {
		return this.newPixelformat;
	}

	public void setNewPixelformat(int newPixelformat) {
		this.newPixelformat = newPixelformat;
	}
	
	public int getNewWidth() {
		return this.newWidth;
	}

	public void setNewWidth(final int newWidth) {
		this.newWidth = newWidth;
	}

	public boolean isKeepOriginal() {
		return this.keepOriginal;
	}

	/**
	 * @return the keepDimension
	 */
	public boolean isKeepDimension() {
		return keepDimension;
	}

	/**
	 * @param keepDimension the keepDimension to set
	 */
	public void setKeepDimension(boolean keepDimension) {
		this.keepDimension = keepDimension;
	}

	/**
	 * @return the keepPixelformat
	 */
	public boolean isKeepPixelformat() {
		return keepPixelformat;
	}

	/**
	 * @param keepPixelformat the keepPixelformat to set
	 */
	public void setKeepPixelformat(boolean keepPixelformat) {
		this.keepPixelformat = keepPixelformat;
	}
	
}
