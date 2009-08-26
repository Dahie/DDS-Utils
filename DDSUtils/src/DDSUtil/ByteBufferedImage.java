/**
 * 
 */
package DDSUtil;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.DataBufferByte;
import java.awt.image.DataBufferInt;
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
	 * @param arg0
	 * @param arg1
	 * @param arg2
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
	
	public ByteBufferedImage(final int width, final int height, final int[] pixels) {
		super(width, height, BufferedImage.TYPE_4BYTE_ABGR);
		initRaster(width, height, IntBuffer.wrap(pixels));
	}
	
	public ByteBufferedImage(int width, int height, byte[] argb) {
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
	
	public byte[] getARGBPixels(){

		byte[] argb = convertBIintoARGBArray(this);
		return argb;
	}

	/**
	 * Transfers the pixel-Information from a {@link BufferedImage} into a byte-array.
	 * If the {@link BufferedImage} is of different type, the pixels are reordered and stored in RGBA-order.
	 * TODO this eats performance ... FIXME need simple way to convert bi to data-array
	 * @param bi
	 * @return array in order RGBA
	 */
	public static byte[] convertBIintoARGBArray(final BufferedImage bi) {

		DataBuffer dataBuffer = bi.getRaster().getDataBuffer();
		
		// read channel count
		/*int componentCount = bi.getColorModel().getNumComponents() ;
		
		int length = bi.getWidth() * bi.getHeight() * 4;
		byte[] argb = new byte[length];
		
		int r, g, b, a;
		int count = 0;
		for (int i = 0; i < length; i=i+4) {
			// databuffer has unsigned integers, they must be converted to signed byte 
			// original order from BufferedImage
			
			if(componentCount > 3) {
				// 32bit image
				b =  (dataBuffer.getElem(i) );
				g =  (dataBuffer.getElem(i+1));
				r =  (dataBuffer.getElem(i+2));
				a =  (dataBuffer.getElem(i+3));
				
				argb[i] = (byte) (a & 0xFF);
				argb[i+1] = (byte) (r & 0xFF);
				argb[i+2] = (byte) (g & 0xFF);
				argb[i+3] = (byte) (b & 0xFF);
			} else {
				//24bit image
				b =  (dataBuffer.getElem(count) );
				count++;
				g =  (dataBuffer.getElem(count));
				count++;
				r =  (dataBuffer.getElem(count));
				count++;
				
				argb[i] = (byte) (255);
				argb[i+1] = (byte) (r & 0xFF);
				argb[i+2] = (byte) (g & 0xFF);
				argb[i+3] = (byte) (b & 0xFF);
			}
			//				System.out.println(argb[i] + " " + argb[i+1] + " " + argb[i+2] + " " + argb[i+3]);
		}
		// aim should be RGBA order
		return argb;*/
		
		WritableRaster r = bi.getRaster();
	    DataBuffer db = r.getDataBuffer();
	    if (db instanceof DataBufferByte) {
	        DataBufferByte dbi = (DataBufferByte) db;
	        return dbi.getData();
	    }
		System.err.println("db is of type " + db.getClass());
		return null;
	}

	
	public static int[] convertBIintoIntArray(BufferedImage bi) {
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
