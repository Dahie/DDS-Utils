/**
 * 
 */
package Compression;

import gr.zdimensions.jsquish.Squish;
import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;

import DDSUtil.ByteBufferedImage;
import DDSUtil.ImageOperations;

/**
 * @author danielsenff
 *
 */
public class DXTBufferCompressor {

//	byte[] compressedData;
	protected byte[] data;
	protected Dimension dimension;
	protected CompressionType compressionType;
	
	/**
	 * @param data Byte-Array should store ARGB
	 * @param width
	 * @param height
	 * @param compressionType
	 */
	public DXTBufferCompressor(final byte[] data, 
			final int width, 
			final int height, 
			final Squish.CompressionType compressionType) {
		this(data, new Dimension(width, height), compressionType);
	}
	
	/**
	 * @param byteBuffer ByteBuffer should store ARGB
	 * @param width
	 * @param height
	 * @param compressionType
	 */
	public DXTBufferCompressor(final ByteBuffer byteBuffer, 
			final int width, 
			final int height, 
			final Squish.CompressionType compressionType) {
		this(toByteArray(byteBuffer), 
				new Dimension(width, height), 
				compressionType);
	}
	
	
	/**
	 * @param image
	 * @param compressionType
	 */
	public DXTBufferCompressor(final Image image, 
			final Squish.CompressionType compressionType) {
		
		this(ByteBufferedImage.convertBIintoARGBArray((BufferedImage) image),
				new Dimension(image.getWidth(null), image.getHeight(null)),
				compressionType	);
	}

	/**
	 * @param data Byte-Array should store ARGB
	 * @param dimension
	 * @param compressionType
	 */
	public DXTBufferCompressor(final byte[] data, 
			final Dimension dimension, 
			final Squish.CompressionType compressionType) {
		this.data = data;
		this.dimension = dimension;
		this.compressionType = compressionType;
	}

	
	/**
	 * @return ByteBuffer
	 */
	public ByteBuffer getByteBuffer() {
		//TODO what if RGB not ARGB?
		byte[] compressedData;
		try {
			compressedData = squishCompressToArray(data, dimension.width, dimension.height, compressionType);
			return ByteBuffer.wrap(compressedData);
		} catch (DataFormatException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	/**
	 * Get the Byte-array hold by this object.
	 * @return
	 */
	public byte[] getArray() {
		try {
			return squishCompressToArray(data, dimension.width, dimension.height, compressionType);
		} catch (final DataFormatException e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * Compresses the RGBA-byte-array into a DXT-compressed {@link ByteBuffer}.
	 * @param rgba
	 * @param height
	 * @param width
	 * @param compressionType
	 * @return
	 */
	private static ByteBuffer squishCompress(final byte[] rgba, 
			final int width, final int height, final Squish.CompressionType compressionType) {
		
		int storageRequirements = Squish.getStorageRequirements(width, height, compressionType);
		 
		byte[] compressedData = Squish.compressImage(rgba, 
				width, 
				height, 
				new byte[storageRequirements], 
				compressionType, 
				Squish.CompressionMethod.CLUSTER_FIT);
		ByteBuffer buffer = ByteBuffer.wrap(compressedData);
		return buffer;
	}


	

	/**
	 * Compresses the RGBA-byte-array into a DXT-compressed byte-array.
	 * @param rgba Byte-Array needs to be in RGBA-order
	 * @param height
	 * @param width
	 * @param compressionType
	 * @return
	 * @throws DataFormatException 
	 */
	private static byte[] squishCompressToArray(final byte[] rgba, 
			final int width, 
			final int height, 
			final Squish.CompressionType compressionType) throws DataFormatException {
		
		// expected array length
		int length = width * height * 4;
		if (rgba.length != length) throw new DataFormatException("unexpected length:" + 
				rgba.length +  " instead of "+ length);
		
		int storageRequirements = Squish.getStorageRequirements(width, height, compressionType);
		
		return Squish.compressImage(rgba, 
				width, 
				height, 
				new byte[storageRequirements], 
				compressionType, 
				Squish.CompressionMethod.CLUSTER_FIT);
	} 
	
	
	
	/**
	 * Compresses a {@link ByteBuffer} into a DXT-compressed {@link ByteBuffer}
	 * @param buffer
	 * @param width
	 * @param height
	 * @param compressionType
	 * @return
	 */
	private static ByteBuffer squishCompress(final ByteBuffer bytebuffer, 
			final int width ,final int height, final Squish.CompressionType compressionType) {
		
		byte[] rgba = toByteArray(bytebuffer);
		return squishCompress(rgba, width, height, compressionType);
	}

	private static byte[] toByteArray(final ByteBuffer bytebuffer) {
		byte[] rgba = new byte[bytebuffer.capacity()];
		bytebuffer.get(rgba);
		return rgba;
	}
	
	public int getStorageRequirements() {
		return getStorageRequirements(dimension, compressionType);
		
	}
	
	/**
	 * Return the length of the required {@link ByteBuffer} for the image
	 * @param width
	 * @param height
	 * @param type
	 * @return
	 */
	public static int getStorageRequirements(final int width, final int height, final Squish.CompressionType type) {
		return Squish.getStorageRequirements(width, height, type);
	}
	
	/**
	 * Return the length of the required {@link ByteBuffer} for the image
	 * @param imageDimension
	 * @param type
	 * @return
	 */
	public static int getStorageRequirements(final Dimension imageDimension, final Squish.CompressionType type) {
		return Squish.getStorageRequirements((int)imageDimension.getWidth(), (int)imageDimension.getHeight(), type);
	}
	
}
