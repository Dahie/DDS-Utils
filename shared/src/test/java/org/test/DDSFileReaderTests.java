package org.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.awt.image.BufferedImage;

import org.ddsutil.UnsupportedCompressionFormatException;
import org.jogl.DDSImage;
import org.model.DDSFile;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


public class DDSFileReaderTests extends DDSTestCase{

	
	@Test
	public void openDebugrfmIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmIcon);
		assertEquals(1024, ddsimage.getHeight());
		assertEquals(1024, ddsimage.getWidth());
		assertEquals(false, ddsimage.isCompressed());
		assertEquals(DDSImage.D3DFMT_X8R8G8B8, ddsimage.getPixelFormat());
		assertEquals(0, ddsimage.getNumMipMaps());
	}
	
	@Test
	public void openDebugrfmSMIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmSMIcon);
		assertEquals(512, ddsimage.getHeight());
		assertEquals(512, ddsimage.getWidth());
		assertFalse(ddsimage.isCompressed());
		assertEquals(DDSImage.D3DFMT_A8R8G8B8, ddsimage.getPixelFormat());
		assertEquals(32, ddsimage.getDepth());
		assertEquals(0, ddsimage.getNumMipMaps());
	}
	
	@Test
	public void openrrfmIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmIcon);
		DDSFile dds = new DDSFile(textureDDSrfmIcon, ddsimage);
		
		try {
			dds.loadImageData();
			BufferedImage bufferedImage = dds.getMipMap(0);
			
			assertEquals(ddsimage.getWidth(), bufferedImage.getHeight());
			assertEquals(ddsimage.getHeight(), bufferedImage.getWidth());
			
		} catch (UnsupportedCompressionFormatException e) {
			assertFalse(true);
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void openrrfmSMIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmSMIcon);
		DDSFile dds = new DDSFile(textureDDSrfmIcon, ddsimage);
		try {
			dds.loadImageData();
			
			BufferedImage bufferedImage = dds.getTopMipMap();
			
			assertEquals(ddsimage.getWidth(), bufferedImage.getHeight());
			assertEquals(ddsimage.getHeight(), bufferedImage.getWidth());
			
			
		} catch (UnsupportedCompressionFormatException e) {
			assertFalse(true);
			e.printStackTrace();
		}
	}
}
