/**
 * 
 */
package org.model;

import io.github.memo33.jsquish.Squish;
import io.github.memo33.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;


import org.compression.DXTBufferCompressor;

import org.ddsutil.PixelFormats;
import org.ddsutil.UnsupportedCompressionFormatException;


/**
 * Abstract TextureMap
 * @author danielsenff
 *
 */
public abstract class AbstractTextureMap implements TextureMap {

	public AbstractTextureMap() {}
	
	@Override
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelFormat)
			throws UnsupportedCompressionFormatException {
		CompressionType compressionType = PixelFormats.getSquishCompressionFormat(pixelFormat);
		return this.getDXTCompressedBuffer(compressionType );
	}
	
	/**
	 * @param bufferedImage
	 * @param compressionType
	 * @return
	 */
	@Override
	public ByteBuffer compress(final BufferedImage bufferedImage,
			final Squish.CompressionType compressionType) {
		DXTBufferCompressor compressor = new DXTBufferCompressor(bufferedImage, compressionType);
		return compressor.getByteBuffer();
	}

}
