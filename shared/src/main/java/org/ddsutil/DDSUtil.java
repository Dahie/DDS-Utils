package org.ddsutil;


import io.github.memo33.jsquish.Squish;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;


import org.jogl.DDSImage;
import org.model.TextureMap;
import org.util.FileUtil;
import org.util.ImageUtils;

import org.compression.DXTBufferCompressor;
import org.compression.DXTBufferDecompressor;



/**
 * Easy loading, saving and manipulation of DDSImages and DXT-Compression.
 * 
 * @author danielsenff
 *
 */
public class DDSUtil {

	/**
	 * Topmost MipMap Index 
	 */
	public static final int TOP_MOST_MIP_MAP = 0;

	private DDSUtil() {	}

	/**
	 * Create a {@link BufferedImage} from a DXT-compressed Byte-array.
	 * @param compressedData
	 * @param width
	 * @param height
	 * @param compressionType
	 * @return
	 */
	public static BufferedImage decompressTexture(final byte[] compressedData, 
			final int width, 
			final int height, 
			final Squish.CompressionType compressionType) {
		return new DXTBufferDecompressor(compressedData, width, height, compressionType).getImage();
	}



	/**
	 * Compresses a {@link BufferedImage} into a {@link ByteBuffer}
	 * @param image
	 * @param compressionType
	 * @return
	 */
	public static ByteBuffer compressTexture(final BufferedImage image, 
			final Squish.CompressionType compressionType) {
		return new DXTBufferCompressor(image, compressionType).getByteBuffer();
	}
	
	/**
	 * @param image
	 * @param compressionType
	 * @return
	 */
	public static byte[] compressTextureToArray(final BufferedImage image, 
			final Squish.CompressionType compressionType) {
		return new DXTBufferCompressor(image, compressionType).getArray();
	}
	
	/**
	 * Writes a DDS-Image file to disk.
	 * @param destinationfile 
	 * @param sourceImage 
	 * @param pixelFormat
	 * @param generateMipMaps 
	 * @throws IOException 
	 * 
	 */
	@Deprecated
	public static void write(final File destinationfile, 
			BufferedImage sourceImage, 
			final int pixelFormat,
			boolean generateMipMaps) throws IOException {

		//writeDDSImage(destinationfile, mipmapBuffer, width, height, pixelFormat);
	}
	
	/**
	 * TODO: what about DXT1?
	 * @param file 
	 * @param map 
	 * @param pixelFormat
	 * @throws IOException 
	 * 
	 */
	public void write(final File file, 
			final TextureMap map, 
			final int pixelFormat) throws IOException {
        try {
            writeDDSImage(file, map.getDXTCompressedBuffer(pixelFormat),
                    map.getWidth(),
                    map.getHeight(),
                    pixelFormat);
        } catch (UnsupportedCompressionFormatException e) {
            throw new RuntimeException(e);
        }
    }

	@Deprecated
	private static DDSImage writeDDSImage(final File file,
			final ByteBuffer[] mipmapBuffer, 
			final int width, 
			final int height,
			final int pixelFormat) throws IllegalArgumentException, IOException {

		return null;
	}
	


	/**
	 * Returns true for file formats supported by this library.
	 * Currently TEX and DDS reading is supported.
	 * @param file
	 * @return
	 */
	public static boolean isReadSupported(final File file) {
		String fileSuffix = FileUtil.getFileSuffix(file);
		return fileSuffix.endsWith("dds") || fileSuffix.endsWith("tex");
	}
}
