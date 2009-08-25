/**
 * 
 */
package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import DDSUtil.DDSUtil;
import DDSUtil.MipMapsUtil;
import DDSUtil.NonCubicDimensionException;
import Helper.Stopwatch;
import JOGL.DDSImage;
import Model.DDSImageFile;

/**
 * @author danielsenff
 *
 */
public class WriteNonSquareImages extends DDSTestCase {

	
	public void test512x512() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, true, inputDirectory +"512x512.bmp", outputDirectory + "test_nonsquare_512x512.dds");
	}
	
	public void test512x256() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, true, inputDirectory +"512x256.bmp", outputDirectory + "test_nonsquare_512x256.dds");
	}
	
	public void test512x512noMipMap() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, false, inputDirectory +"512x512.bmp", outputDirectory + "test_nonsquare_512x512noMipMap.dds");
	}
	
	public void test512x256noMipMap() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, false, inputDirectory +"512x256.bmp", outputDirectory + "test_nonsquare_512x256noMipMap.dds");
	}
	
	public void test512x240() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, true, inputDirectory +"512x240.bmp", outputDirectory + "test_nonsquare_512x240.dds");
	}
	
	public void test512x240noMipMap() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, false, inputDirectory +"512x240.bmp", outputDirectory + "test_nonsquare_512x240noMipMap.dds");
	}
	
	public void test423x240() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, true, inputDirectory +"423x240.bmp", outputDirectory + "test_nonsquare_523x240.dds");
	}
	
	public void test423x240noMipMap() {
		saveNonSquareImages(DDSImage.D3DFMT_DXT5, false, inputDirectory +"423x240.bmp", outputDirectory + "test_nonsquare_523x240noMipMap.dds");
	}
	
	private void saveNonSquareImages(int format, boolean canCreateMipmaps, String input, String output) {
		
		File newFile = new File(output);
		try {
			BufferedImage bi = ImageIO.read(new File(input));

			DDSUtil.write(newFile, bi,format, canCreateMipmaps );

	
			// test
			DDSImageFile newddsimage = new DDSImageFile(newFile);
			
			assertEquals(canCreateMipmaps, newddsimage.hasMipMaps());
			if(format != -1) // original or not?
				assertEquals(format, newddsimage.getPixelformat());
			assertEquals(bi.getHeight(), newddsimage.getHeight());
			assertEquals(bi.getWidth(), newddsimage.getWidth());
			assertEquals(format, newddsimage.getPixelformat());
			assertEquals(canCreateMipmaps, newddsimage.hasMipMaps());
			if (canCreateMipmaps) {
				assertEquals("mipmap count",MipMapsUtil.calculateMaxNumberOfMipMaps(bi.getWidth(), bi.getHeight()), newddsimage.getNumMipMaps());
			}

		} catch (IOException e1) {
			e1.printStackTrace();
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
			assertEquals("illegal argument, as wrong image dimensions", true, true);
		} 
		System.gc();
	}
	
	
}
