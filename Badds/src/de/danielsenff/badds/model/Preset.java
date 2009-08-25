/**
 * 
 */
package de.danielsenff.badds.model;

/**
 * @author danielsenff
 *
 */
public class Preset {
	
	private String name;
	private int width;
	private int height;
	private int pixelformat; // pixelformat from DDSImage.class
	private boolean mipmaps;
	
	/**
	 * @param name
	 * @param width
	 * @param height
	 * @param pixelformat pixelformat from DDSImage
	 */
	public Preset(String name, int width, int height, int pixelformat, boolean mipmaps) {
		super();
		this.name = name;
		this.width = width;
		this.height = height;
		this.pixelformat = pixelformat;
		this.mipmaps = mipmaps;
	}
	
	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

	public int getPixelformat() {
		return this.pixelformat;
	}



	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}
	

	public boolean isMipmaps() {
		return this.mipmaps;
	}
}
