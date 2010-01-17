/**
 * 
 */
package model;

import gr.zdimensions.jsquish.Squish;
import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import compression.DXTBufferCompressor;
import ddsutil.DDSUtil;



/**
 * Abstract TextureMap
 * @author danielsenff
 *
 */
public abstract class AbstractTextureMap implements TextureMap {

	
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelformat) {
		CompressionType compressionType = DDSUtil.getSquishCompressionFormat(pixelformat);
		return this.getDXTCompressedBuffer(compressionType );
	}
	
	/**
	 * @param bi
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer compress(final BufferedImage bi, 
			final Squish.CompressionType compressionType) {
		DXTBufferCompressor compi = new DXTBufferCompressor(bi, compressionType);
		return compi.getByteBuffer();
	}
	
	
}
