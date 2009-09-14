package Model;

/**
 * 
 */

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.ByteBuffer;

import DDSUtil.DDSUtil;
import JOGL.DDSImage;
import JOGL.DDSImage.ImageInfo;

/**
 * @author danielsenff
 *
 */
public class DDSFile {

	/**
	 * Pixelfomat describes the way pixels are stored in the DDS.
	 * Either uncompressed or with a special compression format.
	 *
	 */
	public enum PixelFormat {
		DXT5, DXT4, DXT3, DXT2, DXT1,
		A8R8G8B8, X8R8G8B8, R8G8B8, Unknown
	}
	
	/**
	 * Topmost MipMap Index 
	 */
	public static final int TOP_MOST_MIP_MAP = 0;
	
	/**
	 * TextureType describes what kind of Texture the DDS is. Regular 2D-Texture, Volume or Cubemap.
	 *
	 */
	public enum TextureType {
		/**
		 * Regular texture (plus MipMaps) with one slice.
		 */
		TEXTURE, 
		/**
		 * Cubemaps contain 6 slices (including MipMaps) for 6 sides of a cube.
		 */
		CUBEMAP, 
		/**
		 * Volume-textures contain many slices (including MipMaps).
		 */
		VOLUME
	}
	
	
	protected int height;
	protected int width;
	protected int pixelformat;
	protected File file = null;
	private int depth;
	protected int numMipMaps;
	protected boolean hasMipMaps;

	protected TextureType textureType;
	
	protected DDSFile() {}
	
	/**
	 * @throws IOException 
	 * 
	 */
	public DDSFile(final String filename) {
		this(new File(filename));
	}
	
	/**
	 * Constructs a DDSFile from a {@link File}
	 * @param file
	 * @throws IOException
	 */
	public DDSFile(final File file) {
		this.file = file;
		DDSImage ddsimage = null;
		try {
			ddsimage = DDSImage.read(file);
			initDdsValues(ddsimage);
//			ddsimage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructs a DDSFile from a {@link File} and a {@link DDSImage}
	 * @param file
	 * @param ddsimage
	 * @throws InvalidObjectException 
	 */
	public DDSFile(final File file, final DDSImage ddsimage) {
		this.file = file;
		initDdsValues(ddsimage);
//		ddsimage.close();
	}
	
	/**
	 * Constructs a DDSFile from a filepath and a {@link DDSImage}
	 * @param filename
	 * @param ddsimage
	 * @throws InvalidObjectException 
	 */
	public DDSFile(final String filename, final DDSImage ddsimage) throws InvalidObjectException {
		this(new File(filename), ddsimage);
	}

	/**
	 * @param ddsimage
	 */
	protected void initDdsValues(final DDSImage ddsimage) {
		this.height 		= ddsimage.getHeight();
		this.width  		= ddsimage.getWidth();
		this.depth 			= ddsimage.getDepth();
		this.pixelformat 	= ddsimage.getPixelFormat();
		this.textureType	= getTextureType(ddsimage);
		this.numMipMaps 	= ddsimage.getNumMipMaps();
		/*if(numMipMaps == 0)
			throw new InvalidObjectException("DDSImage has no mipmaps");*/

		this.hasMipMaps		= (ddsimage.getNumMipMaps() > 1); // there is always at least the topmost MipMap
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
	 * Gets the format in which pixels are stored as a verbose {@link String}.
	 * @return
	 */
	public String getPixelformatVerbose() {
		return verbosePixelformat(this.pixelformat);
	}
	
	/**
	 * Returns true if the dds-file is compressed as DXT1-5
	 * @return boolean
	 */
	public boolean isCompressed() {
		return DDSUtil.isDXTCompressed(pixelformat);
	}
	
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
	
	@Override
	public String toString() {
		return this.file.getAbsolutePath()+verbosePixelformat(this.pixelformat);
	}
	
	@Override
	public boolean equals(Object second) {
		if(second != null && second instanceof DDSFile) {
			DDSFile secondFile = (DDSFile) second;
			boolean isEqual = (this.getFile().getAbsoluteFile().equals(secondFile.getFile().getAbsoluteFile()) && 
					this.hasMipMaps() == secondFile.hasMipMaps() &&
					this.getPixelformat() == secondFile.getPixelformat() &&
					this.getHeight() == secondFile.getHeight() &&
					this.getWidth() == secondFile.getWidth());
			return isEqual;	
		}
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
	
//	public ByteBuffer[] getMipMapData() {
//		ByteBuffer[] buffer = new ByteBuffer[ddsimage.getNumMipMaps()+1];
//		for (int i = 0; i < buffer.length; i++) {
//			buffer[i] = ddsimage.getMipMap(i).getData();
//		}
//		return buffer;
//	}
	
	/**
	 * The DDS-Image can have different texture types. 
	 * Regular Texture, Volume-Texture and CubeMap
	 * @return TextureType Type of Texture
	 */
	public TextureType getTextureType() {
		return this.textureType;
	}
	
	/**
	 * The DDS-Image can have different texture types. 
	 * Regular Texture, Volume-Texture and CubeMap
	 * This returns the textureType from a {@link DDSImage}
	 * @param ddsimage
	 * @return
	 */
	public static TextureType getTextureType(final DDSImage ddsimage) {
		if(ddsimage.isCubemap()) {
			return TextureType.CUBEMAP;
		} else if (ddsimage.isVolume()) {
			return TextureType.VOLUME;
		} else {
			return TextureType.TEXTURE;
		}
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
	 * @param pixelformat
	 * @return
	 */
	protected int convertPixelformat(final PixelFormat pixelformat) {
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
	
	/**
	 * Write to disc
	 * @throws IOException
	 */
	public void write() throws IOException {
		this.write(this.file);
	}
	
	/**
	 * Write this dds to disc
	 * @param filename
	 * @throws IOException
	 */
	public void write(final String filename) throws IOException {
		write(new File(filename));
	}
	
	/**
	 * Write this dds to disc
	 * @param file
	 * @throws IOException
	 */
	public void write(final File file) throws IOException {
		
		ByteBuffer[] mipmaps = new ByteBuffer[this.numMipMaps];
		for (int i = 0; i < mipmaps.length; i++) {
			mipmaps[i] = DDSImage.read(this.file).getMipMap(i).getData();
		}
		

		DDSImage writedds = DDSImage.createFromData(this.pixelformat, width, height, mipmaps);
		writedds.write(file);
		writedds.close();
	}
	
	/**
	 * Checks if the {@link File} is a valid DDS-Image
	 * @param file 
	 * @return 
	 * @throws IOException 
	 * 
	 */
	public static boolean isValidDDSImage(final File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		boolean isDDSImage = DDSImage.isDDSImage(fis);
		fis.close();
		return isDDSImage;
	}

}
