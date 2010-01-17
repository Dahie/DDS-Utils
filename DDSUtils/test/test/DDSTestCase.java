/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;

import jogl.DDSImage;

import model.DDSFile;

import static org.junit.Assert.*;

/**
 * @author danielsenff
 *
 */
public class DDSTestCase {

	String outputDirectory = "test/output/";
	String inputDirectory = "test/resource/";
	
	File texturePNG2048 = new File(inputDirectory +"STRBahrain.png");
	protected File textureDDS1024 = new File(inputDirectory +"RAIKKONENEXTRA0.dds");
	protected File textureBMP512x512 = new File(inputDirectory +"512x512.bmp");
	File texturePNG64x64 = new File(inputDirectory + "SUAEXTRA3-1.png");
	File imageBMP3 = new File(inputDirectory +"3x3.bmp");
	protected File originalB1024 = new File(inputDirectory +"RAIKKONENEXTRA1.dds");
	File original2048 = new File(inputDirectory +"RAIKKONENEXTRA0.dds");
	
	
	public static DDSFile loadDDSFile(File file) {
		DDSFile ddsimage = null;
		ddsimage = new DDSFile(file);
		return ddsimage;
	}
	
	public static DDSImage loadDDSImage(File file) {
		DDSImage ddsimage = null;
		try {
			ddsimage = DDSImage.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ddsimage;
	}
	
	public static void assertDDSImage(final DDSFile ddsimage, final DDSFile newddsimage) {
		assertEquals("height",ddsimage.getHeight(), newddsimage.getHeight());
		assertEquals("width", ddsimage.getWidth(), newddsimage.getWidth());
		assertEquals("is compressed", ddsimage.isCompressed(), newddsimage.isCompressed());
		assertEquals("pixelformat", ddsimage.getPixelformat(), newddsimage.getPixelformat());
		assertEquals("has MipMaps", ddsimage.hasMipMaps(), newddsimage.hasMipMaps());
		if (ddsimage.hasMipMaps() && newddsimage.hasMipMaps()) {
			assertEquals("number of MipMaps", ddsimage.getNumMipMaps(), newddsimage.getNumMipMaps());
		}
	}
	
	public void testSelf() {
		assertEquals("just something to get the green bar :)", true, true);
	}
}
