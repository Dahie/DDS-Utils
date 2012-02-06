/**
 * 
 */
package model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import jogl.DDSImage;

import model.DDSFile;

import org.junit.Test;

import ddsutil.MipMapsUtil;

import test.DDSTestCase;
import util.Stopwatch;

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
		assertEquals(DDSImage.D3DFMT_DXT5, ddsimage.getPixelformat());
	}
	
	@Test
	public void testActivateMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals("activated mipmaps beforehand", true, ddsimage.hasMipMaps());
		ddsimage.setHasMipMaps(true);
		assertEquals("activated mipmaps", true, ddsimage.hasMipMaps());
		ddsimage.setHasMipMaps(false);
		assertEquals("deactivated mipmaps", false, ddsimage.hasMipMaps());
	}
	
	@Test
	public void testHasMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals("has MipMaps", true, ddsimage.hasMipMaps());
	}

	@Test
	public void testNumMipMap() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		
		try {
			int numMipMaps = DDSImage.read(textureDDS1024).getNumMipMaps();
			assertEquals("Number of MipMaps from original:",numMipMaps, ddsimage.getNumMipMaps());
			assertEquals("calculated number of MipMaps", numMipMaps, MipMapsUtil.calculateMaxNumberOfMipMaps(ddsimage.getWidth(), ddsimage.getHeight()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testDecompressBufferedImage() {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		BufferedImage originalImage = ddsimage.getData();
		
		stopwatch.stop();
		stopwatch.printMilliseconds("Time fo producing BufferedImage: ");
		
		assertEquals(1024, originalImage.getHeight());
		assertEquals(1024, originalImage.getWidth());
		assertEquals(BufferedImage.TYPE_4BYTE_ABGR, originalImage.getType());
		assertEquals("File objects are identical", textureDDS1024.getAbsolutePath(), ddsimage.getAbsolutePath());
	}
	/*
	@Test
	public void testGetAllMipMapBuffer() {
		DDSImageFile ddsimage = DDSImageFileTests.loadDDSImage(original1024);
		assertMipMapBuffer(ddsimage.generateAllMipMapBuffer());
	}*/
	@Test
	public void testMipMapBuffer() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals("has mipmaps", true, ddsimage.hasMipMaps());
		assertEquals("numMipMaps", 11, ddsimage.getNumMipMaps());
//		assertMipMapBuffer(MipMapsUtil.compressMipMaps(ddsimage.getWidth(), 
//				ddsimage.getHeight(), ddsimage.generateAllMipMapBuffer(), Squish.CompressionType.DXT5));
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
		assertEquals(true, DDSFile.isPowerOfTwo(2048));
		assertEquals(true, DDSFile.isPowerOfTwo(1024));
		assertEquals(true, DDSFile.isPowerOfTwo(512));
		
		assertEquals(false, DDSFile.isPowerOfTwo(612));
		assertEquals(false, DDSFile.isPowerOfTwo(33));
		assertEquals(false, DDSFile.isPowerOfTwo(78));
	}
}
