/**
 * 
 */
package test;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import junit.framework.TestCase;
import DDSUtil.ImageOperations;
import DDSUtil.MipMaps;
import DDSUtil.MipMapsUtil;

/**
 * @author danielsenff
 *
 */
public class MipMapTests extends DDSTestCase {

	public void testNumMipMaps() {
		Dimension dimension = new Dimension(1024, 1024);
		int numMipMaps = MipMapsUtil.calculateMaxNumberOfMipMaps(dimension);
		
		assertEquals("number of mipmaps in 1024x1024 image", 11, numMipMaps);
	}
	
	public void testMipMapGeneration() {
		
		BufferedImage image = null;
		try {
			
			image = ImageIO.read(textureBMP512x512);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MipMaps mipmaps = new MipMaps(image);
//		for (BufferedImage map : mipmaps)
		for (int i = 0; i < mipmaps.getNumMipMaps(); i++) 
		{
			BufferedImage map = mipmaps.getMipMap(i);
//			ImagePlus imp = new ImagePlus("DDSImage", map);
//			imp.show();
			assertEquals("height", MipMaps.getMipMapSizeAtIndex(i, image.getWidth()), map.getHeight());
			assertEquals("width", MipMaps.getMipMapSizeAtIndex(i, image.getHeight()), map.getWidth());
		}
	}
	
	public void testMipMapTargetSize() {
		int currentValue = 1024;
		int targetIndex = 3;
		int newValue = MipMaps.getMipMapSizeAtIndex(targetIndex, currentValue);
		assertEquals("value at MipMap-index", 128, newValue);
	}
	
	public void testMipMapIteration() {
		BufferedImage image = null;
		try {
			image = ImageIO.read(textureBMP512x512);
		} catch (IOException e) {
			e.printStackTrace();
		}
		MipMaps mipmaps = new MipMaps(image);
		int count = 1;
		for (BufferedImage bufferedImage : mipmaps) {
			count++;
			assertEquals("BufferedImage exists", true, (bufferedImage != null));
		}
		assertEquals("number of MipMaps",mipmaps.getNumMipMaps(), count);
	}
	
}
