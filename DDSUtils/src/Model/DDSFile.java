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
public class DDSFile extends AbstractTextureImage{

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
		this.width  		= ddsimage.getWidth();
		this.height 		= ddsimage.getHeight();
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
	
	public void write(final File targetFile) throws IOException {
		ByteBuffer[] mipmaps = new ByteBuffer[getNumMipMaps()];
		for (int i = 0; i < mipmaps.length; i++) {
			mipmaps[i] = DDSImage.read(this.file).getMipMap(i).getData();
		}
		
		DDSImage outputDDS = DDSImage.createFromData(this.pixelformat, width, height, mipmaps);
		outputDDS.write(file);
		outputDDS.close();
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
	
}
