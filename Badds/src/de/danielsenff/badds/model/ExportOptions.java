/**
 * 
 */
package de.danielsenff.badds.model;

import JOGL.DDSImage;

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
	private boolean makeBackup = true;
	private boolean keepOriginal = true;

	public ExportOptions() {}
	
	/**
	 * 
	 */
	public ExportOptions(Preset preset) {
		setPreset(preset);
	}

	public void setPreset(Preset preset) {
		this.newHeight = preset.getHeight();
		this.generateMipMaps = preset.isMipmaps();
		this.newWidth = preset.getWidth();
		this.newPixelformat = preset.getPixelformat();
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

	/**
	 * @return
	 */
	public boolean isKeepOriginal() {
		return this.keepOriginal;
	}

}
