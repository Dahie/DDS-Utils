package Model;

/**
 * 
 */

import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.ByteBuffer;
import java.util.Vector;

import Compression.DXTBufferDecompressor;
import DDSUtil.DDSUtil;
import DDSUtil.MipMapsUtil;
import DDSUtil.NonCubicDimensionException;
import JOGL.DDSImage;

/**
 * @author danielsenff
 *
 */
public class DDSFile extends AbstractImageFile{

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
	
	
	/**
	 * MipMap at the highest Level, ie the original 
	 */
	protected BufferedImage topmost;
	protected TextureType textureType;
	private DDSImage ddsimage;
	
	protected DDSFile() {}
	
	/**
	 * @throws IOException 
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
			init(ddsimage);
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
		init(ddsimage);
//		ddsimage.close();
	}
	
	/**
	 * Constructs a DDSFile from a filepath and a {@link DDSImage}
	 * @param filename
	 * @param ddsimage
	 * @throws InvalidObjectException 
	 */
	public DDSFile(final String filename, final DDSImage ddsimage) {
		this(new File(filename), ddsimage);
	}

	/**
	 * 
	 * @param filename
	 * @param bi
	 * @param pixelformat
	 * @param hasmipmaps
	 */
	public DDSFile(final File filename, 
			BufferedImage bi, 
			final int pixelformat, 
			final boolean hasmipmaps) {
		//super(filename);
		this.file = filename;
		
		this.height = bi.getHeight();
		this.width  = bi.getWidth();
		this.hasMipMaps = hasmipmaps;
		
		this.topmost = bi; 
		this.pixelformat = pixelformat;
	}
	
	/**
	 * @param ddsimage
	 */
	protected void init(final DDSImage ddsimage) {
		this.ddsimage 		= ddsimage;
		this.height 		= ddsimage.getHeight();
		this.width  		= ddsimage.getWidth();
		this.depth 			= ddsimage.getDepth();
		this.pixelformat 	= ddsimage.getPixelFormat();
		this.textureType	= getTextureType(ddsimage);
		this.numMipMaps 	= ddsimage.getNumMipMaps();
		this.hasMipMaps		= (ddsimage.getNumMipMaps() > 1); // there is always at least the topmost MipMap
	}

	public void loadImageData() {
		CompressionType compressionType = 
			DDSUtil.getSquishCompressionFormat(ddsimage.getPixelFormat());
		this.topmost = new DXTBufferDecompressor(
							ddsimage.getMipMap(0).getData(),
							ddsimage.getWidth(), 
							ddsimage.getHeight(), 
							compressionType).getImage();
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
	
	public void write(final File targetFile) throws IOException {
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
	
	/**
	 * Returns the topmost MipMap
	 * @return {@link BufferedImage}
	 */
	public BufferedImage getData() {
		return this.topmost;
	}
	
	/**
	 * Returns the stored MipMaps as a {@link BufferedImage}-Array
	 * @return
	 */
	public BufferedImage[] getAllMipMapsBI(){
		MipMaps mipMaps = new MipMaps();
		mipMaps.generateMipMaps(topmost);
		return mipMaps.getAllMipMapsArray();		
	}
	
	/**
	 * returns the stored MipMaps as {@link ByteBuffer}-Array
	 * @return
	 */
	public Vector<BufferedImage> generateAllMipMaps(){
		MipMaps mipMaps = new MipMaps();
		mipMaps.generateMipMaps(topmost);
		return mipMaps.getAllMipMaps();
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
	 * Activates the generation of MipMaps when saving the DDS to disc.
	 * @param generateMipMaps 
	 * @throws IllegalArgumentException 
	 */
	public void setHasMipMaps(final boolean generateMipMaps) throws IllegalArgumentException{
		if(isPowerOfTwo(topmost.getWidth()) && isPowerOfTwo(topmost.getHeight()))
			this.hasMipMaps = generateMipMaps;
		else throw new NonCubicDimensionException();
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
