/**
 * 
 */
package org.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.IntBuffer;

import javax.imageio.ImageIO;

import org.ddsutil.ByteBufferedImage;
import org.ddsutil.ImageOperations;



import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author danielsenff
 *
 */
public class BufferedImageTests extends DDSTestCase {	
	
	@Test
	public void convertBI3x3intoByteArray() {
		int[] firstPixel = {255, 255, 0,0};
		assertBufferedImage(imageBMP3, firstPixel, 3);
	}
	
	@Test
	public void convertBISUAintoByteArray() {
		int[] firstPixel = {255, 0, 0,0};
		assertBufferedImage(texturePNG64x64, firstPixel, 4);
	}

	private void assertBufferedImage(File file, int[] firstPixel, final int expectedComponentCount) {
		BufferedImage image = null;
		try {
			image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		assertEquals(expectedComponentCount, image.getColorModel().getNumComponents());

		byte[] newPixels = ByteBufferedImage.convertBIintoARGBArray(image);

		assertEquals(image.getWidth()*image.getHeight()*4, newPixels.length);
		assertEquals(firstPixel[0], (newPixels[0] & 0xFF));
		assertEquals(firstPixel[1], (newPixels[1] & 0xFF));
		assertEquals(firstPixel[2], newPixels[2]);
		assertEquals(firstPixel[3], newPixels[3]);
	}
	
}
