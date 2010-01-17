/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;

import jogl.DDSImage;

import model.DDSFile;

import org.junit.Test;
import static org.junit.Assert.*;


/**
 * @author danielsenff
 *
 */
public class DDSImageFileRecompressionTest extends DDSTestCase {

	@Test
	public void testRecompressOriginal() {
		writeRecompressedData(-1, false, 
				outputDirectory + "RAIKKONENEXTRA0_originalrecomp.dds");
	}
	
	@Test
	public void testRecompressDXT5noMipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT5, false, 
				outputDirectory + "RAIKKONENEXTRA0_dxt5recompnomipmap.dds");
	}
	
	@Test
	public void testRecompressDXT1noMipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT5, false, 
				outputDirectory + "RAIKKONENEXTRA0_dxt1recompnomipmap.dds");
	}
	
	@Test
	public void testUncompressedNoMipmap() {	
		writeRecompressedData(DDSImage.D3DFMT_A8R8G8B8, false, 
				outputDirectory + "RAIKKONENEXTRA0_uncompressednomipmap.dds");
	}
	
	@Test
	public void testRecompressDXT1Mipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT1, true, 
				outputDirectory + "RAIKKONENEXTRA0_dxt1recompmipmap.dds");
	}
	
	@Test
	public void testRecompressDXT5Mipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT5, true, 
				outputDirectory + "RAIKKONENEXTRA0_dxt5recompmipmap.dds");
	}
	
	@Test
	public void testUncompressedMipmap() {
		writeRecompressedData(DDSImage.D3DFMT_A8R8G8B8, true, 
				outputDirectory + "RAIKKONENEXTRA0_uncompressedmipmap.dds");
	}

	private void writeRecompressedData(int format, boolean generateMipMaps, String filename) {
		
		File newFile = new File(filename);

		 DDSFile ddsimage = new DDSFile(textureDDS1024);
		 ddsimage.loadImageData();

		// set new compression
		if (format != -1) {
			// not original compression
			ddsimage.setPixelformat(format);
		}
		System.out.println("create MipMaps:" + generateMipMaps);
		ddsimage.setHasMipMaps(generateMipMaps);

		try {
			ddsimage.write(newFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("image written to disc");

		// test
		DDSFile newddsimage = null;
		newddsimage = new DDSFile(newFile);			
		assertDDSImage(ddsimage, newddsimage);
		assertEquals("has activated mipmaps",generateMipMaps, newddsimage.hasMipMaps());
		if(format != -1)
			assertEquals(format, newddsimage.getPixelformat());
	}
	

	
	
}
