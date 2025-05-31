package org.model;

import io.github.memo33.jsquish.Squish;
import org.ddsutil.UnsupportedCompressionFormatException;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;


/**
 * Interface for TextureMaps.
 * @author danielsenff
 *
 */
public interface TextureMap {


	/**
	 * Height of the topmost MipMap.
	 * @return
	 */
	public int getHeight();
	
	/**
	 * Width of the top-most MipMap.
	 * @return
	 */
	public int getWidth();
	
	
	/**
	 * All contained MipMaps compressed with DXT in {@link ByteBuffer}
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final Squish.CompressionType compressionType);
	
	/**
	 * All contained MipMap as {@link ByteBuffer}
	 * @return
	 */
	public ByteBuffer[] getUncompressedBuffer();
	
	/**
	 * Returns a ByteBuffer for each MipMap.
	 * @param pixelFormat
	 * @return
	 * @throws UnsupportedCompressionFormatException
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelFormat)
			throws UnsupportedCompressionFormatException;
	
	/**
	 * Compress a single {@link BufferedImage} into a ByteBuffer.
	 * @param bufferedImage
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer compress(final BufferedImage bufferedImage,
			final Squish.CompressionType compressionType);

}
