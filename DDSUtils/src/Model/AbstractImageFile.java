package Model;

import java.io.File;
import java.io.IOException;

import JOGL.DDSImage;
import Model.DDSFile.TextureType;

public abstract class AbstractImageFile implements TextureImage {

	protected int height;
	protected int width;
	protected int pixelformat;
	protected File file = null;
	protected boolean hasMipMaps;
	protected int numMipMaps;
	protected int depth;
	
	
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

	public String getPixelformatVerbose() {
		// TODO Auto-generated method stub
		return null;
	}

	public TextureType getTextureType() {
		// TODO Auto-generated method stub
		return null;
	}


	public boolean isCompressed() {
		// TODO Auto-generated method stub
		return false;
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

}
