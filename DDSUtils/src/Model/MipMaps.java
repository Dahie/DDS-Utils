/**
 * 
 */
package Model;

import gr.zdimensions.jsquish.Squish;
import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.Vector;

import Compression.DXTBufferDecompressor;
import DDSUtil.ByteBufferedImage;
import DDSUtil.DDSUtil;
import DDSUtil.ImageRescaler;
import DDSUtil.MipMapsUtil;
import DDSUtil.NonCubicDimensionException;
import DDSUtil.Rescaler;
import JOGL.DDSImage;
import JOGL.DDSImage.ImageInfo;

/**
 * @author danielsenff
 *
 */
public class MipMaps extends AbstractTextureMap implements Iterable<BufferedImage> {

	
	/**
	 * Topmost MipMap Index 
	 */
	public static final int TOP_MOST_MIP_MAP = 0;
	
	Vector<BufferedImage> mipmaps;
	protected Rescaler rescaler;
	private int numMipMaps;
	
	/**
	 * @param topmost 
	 * 
	 */
	public MipMaps() {
		this.rescaler = new ImageRescaler();
		this.mipmaps = new Vector<BufferedImage>();
	}
	
	/**
	 * Populate this MipMap-Object based on the given topmost Map.
	 * @param topmost 
	 */
	public void generateMipMaps(BufferedImage topmost) {
		this.mipmaps.add(topmost);
		
		if(!DDSImageFile.isPowerOfTwo(topmost.getWidth()) 
				&& !DDSImageFile.isPowerOfTwo(topmost.getHeight())) 
			throw new NonCubicDimensionException();
		
		this.mipmaps = generateMipMapArray(this.mipmaps);	
	}

	private Vector<BufferedImage> generateMipMapArray(Vector<BufferedImage> mipMapsVector) {
		BufferedImage topmost = mipMapsVector.get(0);
		// dimensions of first map
		int mipmapWidth = topmost.getWidth(); 
		int mipmapHeight = topmost.getHeight();
		numMipMaps = MipMapsUtil.calculateMaxNumberOfMipMaps(mipmapWidth, mipmapHeight);
		
		BufferedImage previousMap = topmost;
		for (int i = 1; i < numMipMaps; i++) {
			// calculation for next map
			mipmapWidth = MipMaps.calculateMipMapSize(mipmapWidth);
			mipmapHeight = MipMaps.calculateMipMapSize(mipmapHeight);
			BufferedImage mipMapBi = rescaler.rescaleBI(previousMap, mipmapWidth, mipmapHeight);
			mipMapsVector.add(mipMapBi);
			// by using this map in the next MipMap generation step, we increase
			// performance, since we don't always scale from the biggest image.
			previousMap = mipMapBi;
		}
		return mipMapsVector;
	}

	
	/**
	 * Returns the highest MipMap in the original resolution. 
	 * @return
	 */
	public BufferedImage getTopMostMipMap() {
		return this.mipmaps.get(0);
	}
	
	/**
	 * @return
	 */
	public int getNumMipMaps() {
		return this.numMipMaps;
	}
	
	public int getHeight() {
		return this.mipmaps.get(TOP_MOST_MIP_MAP).getHeight();
	}
	

	public int getWidth() {
		return this.mipmaps.get(TOP_MOST_MIP_MAP).getWidth();
	}

	/**
	 * Returns a Map of the given level.
	 * @param index
	 * @return
	 */
	public BufferedImage getMipMap(final int index) {
		return this.mipmaps.get(index);
	}
	
	/**
	 * All contained MipMaps compressed with DXT in {@link ByteBuffer}
	 * Squishes each mipmap and store in a {@link DDSImage} compatible {@link ByteBuffer}-Array.
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final Squish.CompressionType compressionType) {
		ByteBuffer[] mipmapBuffer = new ByteBuffer[this.numMipMaps];
		
		for (int j = 0; j < this.numMipMaps; j++) {
			mipmapBuffer[j] = compress(this.mipmaps.get(j), compressionType);
		}
		return mipmapBuffer;
	}
	
	/**
	 * Returns a Vector with all MipMaps
	 * @return
	 */
	public Vector<BufferedImage> getAllMipMaps() {
		return this.mipmaps;
	}
	
	public BufferedImage[] getAllMipMapsArray() {
		BufferedImage[] array = new BufferedImage[numMipMaps];
		for (int i = 0; i < numMipMaps; i++) {
			array[i] = this.mipmaps.get(i);
		}
		return array;
	}

	/* (non-Javadoc)
	 * @see DDSUtil.AbstractTextureMap#getUncompressedBuffer()
	 */
	public ByteBuffer[] getUncompressedBuffer() {
		ByteBuffer[] mipmapBuffer = new ByteBuffer[numMipMaps];
		for (int i = 0; i < numMipMaps; i++) {
			mipmapBuffer[i] = ByteBuffer.wrap(ByteBufferedImage.convertBIintoARGBArray(this.mipmaps.get(i)));
		}
		return mipmapBuffer;
	}

	/**
	 * @param topmost
	 * @param mipmapWidth
	 * @param mipmapHeight
	 * @param mipmapBI
	 * @return
	 */
	public static BufferedImage[] generateMipMaps(final BufferedImage topmost, 
			int mipmapWidth,
			int mipmapHeight, 
			final BufferedImage[] mipmapBI) {
		int i = 0; // cause the first already is set
		while(true) {
			
			ImageRescaler rescaler = new ImageRescaler();
			mipmapBI[i] = rescaler.rescaleBI(mipmapBI[i], mipmapWidth, mipmapHeight);
			
			if (mipmapWidth == 1 || mipmapHeight == 1) 
				break;
			
			i++;
			mipmapWidth = calculateMipMapSize(mipmapWidth);
			mipmapHeight = calculateMipMapSize(mipmapHeight);
		}
		return mipmapBI;
	}

	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	public Iterator<BufferedImage> iterator() {
		return new Iterator<BufferedImage>() {
			int count=0;
			
			public boolean hasNext() {
				boolean b = count++ < mipmaps.size()-1;
				return b;
			}

			public BufferedImage next() {
				return mipmaps.get(count);
			}

			public void remove() {
				 throw new UnsupportedOperationException();
			}
			
		};
	}

	public Rescaler getRescaler() {
		return this.rescaler;
	}

	public void setRescaler(Rescaler rescaler) {
		this.rescaler = rescaler;
	}

	/**
	 * returns the new size for the next iteration of a generated MipMap
	 * Usually half the current value, unless current value is 1
	 * @param currentValue 
	 * @return
	 */
	public static int calculateMipMapSize(final int currentValue) {
		return (currentValue > 1) ? currentValue/2 : 1;
	}

	/**
	 * @param targetIndex
	 * @param currentValue
	 * @return
	 */
	public static int getMipMapSizeAtIndex(final int targetIndex, final int currentValue) {
		int newValue = currentValue;
		for (int i = 0; i < targetIndex; i++) {
			newValue = MipMaps.calculateMipMapSize(newValue);
		}
		return newValue;
	}
	
	public int getMipMapWidth(int index) {
		return this.mipmaps.get(index).getWidth();
	}
	
	public int getMipMapHeight(int index) {
		return this.mipmaps.get(index).getHeight();
	}
	
	/**
	 * Returns the {@link Dimension} of the MipMap at the given index.
	 * @param index
	 * @return
	 */
	public Dimension getMipMapDimension(final int index) {
		return new Dimension(
				getMipMapWidth(index),
				getMipMapHeight(index));
	}
	
}
