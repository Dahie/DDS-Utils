package org.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jogl.DDSImage;
import org.ddsutil.NonCubicDimensionException;
import org.ddsutil.PixelFormats;


public abstract class AbstractTextureImage implements TextureImage {

	protected int height;
	protected int width;
	protected int pixelFormat;
	protected File file = null;
	protected boolean hasMipMaps = false;
	protected int numMipMaps = 0;
	protected int depth;
	
	/**
	 * MipMap at the highest Level, ie the original 
	 */
	protected MipMaps mipMaps = new MipMaps();

	public AbstractTextureImage(final File file) {
		this.file = file;
	}

	/**
	 * Depth of color for all channels
	 * @return int
	 */
	@Override
	public int getDepth() {
		return depth;
	}
	
	/**
	 * Depth of color of each channel
	 * @return int
	 */
	@Override
	public int getChannelDepth() {
		switch(this.pixelFormat){
			case DDSImage.D3DFMT_A8R8G8B8:
			case DDSImage.D3DFMT_X8R8G8B8:
			case DDSImage.D3DFMT_DXT5:
			case DDSImage.D3DFMT_DXT3:
			case DDSImage.D3DFMT_DXT2:
			case DDSImage.D3DFMT_DXT4:
				return depth/4;
			case DDSImage.D3DFMT_DXT1:
			case DDSImage.D3DFMT_R8G8B8:
				return depth/3;
		}
		return 0;
	}

	/**
	 * Returns the absolute path to the {@link File}.
	 * @return
	 */
	@Override
	public String getAbsolutePath() {
		return this.file.getAbsolutePath();
	}

	/**
	 * Returns the name of the {@link File}.
	 * @return
	 */
	private String getFileName() {
		return this.file.getName();
	}
	
	/**
	 * Returns the associated {@link File}
	 * @return File
	 */
	@Override
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Width of the topmost MipMap
	 * @return
	 */
	@Override
	public int getHeight() {
		return this.height;
	}

	/**
	 * Height of the topmost MipMap
	 * @return
	 */
	@Override
	public int getWidth() {
		return this.width;
	}

	/**
	 * Get the Format in which pixel are stored in the file as internal stored Integer-value.
	 * @return in
	 */
	@Override
	public int getPixelFormat() {
		return this.pixelFormat;
	}
	
	/**
	 * Sets the format in which pixel are stored in the file.
	 * @param pixelFormat
	 */
	@Override
	public void setPixelFormat(final int pixelFormat) {
		this.pixelFormat = pixelFormat;
	}
	
	/**
	 * Sets the format in which pixel are stored in the file.
	 * @param pixelFormat
	 */
	@Override
	public void setPixelFormat(final PixelFormat pixelFormat) {
		this.setPixelFormat(PixelFormats.convertPixelFormat(pixelFormat));
	}

	
	/**
	 * Returns whether or not the dds-file has MipMaps.
	 * Usually only textures whose size is a power of two may have mipmaps.
	 * @return boolean
	 */
	@Override
	public boolean hasMipMaps() {
		return this.hasMipMaps;
	}
	
	/**
	 * Returns the number of MipMaps in this file.
	 * @return int Number of MipMaps
	 */
	@Override
	public int getNumMipMaps() {
		return numMipMaps;
	}
	
	

	/**
	 * Returns true if the dds-file is compressed as DXT1-5
	 * @return boolean
	 */
	@Override
	public boolean isCompressed() {
		return PixelFormats.isDXTCompressed(pixelFormat);
	}
	
	/**
	 * Gets the format in which pixels are stored as a verbose {@link String}.
	 * @return
	 */
	@Override
	public String getPixelformatVerbose() {
		return PixelFormats.verbosePixelformat(this.pixelFormat);
	}
	
	/**
	 * Activates the generation of MipMaps when saving the Texture to disk.
	 * @param generateMipMaps 
	 * @throws IllegalArgumentException 
	 */
	@Override
	public void setHasMipMaps(final boolean generateMipMaps) throws IllegalArgumentException{
		if(isPowerOfTwo(getTopMipMap().getWidth()) && isPowerOfTwo(getTopMipMap().getHeight()))
			this.hasMipMaps = generateMipMaps;
		else throw new NonCubicDimensionException();
	}
	
	/**
	 * Sets a new {@link BufferedImage} as the Topmost MipMap and generates new MipMaps accordingly.
	 * @param bufferedImage
	 */
	@Override
	public void setData(final BufferedImage bufferedImage) {
		this.width = bufferedImage.getWidth();
		this.height = bufferedImage.getHeight();
		this.setTopMipMap(bufferedImage);
	}

	/**
	 * Sets the topmost MipMap.
	 * @param bi
	 */
	public void setTopMipMap(BufferedImage bi) {
		this.mipMaps.setMipMap(TOP_MOST_MIP_MAP, bi);
	}
	
	/**
	 * Returns the top-most MipMap.
	 * @return 
	 */
	public BufferedImage getTopMipMap() {
		return this.mipMaps.getMipMap(TOP_MOST_MIP_MAP);
	}

	/**
	 * Returns the topmost MipMap
	 * @return {@link BufferedImage}
	 */
	@Override
	public BufferedImage getBufferedImage() {
		// FIXME shouldn't this be getImage?
		return this.getTopMipMap();
	}
	
	/**
	 * Checks if a value is a power of two
	 * @param value
	 * @return
	 */
	public static boolean isPowerOfTwo(final int value) {
		double p = Math.floor(Math.log(value) / Math.log(2.0));
		double n = Math.pow(2.0, p);
	    return (n==value);
	}
}
