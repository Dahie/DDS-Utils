package DDSUtil;

import gr.zdimensions.jsquish.Squish;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileLockInterruptionException;

import util.ImageUtils;

import Compression.DXTBufferCompressor;
import Compression.DXTBufferDecompressor;
import JOGL.DDSImage;
import Model.MipMaps;
import Model.SingleTextureMap;
import Model.TextureMap;



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
	 * Create a {@link BufferedImage} from a DXT-compressed 
	 * dds-texture {@link FileLockInterruptionException}.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static BufferedImage decompressTexture(final File file) throws IOException {
		return decompressTexture(DDSImage.read(file));
	}
	
	/**
	 * Create a {@link BufferedImage} from a DXT-compressed {@link DDSImage}
	 * @param ddsimage
	 * @return
	 */
	public static BufferedImage decompressTexture (final DDSImage ddsimage) {

		return decompressTexture(
				ddsimage.getMipMap(0).getData(), 
				ddsimage.getWidth(), 
				ddsimage.getHeight(), 
				findCompressionFormat(ddsimage));
	}
	
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
	 * Create a {@link BufferedImage} from a DXT-compressed ByteBuffer.
	 * @param textureBuffer
	 * @param width
	 * @param height
	 * @param compressionType
	 * @return
	 */
	public static BufferedImage decompressTexture(final ByteBuffer textureBuffer, 
			final int width, 
			final int height, 
			final Squish.CompressionType compressionType) {
		
		return new DXTBufferDecompressor(textureBuffer, width, height, compressionType).getImage();
	}
	
	/**
	 * Create a {@link BufferedImage} from a DXT-compressed ByteBuffer.
	 * @param textureBuffer
	 * @param width
	 * @param height
	 * @param pixelformat
	 * @return
	 */
	public static BufferedImage decompressTexture(final ByteBuffer textureBuffer, 
			final int width, 
			final int height, 
			final int pixelformat) {
	
		Squish.CompressionType compressionType = getSquishCompressionFormat(pixelformat); 
		return new DXTBufferDecompressor(textureBuffer, width, height, compressionType).getImage();
	}
	
	/**
	 * Compresses a {@link BufferedImage} into a {@link ByteBuffer}
	 * @param image
	 * @param compressionType
	 * @return
	 */
	public static ByteBuffer compressTexture(final Image image, 
			final Squish.CompressionType compressionType) {
		
		return new DXTBufferCompressor(image, compressionType).getByteBuffer();
	}
	
	/**
	 * @param image
	 * @param compressionType
	 * @return
	 */
	public static byte[] compressTextureToArray(final Image image, 
			final Squish.CompressionType compressionType) {

		return new DXTBufferCompressor(image, compressionType).getArray();
	}
	
	




	
	/**
	 * Writes a DDS-Image to disc.
	 * @param destinationfile 
	 * @param sourceImage 
	 * @param pixelformat 
	 * @param generateMipMaps 
	 * @throws IOException 
	 * 
	 */
	public static void write(final File destinationfile, 
			BufferedImage sourceImage, 
			final int pixelformat,
			boolean generateMipMaps) throws IOException {
		
		int width = sourceImage.getWidth();
		int height = sourceImage.getHeight();
		
		//convert RGB to RGBA image
		if(!sourceImage.getColorModel().hasAlpha()) {
			sourceImage = ImageUtils.convert(sourceImage, BufferedImage.TYPE_4BYTE_ABGR);
		}
		
		TextureMap maps;
		if (generateMipMaps) {
			maps = new MipMaps();
			((MipMaps)maps).generateMipMaps(sourceImage);
		} else {
			maps = new SingleTextureMap(sourceImage);
		}
		
		ByteBuffer[] mipmapBuffer = null;
		if (isDXTCompressed(pixelformat)) {
			mipmapBuffer = maps.getDXTCompressedBuffer(pixelformat);
		} else {
			mipmapBuffer = maps.getUncompressedBuffer();
		}
		
		writeDDSImage(destinationfile, mipmapBuffer, width, height, pixelformat);
	}
	
	
	
	/**
	 * TODO: what is with DXT1?
	 * @param file 
	 * @param map 
	 * @param pixelformat 
	 * @throws IOException 
	 * 
	 */
	public void write(final File file, 
			TextureMap map, 
			final int pixelformat) throws IOException {
		
		writeDDSImage(file, map.getDXTCompressedBuffer(pixelformat), 
				map.getWidth(), 
				map.getHeight(), 
				pixelformat);
	}
	
	

	private static DDSImage writeDDSImage(final File file,
			ByteBuffer[] mipmapBuffer, 
			final int width, 
			final int height,
			final int pixelformat) throws IllegalArgumentException, IOException {
		
		DDSImage writedds = DDSImage.createFromData(pixelformat, width, height, mipmapBuffer);
//		writedds.debugPrint();
		writedds.write(file);
		return writedds;
	}
	
	
	
	
	/**
	 * Returns true if the pixelformat is compressed a kind of DXTn-Compression
	 * TODO The {@link DDSImage} specifies isCompressed even on D3DFMT_A8R8G8B8, D3DFMT_R8G8B8 and D3DFMT_X8R8G8B8
	 * this doesn't
	 * @param pixelformat DDSImage pixelformat
	 * @return boolean is compressed
	 */
	public static boolean isDXTCompressed(final int pixelformat) {
		
		switch(pixelformat) {
			default:
			case DDSImage.D3DFMT_A8R8G8B8:
			case DDSImage.D3DFMT_R8G8B8:
			case DDSImage.D3DFMT_X8R8G8B8:
				return false;
			case DDSImage.D3DFMT_DXT1:
			case DDSImage.D3DFMT_DXT2:
			case DDSImage.D3DFMT_DXT3:
			case DDSImage.D3DFMT_DXT4:
			case DDSImage.D3DFMT_DXT5:
				return true;			
		}
	}
	
	
	/**
	 * Returns the PixelFormat of a {@link File}
	 * @param file
	 * @return
	 * @throws IOException
	 */
	public static int getCompressionType(final File file) throws IOException {
		return DDSImage.read(file).getPixelFormat();	
	}
	
	private static Squish.CompressionType findCompressionFormat(DDSImage ddsimage) {
		int pixelFormat = ddsimage.getPixelFormat();
		return getSquishCompressionFormat(pixelFormat);
	}

	/**
	 * 
	 * @param pixelFormat DDSImage pixelformat
	 * @return
	 */
	public static Squish.CompressionType getSquishCompressionFormat(final int pixelFormat) {
		Squish.CompressionType type = null;
		switch(pixelFormat) { 
		case DDSImage.D3DFMT_DXT1: 
			type = Squish.CompressionType.DXT1;
			break; 
		case DDSImage.D3DFMT_DXT3: 
			type = Squish.CompressionType.DXT3;
			break; 
		case DDSImage.D3DFMT_DXT5: 
			type = Squish.CompressionType.DXT5;
			break; 
		}
		return type;
	}

	
	/**
	 * Convert Integer-CompressionType of {@link DDSImage} to {@link Squish}-Enum
	 * @param compressionType int
	 * @return Squish.CompressionType
	 */
	public static Squish.CompressionType selectedCompression(final int compressionType) {
		
		// TODO maybe do as hasmap?
//		 Hashtable numbers = new Hashtable();
//	     numbers.put("one", new Integer(1));
//	     numbers.put("two", new Integer(2));
//	     numbers.put("three", new Integer(3));
	 
		
		switch(compressionType) {
			default:
			case DDSImage.D3DFMT_A8R8G8B8:
				return null;
			case DDSImage.D3DFMT_DXT1:
				return Squish.CompressionType.DXT1;
			case DDSImage.D3DFMT_DXT3:
				return Squish.CompressionType.DXT3;
			case DDSImage.D3DFMT_DXT5:
				return Squish.CompressionType.DXT5;
			case DDSImage.D3DFMT_R8G8B8:
				return null;
			}
	}

	
}
