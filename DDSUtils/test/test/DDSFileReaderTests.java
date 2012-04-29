package test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.awt.image.BufferedImage;

import javax.activation.UnsupportedDataTypeException;

import jogl.DDSImage;
import model.DDSFile;

import org.junit.Test;

public class DDSFileReaderTests extends DDSTestCase{

	
	@Test
	public void openDebugrfmIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmIcon);
//		ddsimage.debugPrint();
		assertEquals("height",1024, ddsimage.getHeight());
		assertEquals("width", 1024, ddsimage.getWidth());
		assertEquals("is compressed", false, ddsimage.isCompressed());
		assertEquals("pixelformat", DDSImage.D3DFMT_X8R8G8B8, ddsimage.getPixelFormat());
		assertEquals("number of MipMaps", 0, ddsimage.getNumMipMaps());
	}
	
	@Test
	public void openDebugrfmSMIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmSMIcon);
//		ddsimage.debugPrint();
		assertEquals("height",512, ddsimage.getHeight());
		assertEquals("width", 512, ddsimage.getWidth());
		assertEquals("is compressed", false, ddsimage.isCompressed());
		assertEquals("pixelformat", DDSImage.D3DFMT_A8R8G8B8, ddsimage.getPixelFormat());
		assertEquals("pixel depth", 32, ddsimage.getDepth());
		assertEquals("number of MipMaps", 0, ddsimage.getNumMipMaps());
	}
	
	@Test
	public void openrrfmIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmIcon);
		DDSFile dds = new DDSFile(textureDDSrfmIcon, ddsimage);
		
		try {
			dds.loadImageData();
			BufferedImage bi = dds.getMipMap(0);
			
			assertEquals("height",ddsimage.getWidth(), bi.getHeight());
			assertEquals("width", ddsimage.getHeight(), bi.getWidth());
			
			
		} catch (UnsupportedDataTypeException e) {
			assertFalse("unsupported datatype", true);
			e.printStackTrace();
		}
		
	}
	
	@Test
	public void openrrfmSMIconDDS() {
		DDSImage ddsimage = loadDDSImage(this.textureDDSrfmSMIcon);
		DDSFile dds = new DDSFile(textureDDSrfmIcon, ddsimage);
		try {
			dds.loadImageData();
			
			BufferedImage bi = dds.getTopMipMap();
			
			assertEquals("height",ddsimage.getWidth(), bi.getHeight());
			assertEquals("width", ddsimage.getHeight(), bi.getWidth());
			
			
		} catch (UnsupportedDataTypeException e) {
			assertFalse("unsupported datatype", true);
			e.printStackTrace();
		}
	}
}
