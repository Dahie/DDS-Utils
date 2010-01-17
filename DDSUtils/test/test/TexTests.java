package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import jogl.TEXImage;

import org.junit.Test;

import ddsutil.DDSUtil;
import static org.junit.Assert.*;



public class TexTests {

	String pathname = "test/resource/raceca2_1.tex";
	
	@Test
	public void initFromFile() {
		File tex = new File(pathname);
		try {
			TEXImage texImage = jogl.TEXImage.read(tex);
			assertEquals(512, texImage.getWidth());
			assertEquals(512, texImage.getHeight());
			assertEquals(4, texImage.getNumMipMaps());
			assertEquals(TEXImage.D3DFMT_DXT5, texImage.getPixelFormat());
		} catch (IOException e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
	@Test
	public void decompress2BI() {
		
		File tex = new File(pathname);
		try {
			BufferedImage bi = DDSUtil.decompressTexture(tex);
			assertEquals(512, bi.getWidth());
			assertEquals(512, bi.getHeight());
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse(true);
		}
	}
	
}
