/**
 * 
 */
package org.model;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.test.DDSTestCase;
import org.ddsutil.MipMapsUtil;


/**
 * @author danielsenff
 *
 */
public class MipMapTests extends DDSTestCase {

	@Test
	public void testNumMipMaps() {
		Dimension dimension = new Dimension(1024, 1024);
		int numMipMaps = MipMapsUtil.calculateMaxNumberOfMipMaps(dimension);
		
		assertEquals(11, numMipMaps);
	}
	
	@Test
	public void testMipMapGeneration() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(textureBMP512x512);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MipMaps mipmaps = new MipMaps();
		mipmaps.generateMipMaps(image);
		for (int i = 0; i < mipmaps.getNumMipMaps(); i++) 
		{
			BufferedImage map = mipmaps.getMipMap(i);
			assertEquals(MipMaps.getMipMapSizeAtIndex(i, image.getWidth()), map.getHeight());
			assertEquals(MipMaps.getMipMapSizeAtIndex(i, image.getHeight()), map.getWidth());
		}
	}
	
	@Test
	public void testMipMapTargetSizeAtIndex() {
		int currentValue = 1024;
		int targetIndex = 3;
		int newValue = MipMaps.getMipMapSizeAtIndex(targetIndex, currentValue);
		assertEquals(128, newValue);
	}
	
	@Test
	public void testMipMapIteration() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(textureBMP512x512);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MipMaps mipmaps = new MipMaps();
		mipmaps.generateMipMaps(image);
		int count = 1;
		for (BufferedImage bufferedImage : mipmaps) {
			count++;
			assertTrue((bufferedImage != null));
		}
		assertEquals(mipmaps.getNumMipMaps(), count);
	}
	
}
