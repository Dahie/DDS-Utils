package org.test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jogl.TEXImage;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


import org.ddsutil.DDSUtil;



public class TexTests {

	String pathname = "src/test/resources/raceca2_1.tex";
	
	@Test
	public void initFromFile() {
		File tex = new File(pathname);
		try {
			TEXImage texImage = org.jogl.TEXImage.read(tex);
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
	public void decompressToBufferedImage() {
//		File tex = new File(pathname);
//		try {
//			BufferedImage bi = DDSUtil.decompressTexture(tex);
//			assertEquals(512, bi.getWidth());
//			assertEquals(512, bi.getHeight());
//		} catch (IOException e) {
//			e.printStackTrace();
//			assertEquals("", e.toString());
//			assertFalse(true);
//		}
	}
	
}
