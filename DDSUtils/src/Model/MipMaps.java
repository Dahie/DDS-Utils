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
	 * 
	 */
	public MipMaps(final BufferedImage topmost) {
		this.rescaler = new ImageRescaler();
		this.mipmaps = new Vector<BufferedImage>();
		
		int mipmapWidth = topmost.getWidth();
		int mipmapHeight = topmost.getHeight();
		
		if(!DDSImageFile.isPowerOfTwo(topmost.getWidth()) 
				&& !DDSImageFile.isPowerOfTwo(topmost.getHeight())) 
			throw new NonCubicDimensionException();
		
		this.mipmaps = generateMipMapArray(topmost, mipmapWidth, mipmapHeight);	
	}

	private Vector<BufferedImage> generateMipMapArray(final BufferedImage topmost,
			int mipmapWidth, int mipmapHeight) {
		numMipMaps = MipMapsUtil.calculateMaxNumberOfMipMaps(mipmapWidth, mipmapHeight);
		Vector<BufferedImage> mipMapsVector = new Vector<BufferedImage>();
		for (int i = 0; i < numMipMaps; i++) {
			BufferedImage mipMapBi = rescaler.rescaleBI(topmost, mipmapWidth, mipmapHeight);
			mipMapsVector.add(mipMapBi);
			mipmapWidth = MipMaps.calculateMipMapSize(mipmapWidth);
			mipmapHeight = MipMaps.calculateMipMapSize(mipmapHeight);
		}
		return mipMapsVector;
	}
	
	/**
	 * @param ddsimage
	 */
	public MipMaps(DDSImage ddsimage) {
		this.rescaler = new ImageRescaler();
		this.mipmaps = new Vector<BufferedImage>();
		
		CompressionType compressionType = DDSUtil.getSquishCompressionFormat(ddsimage.getPixelFormat());
		for (int i = 0; i < ddsimage.getNumMipMaps(); i++) {
			ImageInfo mipMap = ddsimage.getMipMap(i);
			BufferedImage map = new DXTBufferDecompressor(mipMap.getData(), 
					mipMap.getWidth(), 
					mipMap.getHeight(),
					compressionType).getImage();
			this.mipmaps.add(map);
		}
	}

	public int getNumMipMaps() {
		return this.numMipMaps;
	}
	
	public int getHeight() {
		return this.mipmaps.get(TOP_MOST_MIP_MAP).getHeight();
	}
	

	public int getWidth() {
		return this.mipmaps.get(TOP_MOST_MIP_MAP).getWidth();
	}

	public BufferedImage getMipMap(final int index) {
		return this.mipmaps.get(index);
	}
	
	/**
	 * All contained MipMaps compressed with DXT in {@link ByteBuffer}
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final Squish.CompressionType compressionType) {
		BufferedImage bufferedImage = this.mipmaps.get(TOP_MOST_MIP_MAP);
		return compressMipMaps(bufferedImage.getWidth(), 
				bufferedImage.getHeight(), 
				compressionType);
	}
	
	
	
	/**
	 * Squish each mipmap
	 * @param mipMapWidth
	 * @param mipMapHeight
	 * @param mipmapBuffer
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer[] compressMipMaps(int mipMapWidth, int mipMapHeight,
			final Squish.CompressionType compressionType) {

		ByteBuffer[] mipmapBuffer = new ByteBuffer[this.numMipMaps];
		
		for (int j = 0; j < this.numMipMaps; j++) {
			mipmapBuffer[j] = super.compress(this.mipmaps.get(j), compressionType);
		}
		return mipmapBuffer;
	}

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

	public static BufferedImage[] generateMipMaps(BufferedImage topmost, int mipmapWidth,
			int mipmapHeight, BufferedImage[] mipmapBI) {
		int i = 0; // cause the first already is set
		while(true) {
			
			ImageRescaler rescaler = new ImageRescaler();
			mipmapBI[i] = rescaler.rescaleBI(mipmapBI[i], mipmapWidth, mipmapHeight);
			
			if (mipmapWidth == 1 || mipmapHeight == 1) 
				break;
			
			i++;
			mipmapWidth = MipMaps.calculateMipMapSize(mipmapWidth);
			mipmapHeight = MipMaps.calculateMipMapSize(mipmapHeight);
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
	 * @param height
	 * @return
	 */
	public static int calculateMipMapSize(final int currentValue) {
		if (currentValue > 1) return currentValue/2;
		return 1;
	}

	public static int getMipMapSizeAtIndex(int targetIndex, int currentValue) {
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
	
	public Dimension getMipMapDimension(int index) {
		return new Dimension(
				getMipMapWidth(index),
				getMipMapHeight(index));
	}
	
}
