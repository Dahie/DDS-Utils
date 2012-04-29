/**
 * 
 */
package model;

import gr.zdimensions.jsquish.Squish;
import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import javax.activation.UnsupportedDataTypeException;

import compression.DXTBufferCompressor;

import ddsutil.PixelFormats;



/**
 * Abstract TextureMap
 * @author danielsenff
 *
 */
public abstract class AbstractTextureMap implements TextureMap {

	public AbstractTextureMap() {}
	
	@Override
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelformat) 
			throws UnsupportedDataTypeException {
		CompressionType compressionType = PixelFormats.getSquishCompressionFormat(pixelformat);
		return this.getDXTCompressedBuffer(compressionType );
	}
	
	/**
	 * @param bi
	 * @param compressionType
	 * @return
	 */
	@Override
	public ByteBuffer compress(final BufferedImage bi, 
			final Squish.CompressionType compressionType) {
		DXTBufferCompressor compi = new DXTBufferCompressor(bi, compressionType);
		return compi.getByteBuffer();
	}

}
