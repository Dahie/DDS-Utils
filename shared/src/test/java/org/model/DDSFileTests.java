/**
 * 
 */
package org.model;

import java.io.File;
import java.io.IOException;

import org.jogl.DDSImage;

import org.model.DDSFile;

import org.junit.Test;

import org.test.DDSTestCase;

import static org.junit.Assert.*;

/**
 * @author danielsenff
 *
 */
public class DDSFileTests extends DDSTestCase {	

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
	public void testHasMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertTrue(ddsimage.hasMipMaps());
	}

	@Test
	public void testNumMipMap() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		
		try {
			int numMipMaps = DDSImage.read(textureDDS1024).getNumMipMaps();
			assertEquals(numMipMaps, ddsimage.getNumMipMaps());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testEqualsItself() throws Exception {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		
		assertTrue(ddsimage.equals(ddsimage));
	}

	public void testEqualsSamefile() throws Exception {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		DDSFile ddsimage2 = loadDDSFile(textureDDS1024);
		
		assertTrue(ddsimage.equals(ddsimage2));
	}

	public void testNotEqualsOtherFile() throws Exception {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		DDSFile ddsimage3 = loadDDSFile(originalB1024);
		
		assertEquals(false, ddsimage.equals(ddsimage3));
	}
	

}
