/**
 * 
 */
package Model;

import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.nio.ByteBuffer;
import java.util.Vector;

import util.Stopwatch;

import Compression.DXTBufferDecompressor;
import DDSUtil.DDSUtil;
import DDSUtil.MipMapsUtil;
import DDSUtil.NonCubicDimensionException;
import JOGL.DDSImage;

/**
 * @author Daniel Senff
 *
 */
public class DDSImageFile extends DDSFile {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8820796948052852501L;
	/**
	 * MipMap at the highest Level, ie the original 
	 */
	protected BufferedImage topmost;
	
	
	/**
	 * @param filename 
	 * @throws IOException 
	 */
	public DDSImageFile(final String filename) {
		this(new File(filename));
	}
	
	/**
	 * @param file
	 * @param ddsimage 
	 * @throws IOException
	 */
	public DDSImageFile(final File file) {
		super.file = file;
		DDSImage ddsimage;
		try {
			ddsimage = DDSImage.read(file);
			initDdsValues(ddsimage);
			CompressionType compressionType = DDSUtil.getSquishCompressionFormat(ddsimage.getPixelFormat());
			this.topmost = new DXTBufferDecompressor(
					ddsimage.getMipMap(0).getData(),
					ddsimage.getWidth(), 
					ddsimage.getHeight(), 
					compressionType).getImage();
			ddsimage.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		System.gc();
	}
	
	/**
	 * 
	 * @param filename
	 * @param ddsimage
	 * @throws InvalidObjectException 
	 */
	public DDSImageFile(String filename, DDSImage ddsimage) {
		this(new File(filename), ddsimage);
	}

	/**
	 * 
	 * @param file
	 * @param ddsimage
	 * @throws InvalidObjectException 
	 */
	public DDSImageFile(File file, DDSImage ddsimage) {
		super(file, ddsimage);
		
		CompressionType compressionType = 
			DDSUtil.getSquishCompressionFormat(ddsimage.getPixelFormat());
		this.topmost = 
			new DXTBufferDecompressor(ddsimage.getMipMap(0).getData(),ddsimage.getWidth(), ddsimage.getHeight(), compressionType).getImage();
	}

	
	/**
	 * @param filename
	 * @param bi
	 * @param pixelformat
	 * @param hasmipmaps
	 */
	public DDSImageFile(final String filename, 
			final BufferedImage bi, 
			final int pixelformat, 
			final boolean hasmipmaps) {
		this(new File(filename), bi, pixelformat, hasmipmaps);
	}
	
	/**
	 * 
	 * @param filename
	 * @param bi
	 * @param pixelformat
	 * @param hasmipmaps
	 */
	public DDSImageFile(final File filename, 
			BufferedImage bi, 
			final int pixelformat, 
			final boolean hasmipmaps) {
		//super(filename);
		super.file = filename;
		
		
		this.height = bi.getHeight();
		this.width  = bi.getWidth();
		this.hasMipMaps = hasmipmaps;
		
		this.topmost = bi; 
		this.pixelformat = pixelformat;
	}
	
	
	protected ByteBuffer[] getMipMapBuffer(DDSImage ddsimage) {
		ByteBuffer[] buffer = new ByteBuffer[this.numMipMaps];
//		ddsimage.debugPrint();
		if (hasMipMaps) {
			for (int i = 0; i < numMipMaps; i++) {
				buffer[i] = ddsimage.getMipMap(i).getData();
			}
		}  else {
			//TODO here is a arrayindexoutofbound and I don't know why
			buffer[TOP_MOST_MIP_MAP] = ddsimage.getMipMap(TOP_MOST_MIP_MAP).getData();
		}
			
		return buffer;
	}


	/**
	 * Returns the MipMap on index as a {@link BufferedImage}
	 * @param index
	 * @return BufferedImage
	 */
	/*public BufferedImage getMipMapDataBI(final int index) {
		return mipmapBI[index];
	}*/
	
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
	 * Calculates the number of MipMaps generated for this image dimensions.
	 * @param width
	 * @param height
	 * @return
	 */
	public static int calculateMaxNumberOfMipMaps(final int width, final int height) {
		return MipMapsUtil.calculateMaxNumberOfMipMaps(width, height);
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
	
	
	public void write() throws IOException {
		this.write(this.file);
	}
	
	public void write(final String filename) throws IOException {
		write(new File(filename));
	}
	
	public void write(final File targetFile) throws IOException {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		// Original data
		System.out.println("Write file: " + targetFile.getAbsolutePath());
		
		DDSUtil.write(targetFile, topmost, pixelformat, hasMipMaps);
		
		stopwatch.stop();
		stopwatch.printMilliseconds("Time writing DDSImage: ");
	}

	/**
	 * Closes the included ddsimage and finalizes the object. 
	 * Don't use this object after calling this method
	 * @throws Throwable
	 */
	public void close() {
		try {
			this.finalize();
		} catch (Throwable e) {
//			e.printStackTrace();
		}
	}

	/**
	 * Activates the generation of MipMaps when saving the DDS to disc.
	 * @param generateMipMaps 
	 * @throws IllegalArgumentException 
	 */
	public void activateMipMaps(final boolean generateMipMaps) throws IllegalArgumentException{
		if(isPowerOfTwo(topmost.getWidth()) && isPowerOfTwo(topmost.getHeight()))
			super.hasMipMaps = generateMipMaps;
		else throw new NonCubicDimensionException();
	}
	
	@Override
	public boolean hasMipMaps() {
		return super.hasMipMaps;
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
