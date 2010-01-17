package model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import ddsutil.DDSUtil;
import ddsutil.MipMapsUtil;
import ddsutil.NonCubicDimensionException;

import jogl.DDSImage;


public abstract class AbstractTextureImage implements TextureImage {

	protected int height;
	protected int width;
	protected int pixelformat;
	protected File file = null;
	protected boolean hasMipMaps;
	protected int numMipMaps;
	protected int depth;
	
	/**
	 * MipMap at the highest Level, ie the original 
	 */
	protected BufferedImage topmost;
	
	/**
	 * Depth of color for all channels
	 * @return int
	 */
	public int getDepth() {
		return depth;
	}
	
	/**
	 * Depth of color of each channel
	 * @return int
	 */
	public int getChannelDepth() {
		
		switch(this.pixelformat){
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
	public File getFile() {
		return this.file;
	}
	
	/**
	 * Width of the topmost MipMap
	 * @return
	 */
	public int getHeight() {
		return this.height;
	}

	/**
	 * Height of the topmost MipMap
	 * @return
	 */
	public int getWidth() {
		return this.width;
	}
	

	/**
	 * Get the Format in which pixel are stored in the file as internal stored Integer-value.
	 * @return in
	 */
	public int getPixelformat() {
		return this.pixelformat;
	}
	
	/**
	 * Sets the format in which pixel are stored in the file.
	 * @param pixelformat
	 */
	public void setPixelformat(final int pixelformat) {
		this.pixelformat = pixelformat;
	}
	
	/**
	 * Sets the format in which pixel are stored in the file.
	 * @param pixelformat
	 */
	public void setPixelformat(final PixelFormat pixelformat) {
		this.setPixelformat(convertPixelformat(pixelformat));
	}

	
	/**
	 * Returns whether or not the dds-file has MipMaps.
	 * Usually only textures whose size is a power of two may have mipmaps.
	 * @return boolean
	 */
	public boolean hasMipMaps() {
		return this.hasMipMaps;
	}
	
	/**
	 * Returns the number of MipMaps in this file.
	 * @return int Number of MipMaps
	 */
	public int getNumMipMaps() {
		return numMipMaps;
	}
	
	/**
	 * @param pixelformat
	 * @return
	 */
	protected static int convertPixelformat(final PixelFormat pixelformat) {
		int format;
		switch(pixelformat) {
			default:
			case Unknown:
				format = DDSImage.D3DFMT_UNKNOWN;
				break;
			case DXT5:
				format = DDSImage.D3DFMT_DXT5;
				break;
			case DXT4:
				format = DDSImage.D3DFMT_DXT4;
				break;
			case DXT3:
				format = DDSImage.D3DFMT_DXT3;
				break;
			case DXT2:
				format = DDSImage.D3DFMT_DXT2;
				break;
			case DXT1:
				format = DDSImage.D3DFMT_DXT1;
				break;
			case A8R8G8B8:
				format = DDSImage.D3DFMT_A8R8G8B8;
				break;
			case X8R8G8B8:
				format = DDSImage.D3DFMT_X8R8G8B8;
				break;
			case R8G8B8:
				format = DDSImage.D3DFMT_R8G8B8;
				break;
		}
		return format;
	}
	
	public void write() throws IOException {
		this.write(this.file);
	}
	
	/**
	 * Returns the internal Integer-value for the input pixelformat-Name
	 * @param pixelformatVerbose
	 * @return
	 */
	public static int verbosePixelformat(final String pixelformatVerbose) {
		if (pixelformatVerbose.equals(PixelFormat.DXT1.toString())) {
			return DDSImage.D3DFMT_DXT1;
		} else if (pixelformatVerbose.equals(PixelFormat.DXT2.toString())) {
			return DDSImage.D3DFMT_DXT2;
		} else if (pixelformatVerbose.equals(PixelFormat.DXT3.toString())) {
			return DDSImage.D3DFMT_DXT3;
		} else if (pixelformatVerbose.equals(PixelFormat.DXT4.toString())) {
			return DDSImage.D3DFMT_DXT4;
		} else if (pixelformatVerbose.equals(PixelFormat.DXT5.toString())) {
			return DDSImage.D3DFMT_DXT5;
		} else if (pixelformatVerbose == PixelFormat.R8G8B8.toString()) {
			return DDSImage.D3DFMT_R8G8B8;
		} else if (pixelformatVerbose.equals(PixelFormat.X8R8G8B8.toString())) {
			return DDSImage.D3DFMT_X8R8G8B8;
		} else if (pixelformatVerbose.equals(PixelFormat.A8R8G8B8.toString())) {
			return DDSImage.D3DFMT_A8R8G8B8;
		} else {
			return DDSImage.D3DFMT_UNKNOWN;
		}
	}
	
	/**
	 * Returns true if the dds-file is compressed as DXT1-5
	 * @return boolean
	 */
	public boolean isCompressed() {
		return DDSUtil.isDXTCompressed(pixelformat);
	}
	
	/**
	 * Gets the format in which pixels are stored as a verbose {@link String}.
	 * @return
	 */
	public String getPixelformatVerbose() {
		return verbosePixelformat(this.pixelformat);
	}
	
	/**
	 * Returns the verbose Pixelformat this DDSFile for the pixelformat-code
	 * @param pixelformat
	 * @return String
	 */
	public static String verbosePixelformat(final int pixelformat) {
		switch(pixelformat) {
		default:
			return PixelFormat.Unknown.toString();
		case DDSImage.D3DFMT_A8R8G8B8:
			return PixelFormat.Unknown.toString();
		case DDSImage.D3DFMT_DXT1:
			return PixelFormat.DXT1.toString();
		case DDSImage.D3DFMT_DXT2:
			return PixelFormat.DXT2.toString();
		case DDSImage.D3DFMT_DXT3:
			return PixelFormat.DXT3.toString();
		case DDSImage.D3DFMT_DXT4:
			return PixelFormat.DXT4.toString();
		case DDSImage.D3DFMT_DXT5:
			return PixelFormat.DXT5.toString();
		case DDSImage.D3DFMT_R8G8B8:
			return PixelFormat.R8G8B8.toString();
		case DDSImage.D3DFMT_X8R8G8B8:
			return PixelFormat.X8R8G8B8.toString();
		}
	}
	
	/**
	 * Activates the generation of MipMaps when saving the Texture to disk.
	 * @param generateMipMaps 
	 * @throws IllegalArgumentException 
	 */
	public void setHasMipMaps(final boolean generateMipMaps) throws IllegalArgumentException{
		if(isPowerOfTwo(topmost.getWidth()) && isPowerOfTwo(topmost.getHeight()))
			this.hasMipMaps = generateMipMaps;
		else throw new NonCubicDimensionException();
	}
	
	/**
	 * Sets a new {@link BufferedImage} as the Topmost MipMap and generates new MipMaps accordingly.
	 * @param bi
	 */
	public void setData(final BufferedImage bi) {
		this.width = bi.getWidth();
		this.height = bi.getHeight();
		this.topmost = bi;
	}
	
	/**
	 * Returns the topmost MipMap
	 * @return {@link BufferedImage}
	 */
	public BufferedImage getData() {
		return this.topmost;
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
	
	/**
	 * Calculates the number of MipMaps generated for this image dimensions.
	 * @param width
	 * @param height
	 * @return
	 */
	public static int calculateMaxNumberOfMipMaps(final int width, final int height) {
		return MipMapsUtil.calculateMaxNumberOfMipMaps(width, height);
	}

}
