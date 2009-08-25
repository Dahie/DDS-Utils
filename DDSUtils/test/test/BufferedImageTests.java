/**
 * 
 */
package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import DDSUtil.ByteBufferedImage;
import DDSUtil.ImageOperations;

/**
 * @author danielsenff
 *
 */
public class BufferedImageTests extends DDSTestCase {

	
	public void testCreateBIfromRGBIntBuffer() {
		int height = 2, width = 3;
		int[] pixels = new int[6];
		for (int i = 0; i < pixels.length; i++) 
		{
			pixels[i] = ImageOperations.writePixelRGB(0, 0, 88);
			assertEquals("color value as integer", -16777128, pixels[i]);
		}
		createBIfromIntBuffer(height, width, pixels);
	}
	
	public void testCreateBIfromARGBIntBuffer() {
		int height = 2, width = 3;
		int[] pixels = new int[6];
		for (int i = 0; i < pixels.length; i++) 
		{
			pixels[i] = ImageOperations.writePixelARGB(255, 0, 0, 88);
			assertEquals("color value as integer", -16777128, pixels[i]);
		}
		
		createBIfromIntBuffer(height, width, pixels);
	}
	

	private void createBIfromIntBuffer(int height, int width, int[] pixels) {
		IntBuffer intBuffer = IntBuffer.wrap(pixels);
		for (int i = 0; i < pixels.length; i++) {
			assertEquals("color value as integer", -16777128, intBuffer.get(i));
		}
		
//		BufferedImage image = BIUtil.createBufferedImage(intBuffer, width, height);
		ByteBufferedImage image = new ByteBufferedImage(width, height, intBuffer);
		
		
		assertEquals("width", width, image.getWidth());
		assertEquals("height", height, image.getHeight());
		assertEquals("image type", image.getType(), BufferedImage.TYPE_4BYTE_ABGR);
		byte[] newPixels = image.getARGBPixels();
		for (int i = 0; i < pixels.length; i++) 
		{
			int[] color = ImageOperations.readPixelARGB(pixels[i]);
			assertEquals("color value as integer", -16777128, pixels[i]);
			
			for (int j = 0; j < 4; j++) {
				int pos = (i * 4) + j;
				assertEquals("channel value at "+i+", "+j, color[j], (newPixels[pos] & 0xFF));
			}
		}
	}
	
	public void testCreateBIfromRGBByteBuffer() {
		int height = 2, width = 3;
		byte[] pixels = new byte[height * width * 4];
		for (int i = 0; i < pixels.length; i=i+4) {
			pixels[i] = (byte) 88;
			pixels[i+1] = 0;
			pixels[i+2] = 0;
			pixels[i+3] = (byte) 255;
		}
		
		createBIfromByteBuffer(height, width, pixels);
	}
	
	public void testCreateBIfromARGBByteBuffer() {
		int height = 2, width = 3;
		byte[] pixels = new byte[height * width * 4];
		for (int i = 0; i < pixels.length; i=i+4) 
		{
			pixels[i] = (byte) 88; //r
			pixels[i+1] = 0; //g
			pixels[i+2] = 0; //b
			pixels[i+3] = (byte) 255;
		}
		
		createBIfromByteBuffer(height, width, pixels);
	}
	
	private void createBIfromByteBuffer(int height, int width, byte[] pixels) {
//		BufferedImage image = BIUtil.createBufferedImage(ByteBuffer.wrap(pixels), width, height);
		ByteBufferedImage image = new ByteBufferedImage(width, height, pixels);
		
		assertEquals("width", width, image.getWidth());
		assertEquals("height", height, image.getHeight());
		byte[] newPixels = image.getARGBPixels();
		for (int i = 0; i < pixels.length; i++) 
		{
			assertEquals("channel value at " + i, pixels[i], newPixels[i]);
		}
	}
	
	
	public void testARGBOrder_4BYTE_ABGR() {
		BufferedImage bi = new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR);
		int[] pixel = { 28, 55, 128, 240};
		bi.setRGB(0, 0, ImageOperations.writePixelRGB(pixel));
		
//		for (int i = 0; i < pixels.length; i++) 
		{
//			assertEquals("channel value", pixels[i], newPixels[i]);
		}
	} 
	
	
	public void testConvertBI3x3intoByteArray() {
		int[] firstPixel = {255, 255, 0,0};
		assertBI(imageBMP3, firstPixel);
	}
	
	public void testConvertBISUAintoByteArray() {
		int[] firstPixel = {255, 0, 0,0};
		assertBI(texturePNG64x64, firstPixel);
	}

	private void assertBI(File file, int[] firstPixel) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] newPixels = ByteBufferedImage.convertBIintoARGBArray(image);
		
		assertEquals("length", image.getWidth()*image.getHeight()*4, newPixels.length);
		assertEquals("first pixel a", firstPixel[0], (newPixels[0] & 0xFF));
		assertEquals("first pixel r", firstPixel[1], (newPixels[1] & 0xFF));
		assertEquals("first pixel g", firstPixel[2], newPixels[2]);
		assertEquals("first pixel b", firstPixel[3], newPixels[3]);
	}
	
}
