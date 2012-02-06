package model;

import gr.zdimensions.jsquish.Squish;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.activation.UnsupportedDataTypeException;

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
	 * Width of the topmost MipMap.
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
	 * All contained MipMaps as {@link ByteBuffer}
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer[] getUncompressedBuffer();
	
	/**
	 * Returns a ByteBuffer for each MipMap.
	 * @param pixelformat
	 * @return
	 * @throws UnsupportedDataTypeException 
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelformat) 
			throws UnsupportedDataTypeException;
	
	/**
	 * Compress a single {@link BufferedImage} into a ByteBuffer.
	 * @param bi
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer compress(final BufferedImage bi, 
			final Squish.CompressionType compressionType);

}
