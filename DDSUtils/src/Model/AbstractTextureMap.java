/**
 * 
 */
package Model;

import gr.zdimensions.jsquish.Squish;
import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import Compression.DXTBufferCompressor;
import DDSUtil.DDSUtil;


/**
 * @author danielsenff
 *
 */
public abstract class AbstractTextureMap implements TextureMap{

	/**
	 * @param pixelformat
	 * @return
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelformat) {
		CompressionType compressionType = DDSUtil.getSquishCompressionFormat(pixelformat);
		//FIXME wtf doesn't this loop itself?
		return this.getDXTCompressedBuffer(compressionType );
	}
	
	/**
	 * @param bi
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer compress(final BufferedImage bi, final Squish.CompressionType compressionType) {
		DXTBufferCompressor compi = new DXTBufferCompressor(bi, compressionType);
		return compi.getByteBuffer();
	}
	
	
}
