/**
 * 
 */
package org.model;

import io.github.memo33.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import org.ddsutil.ByteBufferedImage;


/**
 * TextureMap without MipMaps
 * @author danielsenff
 *
 */
public class SingleTextureMap extends AbstractTextureMap {

	BufferedImage bufferedImage;
	
	/**
	 * @param bi
	 */
	public SingleTextureMap(final BufferedImage bi) {
		super();
		this.bufferedImage = bi;
	}
	
	/**
	 * @return 
	 */
	public BufferedImage getData() {
		return this.bufferedImage;
	}
	
	/* (non-Javadoc)
	 * @see DDSUtil.AbstractMipMaps#getDXTCompressedBuffer(io.github.memo33.jsquish.Squish.CompressionType)
	 */
	@Override
	public ByteBuffer[] getDXTCompressedBuffer(final CompressionType compressionType) {
		ByteBuffer[] buffer = new ByteBuffer[1];
		buffer[0] = super.compress(bufferedImage, compressionType);
		return buffer;
	}

	

	/* (non-Javadoc)
	 * @see DDSUtil.AbstractMipMaps#getHeight()
	 */
	@Override
	public int getHeight() {
		return this.bufferedImage.getHeight();
	}

	/* (non-Javadoc)
	 * @see DDSUtil.AbstractMipMaps#getWidth()
	 */
	@Override
	public int getWidth() {
		return this.bufferedImage.getWidth();
	}

	/* (non-Javadoc)
	 * @see DDSUtil.AbstractTextureMap#getUncompressedBuffer()
	 */
	@Override
	public ByteBuffer[] getUncompressedBuffer() {
		ByteBuffer[] mipmapBuffer = new ByteBuffer[1];
		mipmapBuffer[0] = ByteBuffer.wrap(ByteBufferedImage.convertBIintoARGBArray(this.bufferedImage));
		return mipmapBuffer;
	}

}
