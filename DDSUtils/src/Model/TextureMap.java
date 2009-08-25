package Model;

import gr.zdimensions.jsquish.Squish;
import gr.zdimensions.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;

import Compression.DXTBufferCompressor;
import DDSUtil.DDSUtil;

/**
 * Interface for TextureMaps.
 * @author danielsenff
 *
 */
public interface TextureMap {


	/**
	 * @return
	 */
	public abstract int getHeight();
	
	/**
	 * @return
	 */
	public abstract int getWidth();
	
	
	/**
	 * All contained MipMaps compressed with DXT in {@link ByteBuffer}
	 * @param compressionType
	 * @return
	 */
	public abstract ByteBuffer[] getDXTCompressedBuffer(final Squish.CompressionType compressionType);
	
	/**
	 * All contained MipMaps as {@link ByteBuffer}
	 * @param compressionType
	 * @return
	 */
	public abstract ByteBuffer[] getUncompressedBuffer();
	
	/**
	 * @param pixelformat
	 * @return
	 */
	public ByteBuffer[] getDXTCompressedBuffer(final int pixelformat);
	
	/**
	 * @param bi
	 * @param compressionType
	 * @return
	 */
	public ByteBuffer compress(final BufferedImage bi, final Squish.CompressionType compressionType); 
}
