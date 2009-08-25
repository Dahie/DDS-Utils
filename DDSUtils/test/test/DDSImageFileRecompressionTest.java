/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;

import Helper.Stopwatch;
import JOGL.DDSImage;
import Model.DDSImageFile;

/**
 * @author danielsenff
 *
 */
public class DDSImageFileRecompressionTest extends DDSTestCase {


	public void testRecompressOriginal() {
		writeRecompressedData(-1, false, 
				outputDirectory + "RAIKKONENEXTRA0_originalrecomp.dds");
	}
	
	public void testRecompressDXT5noMipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT5, false, 
				outputDirectory + "RAIKKONENEXTRA0_dxt5recompnomipmap.dds");
	}
	
	public void testRecompressDXT1noMipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT5, false, 
				outputDirectory + "RAIKKONENEXTRA0_dxt1recompnomipmap.dds");
	}
	
	public void testUncompressedNoMipmap() {	
		writeRecompressedData(DDSImage.D3DFMT_A8R8G8B8, false, 
				outputDirectory + "RAIKKONENEXTRA0_uncompressednomipmap.dds");
	}
	
	public void testRecompressDXT1Mipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT1, true, 
				outputDirectory + "RAIKKONENEXTRA0_dxt1recompmipmap.dds");
	}
	
	public void testRecompressDXT5Mipmap() {
		writeRecompressedData(DDSImage.D3DFMT_DXT5, true, 
				outputDirectory + "RAIKKONENEXTRA0_dxt5recompmipmap.dds");
	}
	
	public void testUncompressedMipmap() {
		writeRecompressedData(DDSImage.D3DFMT_A8R8G8B8, true, 
				outputDirectory + "RAIKKONENEXTRA0_uncompressedmipmap.dds");
	}

	private void writeRecompressedData(int format, boolean generateMipMaps, String filename) {
		
		File newFile = new File(filename);

		 DDSImageFile ddsimage = new DDSImageFile(textureDDS1024);

		// set new compression
		if (format != -1) {
			// not original compression
			ddsimage.setPixelformat(format);
		}
		System.out.println("create MipMaps:" + generateMipMaps);
		ddsimage.activateMipMaps(generateMipMaps);

		try {
			ddsimage.write(newFile);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.out.println("image written to disc");

		// test
		DDSImageFile newddsimage = null;
		newddsimage = new DDSImageFile(newFile);			
		assertDDSImage(ddsimage, newddsimage);
		assertEquals("has activated mipmaps",generateMipMaps, newddsimage.hasMipMaps());
		if(format != -1)
			assertEquals(format, newddsimage.getPixelformat());


		ddsimage.close();
		newddsimage.close();

		System.gc();
	}
	

	
	
}
