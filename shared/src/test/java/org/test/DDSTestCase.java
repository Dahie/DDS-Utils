/**
 * 
 */
package org.test;

import java.io.File;
import java.io.IOException;

import org.ddsutil.UnsupportedCompressionFormatException;
import org.jogl.DDSImage;

import org.model.DDSFile;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author danielsenff
 *
 */
public class DDSTestCase {

	String outputDirectory = "/output/";
	String inputDirectory = "src/test/resources/";
	
	protected File texturePNG2048 = new File(inputDirectory +"STRBahrain.png");
	protected File textureDDS1024 = new File(inputDirectory +"RAIKKONENEXTRA0.dds");
	protected File textureBMP512x512 = new File(inputDirectory +"512x512.bmp");
	protected File texturePNG64x64 = new File(inputDirectory + "SUAEXTRA3-1.png");
	protected File imageBMP3 = new File(inputDirectory +"3x3.bmp");
	protected File originalB1024 = new File(inputDirectory +"RAIKKONENEXTRA1.dds");
	protected File original2048 = new File(inputDirectory +"RAIKKONENEXTRA0.dds");
	
	protected File textureDDSrfmIcon = new File(inputDirectory +"TestingMODicon.dds");
	protected File textureDDSrfmSMIcon = new File(inputDirectory +"TestingMODSMicon.dds");
	
	public static DDSFile loadDDSFile(File file) {
		DDSFile ddsimage = null;
		try {
			ddsimage = new DDSFile(file);
			ddsimage.loadImageData();
		} catch (UnsupportedCompressionFormatException e) {
			e.printStackTrace();
		}
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
		assertEquals(ddsimage.getHeight(), newddsimage.getHeight());
		assertEquals(ddsimage.getWidth(), newddsimage.getWidth());
		assertEquals(ddsimage.isCompressed(), newddsimage.isCompressed());
		assertEquals(ddsimage.getPixelFormat(), newddsimage.getPixelFormat());
		assertEquals(ddsimage.hasMipMaps(), newddsimage.hasMipMaps());
		if (ddsimage.hasMipMaps() && newddsimage.hasMipMaps()) {
			assertEquals(ddsimage.getNumMipMaps(), newddsimage.getNumMipMaps());
		}
	}
	
	public void testSelf() {
		// just something to get the green bar :)
		assertTrue(true);
	}
}
