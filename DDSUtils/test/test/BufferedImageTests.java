/**
 * 
 */
package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.junit.Test;

import ddsutil.ByteBufferedImage;
import ddsutil.ImageOperations;


import static org.junit.Assert.*;

/**
 * @author danielsenff
 *
 */
public class BufferedImageTests extends DDSTestCase {	
	
	@Test
	public void convertBI3x3intoByteArray() {
		int[] firstPixel = {255, 255, 0,0};
		assertBI(imageBMP3, firstPixel, 3);
	}
	
	@Test
	public void convertBISUAintoByteArray() {
		int[] firstPixel = {255, 0, 0,0};
		assertBI(texturePNG64x64, firstPixel, 4);
	}

	private void assertBI(File file, int[] firstPixel, final int expectedComponentCount) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals("component count", expectedComponentCount, image.getColorModel().getNumComponents());
		
		
		byte[] newPixels = ByteBufferedImage.convertBIintoARGBArray(image);
		
		
		assertEquals("length", image.getWidth()*image.getHeight()*4, newPixels.length);
		assertEquals("first pixel a", firstPixel[0], (newPixels[0] & 0xFF));
		assertEquals("first pixel r", firstPixel[1], (newPixels[1] & 0xFF));
		assertEquals("first pixel g", firstPixel[2], newPixels[2]);
		assertEquals("first pixel b", firstPixel[3], newPixels[3]);
	}
	
}
