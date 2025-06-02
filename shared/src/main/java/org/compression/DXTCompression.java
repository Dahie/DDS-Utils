/**
 * 
 */
package org.compression;

import io.github.memo33.jsquish.Squish;

import java.awt.Dimension;
import java.nio.ByteBuffer;


/**
 * @author danielsenff
 *
 */
public class DXTCompression {

	/**
	 * Return the length of the required {@link ByteBuffer} for the image
	 * @param width
	 * @param height
	 * @param compressionType
	 * @return
	 */
	public static int getStorageRequirements(final int width, final int height, final Squish.CompressionType compressionType) {
		return Squish.getStorageRequirements(width, height, compressionType);
	}
	
	/**
	 * Return the length of the required {@link ByteBuffer} for the image
	 * @param imageDimension
	 * @param compressionType
	 * @return
	 */
	public static int getStorageRequirements(final Dimension imageDimension, final Squish.CompressionType compressionType) {
		return Squish.getStorageRequirements((int)imageDimension.getWidth(), (int)imageDimension.getHeight(), compressionType);
	}
	
	
	


	
	

	/**
	 * Compresses the RGBA-byte-array into a DXT-compressed byte-array.
	 * @param rgbaArray
	 * @param height
	 * @param width
	 * @param compressionType
	 * @return
	 */
	public static byte[] squishCompressToArray(final byte[] rgbaArray,
			final int width ,final int height, 
			final Squish.CompressionType compressionType) {
		
		int storageRequirements = Squish.getStorageRequirements(width, height, compressionType);
		
		return Squish.compressImage(rgbaArray,
				width, 
				height, 
				new byte[storageRequirements], 
				compressionType, 
				Squish.CompressionMethod.CLUSTER_FIT);
	} 
	
	/**
	 * Compresses the RGBA-byte-array into a DXT-compressed {@link ByteBuffer}.
	 * @param rgbaArray
	 * @param height
	 * @param width
	 * @param compressionType
	 * @return
	 */
	public static ByteBuffer squishCompress(final byte[] rgbaArray,
			final int width ,final int height, final Squish.CompressionType compressionType) {
		
		int storageRequirements = Squish.getStorageRequirements(width, height, compressionType);
		ByteBuffer buffer = ByteBuffer.allocateDirect(storageRequirements); 
		buffer.put(Squish.compressImage(rgbaArray,
				width, 
				height, 
				new byte[storageRequirements], 
				compressionType, 
				Squish.CompressionMethod.CLUSTER_FIT));
		buffer.rewind();
		return buffer;
	}
	
	/**
	 * @param bytebuffer
	 * @param width
	 * @param height
	 * @param compressionType
	 * @return
	 */
	public static ByteBuffer squishCompress(final ByteBuffer bytebuffer, 
			final int width, final int height, final Squish.CompressionType compressionType) {
		byte[] rgbaArray = new byte[bytebuffer.capacity()];
		bytebuffer.get(rgbaArray);
		return squishCompress(rgbaArray, width, height, compressionType);
	}
	
}
