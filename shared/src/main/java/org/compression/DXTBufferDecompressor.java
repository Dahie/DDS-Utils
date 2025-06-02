/**
 * 
 */
package org.compression;

import io.github.memo33.jsquish.Squish;
import io.github.memo33.jsquish.Squish.CompressionType;

import java.awt.Dimension;
import java.nio.ByteBuffer;


/**
 * Decompressor for DXT-Compression
 * @author danielsenff
 *
 */
public class DXTBufferDecompressor extends BufferDecompressor{

	protected CompressionType compressionType;
	

	/**
	 * @param compressedBuffer
	 * @param width
	 * @param height
	 * @param compressionType
	 */
	public DXTBufferDecompressor(final ByteBuffer compressedBuffer, 
			final int width, final int height, CompressionType compressionType) {
		this(compressedBuffer, new Dimension(width, height), compressionType);
	}
	
	
	/**
	 * @param compressedData
	 * @param width
	 * @param height
	 * @param compressionType
	 */
	public DXTBufferDecompressor(byte[] compressedData, int width, int height,
			CompressionType compressionType) {
		this(ByteBuffer.wrap(compressedData), new Dimension(width, height), compressionType);
	}
	
	/**
	 * @param compressedBuffer
	 * @param dimension
	 * @param compressionType
	 */
	public DXTBufferDecompressor(final ByteBuffer compressedBuffer, 
			final Dimension dimension, final CompressionType compressionType) {
		this.uncompressedBuffer = 
			squishDecompressBuffer(compressedBuffer, dimension.width, dimension.height, compressionType);
		this.dimension = dimension;
		this.compressionType = compressionType;
	}


	/**
	 * Compresses a Byte-Array into a DXT-compressed {@link ByteBuffer}
	 * If the type is null, it returns the uncompressed ByteBuffer.
	 * 
	 * Decompresses a DXT-compressed Byte-Array and returns a byte-Array.
	 * If the {@link CompressionType} is null, it return the source data.
	 * @param compressedData
	 * @param width
	 * @param height
	 * @param type
	 * @return byte[]
	 * @throws OutOfMemoryError
	 */
	public static byte[] squishDecompressToArray(final byte[] compressedData, final int width, final int height,
			final Squish.CompressionType type) throws OutOfMemoryError {

		//Use JSquish to decompress images. Then bind as normal. 
		if (type != null) {
			byte[] decompressedData = Squish.decompressImage(null, width, height, compressedData, type);
			return decompressedData;
		}
		
		return compressedData;
		
	} 
	

	
	/**
	 * Decompresses a DXT-compressed Byte-Array and returns a ByteBuffer
	 * If the {@link CompressionType} is null, it return the source data
	 * @param compressedData
	 * @param width
	 * @param height
	 * @param type
	 * @return
	 * @throws OutOfMemoryError
	 */
	public static ByteBuffer squishDecompress(final byte[] compressedData, final int width, final int height,
			final Squish.CompressionType type) throws OutOfMemoryError {
		
		return ByteBuffer.wrap(squishDecompressToArray(compressedData, width, height, type));
	} 
	
	
	
	/**
	 * Decompresses a DXT-compressed {@link ByteBuffer} 
	 * @param byteBuffer
	 * @param width
	 * @param height
	 * @param compressionType
	 * @return
	 * @throws OutOfMemoryError
	 */
	private static ByteBuffer squishDecompressBuffer(final ByteBuffer byteBuffer, 
			final int width, final int height,
			final Squish.CompressionType compressionType) throws OutOfMemoryError {
		
		byte[] data = new byte[byteBuffer.capacity()]; 
		byteBuffer.get(data); 
		 
		return squishDecompress(data, width, height, compressionType);
	} 
	
}
