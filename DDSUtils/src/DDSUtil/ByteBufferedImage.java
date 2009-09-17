/**
 * 
 */
package DDSUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;




/**
 * @author danielsenff
 *
 */
public class ByteBufferedImage extends BufferedImage {

	/**
	 * @param width 
	 * @param height 
	 * @param type
	 */
	public ByteBufferedImage(int width, int height, int type) {
		super(width, height, type);
	}
	
	/**
	 * Creates a BufferedImage with 4byte ARGB.
	 * @param width
	 * @param height
	 * @param buffer
	 */
	public ByteBufferedImage(final int width, final int height, final Buffer buffer) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, buffer);
	}
	
	/**
	 * @param width
	 * @param height
	 * @param pixels
	 */
	public ByteBufferedImage(final int width, final int height, final int[] pixels) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, IntBuffer.wrap(pixels));
	}
	
	/**
	 * @param width
	 * @param height
	 * @param argb
	 */
	public ByteBufferedImage(final int width, final int height, final byte[] argb) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, ByteBuffer.wrap(argb));
	}

	private void initRaster(int width, int height, Buffer buffer) {
		WritableRaster wr = this.getRaster();
		byte[] rgba = new byte[buffer.capacity()];
		((ByteBuffer)buffer).get(rgba);
		wr.setDataElements(0,0, width, height, rgba);
	}
	
	/**
	 * @return
	 */
	public int[] getPixels() {
		return convertBIintoIntArray(this);
	}

	/**
	 * @return
	 */
	public IntBuffer getPixelBuffer() {
		return IntBuffer.wrap(getPixels());
	}
	
	/**
	 * @return
	 */
	public byte[] getARGBPixels(){
		return convertBIintoARGBArray(this);
	}

	/**
	 * Transfers the pixel-Information from a {@link BufferedImage} into a byte-array.
	 * If the {@link BufferedImage} is of different type, the pixels are reordered and stored in RGBA-order.
	 * @param bi
	 * @return array in order RGBA
	 */
	public static byte[] convertBIintoARGBArray(final BufferedImage bi) {
		
		WritableRaster r = bi.getRaster();
		SampleModel sampleModel = bi.getSampleModel();
	    DataBuffer db = r.getDataBuffer();
	    
	    /*bi.getColorModel();
	    Point location;
		int scanlineStride;
		int[] bankIndices;
		int[] bandOffsets;
		WritableRaster rARGB = WritableRaster.createBandedRaster(
	    		r.getDataBuffer(), 
	    		bi.getWidth(), bi.getHeight(), 
	    		scanlineStride, bankIndices, bandOffsets, location);*/ 
	    
		/*int[] bands = {0,1,2,3};
		SampleModel sModel2 = sampleModel.getSamples(0, 0, bi.getWidth(), bi.getHeight(), 4, bands, sampleModel.createDataBuffer());
		
		
		sampleModel.createSubsetSampleModel(bands)
		r.get*/
		
		
	    if (db instanceof DataBufferByte) {
	        DataBufferByte dbi = (DataBufferByte) db;
	        return dbi.getData();
	    }
		System.err.println("db is of type " + db.getClass());
		return null;
	}

	
	/**
	 * @param bi
	 * @return
	 */
	public static int[] convertBIintoIntArray(final BufferedImage bi) {
	    WritableRaster r = bi.getRaster();
	    DataBuffer db = r.getDataBuffer();
	    if (db instanceof DataBufferInt) {
	        DataBufferInt dbi = (DataBufferInt) db;
	        return dbi.getData();
	    }
		System.err.println("db is of type " + db.getClass());
		return null;
	}

	

}
