/**
 * 
 */
package org.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import org.jogl.DDSImage;

import org.junit.Test;

import org.ddsutil.MipMapsUtil;

import org.test.DDSTestCase;
import org.util.Stopwatch;

/**
 * @author danielsenff
 *
 */
public class DDSImageFileTests extends DDSTestCase {
	
	@Test
	public void testFourCC() throws FileNotFoundException, IOException {
		assertTrue("is a dds image", DDSImage.isDDSImage(new FileInputStream(textureDDS1024)));
	}

	@Test
	public void testDimensions() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals(1024, ddsimage.getHeight());
		assertEquals(1024, ddsimage.getWidth());
	}
	
	@Test
	public void testIsDXT5() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals(DDSImage.D3DFMT_DXT5, ddsimage.getPixelFormat());
	}
	
	@Test
	public void testActivateMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		ddsimage.setHasMipMaps(true);
		assertTrue(ddsimage.hasMipMaps());
		ddsimage.setHasMipMaps(false);
		assertEquals(false, ddsimage.hasMipMaps());
	}

		@Test
	public void testDectivateMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		ddsimage.setHasMipMaps(false);
		assertEquals(false, ddsimage.hasMipMaps());
	}
	
	@Test
	public void testHasMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertTrue(ddsimage.hasMipMaps());
	}

	@Test
	public void testNumMipMap() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		
		try {
			int numMipMaps = DDSImage.read(textureDDS1024).getNumMipMaps();
			assertEquals(numMipMaps, MipMapsUtil.calculateMaxNumberOfMipMaps(ddsimage.getWidth(), ddsimage.getHeight()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


	@Test
	public void testDecompressBufferedImage() {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		BufferedImage originalImage = ddsimage.getBufferedImage();
		
		stopwatch.stop();
		stopwatch.printMilliseconds("Time fo producing BufferedImage: ");
		
		assertEquals(1024, originalImage.getHeight());
		assertEquals(1024, originalImage.getWidth());
		assertEquals(BufferedImage.TYPE_4BYTE_ABGR, originalImage.getType());
	}

	@Test
	public void testDecompressBufferedImageFilesIdentical() {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		BufferedImage originalImage = ddsimage.getBufferedImage();
		
		stopwatch.stop();
		stopwatch.printMilliseconds("Time fo producing BufferedImage: ");
		
		assertEquals(textureDDS1024.getAbsolutePath(), ddsimage.getAbsolutePath());
	}

	@Test
	public void testMipMapBuffeHasNumberOfMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertTrue(ddsimage.hasMipMaps());
		assertEquals(11, ddsimage.getNumMipMaps());
	}
	
	private void assertMipMapBuffer(ByteBuffer[] buffer) {
	    int curSize = buffer[0].remaining();
	    for (int i = 0; i < buffer.length; i++) {
	      int remaining = buffer[i].remaining();
	      System.out.println("MipMapGen-Level: " + i + " remaining: " + remaining + " expected " + curSize);
	      
	      if (curSize>16) {
	    	  // squished, 16 is min blocksize
	    	  assertEquals(curSize, remaining);
	    	  curSize /= 4;  
	      }
	      
	    }
	}
	
	@Test
	public void testIsPowerOfTwo() {
		assertTrue(DDSFile.isPowerOfTwo(2048));
		assertTrue(DDSFile.isPowerOfTwo(1024));
		assertTrue(DDSFile.isPowerOfTwo(512));
		
		assertEquals(false, DDSFile.isPowerOfTwo(612));
		assertEquals(false, DDSFile.isPowerOfTwo(33));
		assertEquals(false, DDSFile.isPowerOfTwo(78));
	}
}
