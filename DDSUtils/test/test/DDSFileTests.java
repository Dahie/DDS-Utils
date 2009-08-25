/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;

import JOGL.DDSImage;
import Model.DDSFile;

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
	
	public void testIsDXT5() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals(DDSImage.D3DFMT_DXT5, ddsimage.getPixelformat());
	}
	
	public void testHasMipMaps() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		assertEquals(true, ddsimage.hasMipMaps());
	}

	public void testNumMipMap() {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		
		try {
			int numMipMaps = DDSImage.read(textureDDS1024).getNumMipMaps();
			assertEquals(numMipMaps, ddsimage.getNumMipMaps());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void testEquals() throws Exception {
		DDSFile ddsimage = loadDDSFile(textureDDS1024);
		DDSFile ddsimage2 = loadDDSFile(textureDDS1024);
		DDSFile ddsimage3 = loadDDSFile(originalB1024);
		
		assertEquals("object equals itself", true, ddsimage.equals(ddsimage));
		assertEquals("object equals same file", true, ddsimage.equals(ddsimage2));
		assertEquals("object equals different object", false, ddsimage.equals(ddsimage3));
	}
	

}
