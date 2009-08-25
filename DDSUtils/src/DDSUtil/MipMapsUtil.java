/**
 * 
 */
package DDSUtil;

import gr.zdimensions.jsquish.Squish;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import Model.MipMaps;

/**
 * @author danielsenff
 *
 */
public class MipMapsUtil {

	/**
	 * Topmost MipMap Index 
	 */
	public static final int TOP_MOST_MIP_MAP = 0;

	/**
	 * Squish each mipmap
	 * @param mipMapWidth
	 * @param mipMapHeight
	 * @param mipmapBuffer
	 * @param compressionType
	 * @return
	 */
	private static ByteBuffer[] compressMipMaps(int mipMapWidth, int mipMapHeight,
			final ByteBuffer[] mipmapBuffer, final Squish.CompressionType compressionType) {

		byte[] rgba;
		int i = 0;
		
		//  Squish each MipMap
		while(true) {

			rgba = new byte[mipmapBuffer[i].capacity()];
			mipmapBuffer[i].get(rgba);
			mipmapBuffer[i].clear();

			/*System.out.println("MimMap-Level: " + i + " Width: " +  mipMapWidth + 
					" Height: " + mipMapHeight + " Size: " + (mipMapHeight*mipMapWidth));
			System.out.println("Remaining: " + mipmapdata[i].remaining() + " Limit: " + mipmapdata[i].limit());*/
			
			if ( (mipMapWidth == 1 || mipMapHeight == 1) )
				break;
			
			// values for next run
			i++;
			mipMapHeight = MipMaps.calculateMipMapSize(mipMapHeight);
			mipMapWidth = MipMaps.calculateMipMapSize(mipMapWidth);
		}
		return mipmapBuffer;
	}
	
	
	
	
	/**
	 * Generates all MipMaps of a Texture and stores them as {@link BufferedImage} in an Array.
	 * @param topmost
	 * @param mipmapWidth
	 * @param mipmapHeight
	 * @param hasMipMaps
	 * @return BufferedImages[]
	 */
	protected static BufferedImage[] generateBIMipMaps(BufferedImage topmost, 
			int mipmapWidth, int mipmapHeight, boolean hasMipMaps) {
		
		BufferedImage[] mipmapBI;
		
		if (hasMipMaps && MipMapsUtil.isPowerOfTwo(topmost.getWidth()) && 
				MipMapsUtil.isPowerOfTwo(topmost.getHeight())) {
			
			//get maximum number of mipmaps to create
			int maxNumberOfMipMaps = MipMapsUtil.calculateMaxNumberOfMipMaps(mipmapWidth, mipmapHeight);
			// new array storing all mipmaps
			mipmapBI = new BufferedImage[maxNumberOfMipMaps];
	
			mipmapBI = MipMaps.generateMipMaps(topmost, mipmapWidth, mipmapHeight, mipmapBI);
			
		} else { // doesn't have mipmaps
			mipmapBI = new BufferedImage[1];
			mipmapBI[0] = topmost;
		}
		return mipmapBI;
	}


	Class abstractRescaler;

	/**
	 * The BufferedImage is the images, from which everything is created.
	 * It generates an {@link BufferedImage}-Array which is filled as a side-effect.
	 * It returns an array of ByteBuffer for all MipMaps
	 * @param bi
	 * @param mipmapBI
	 * @param generateMipMaps
	 * @return
	 */
	private static java.nio.ByteBuffer[] getAllMipMapsByteBuffer(final BufferedImage bi, 
			BufferedImage[] mipmapBI, 
			final boolean generateMipMaps)
	{
		ByteBuffer[] mipmapBuffer;
		if(generateMipMaps) 
		{
			// newly generate MipMaps
			mipmapBI = generateBIMipMaps(bi, bi.getWidth(), bi.getHeight(), generateMipMaps);
			int numMipMaps = mipmapBI.length;
			
			mipmapBuffer = new ByteBuffer[numMipMaps];
			for (int i = 0; i < numMipMaps; i++) {
				mipmapBuffer[i] = ByteBuffer.wrap(ByteBufferedImage.convertBIintoARGBArray(mipmapBI[i]));
			}
		} 
		else 
		{
			mipmapBuffer = new ByteBuffer[1];
			mipmapBuffer[0] = ByteBuffer.wrap(ByteBufferedImage.convertBIintoARGBArray(bi));
		}
		return mipmapBuffer;
	}



	/**
	 * Number of MipMaps that will be generated from this image sizes.
	 * @param width
	 * @param height
	 * @return
	 */
	public static int calculateMaxNumberOfMipMaps(final int width, final int height) {
		return ((int) Math.floor(Math.log(Math.max(width, height)) / Math.log(2.0)))+1; // plus original
	}




	/**
	 * Number of MipMaps that will be generated from this image dimension.
	 * @param dimension
	 * @return
	 */
	public static int calculateMaxNumberOfMipMaps(final Dimension dimension) {
		int width = dimension.width;
		int height = dimension.height;
		return ((int) Math.floor(Math.log(Math.max(width, height)) / Math.log(2.0)))+1; // plus original
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
