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


import org.util.Stopwatch;

/**
 * @author danielsenff
 *
 */
public class DDSImageFileCompressionTests extends DDSTestCase {

	
	@Test
	public void testOriginal() {
		DDSFile ddsimage;
		try {
			ddsimage = new DDSFile(textureDDS1024);
			ddsimage.loadImageData();

			// write
			File newFile = new File(outputDirectory+"RAIKKONENEXTRA0_original.dds");
			//ddsimage.write(newFile);

			// test
			DDSFile newddsimage = new DDSFile(newFile);
			assertDDSImage(ddsimage, newddsimage);
		} catch (UnsupportedCompressionFormatException e) {
            throw new RuntimeException(e);
        }
    }

	@Test
	public void testDXT5() {
		writeWithOriginalBuffer(DDSImage.D3DFMT_DXT5, outputDirectory+"RAIKKONENEXTRA0_dxt5resave.dds");
	}
	
	@Test
	public void testDXT3() {
		writeWithOriginalBuffer(DDSImage.D3DFMT_DXT3, outputDirectory+"RAIKKONENEXTRA0_dxt3resave.dds");
	}
	
	@Test
	public void testDXT1() {
		writeWithOriginalBuffer(DDSImage.D3DFMT_DXT1, outputDirectory+"RAIKKONENEXTRA0_dxt1resave.dds");
	}

	@Test
	private void writeWithOriginalBuffer(final int format,  final String filename) {
		DDSFile ddsimage;
		try {
			ddsimage = new DDSFile(textureDDS1024);
            ddsimage.loadImageData();

            ddsimage.setPixelFormat(format);

			// write
			File newFile = new File(filename);
//			ddsimage.write(newFile);


			// test
			DDSFile newddsimage = new DDSFile(newFile);
            ddsimage.loadImageData();

            assertEquals(ddsimage.getHeight(), newddsimage.getHeight());
			assertEquals(ddsimage.getWidth(), newddsimage.getWidth());
			assertTrue(newddsimage.isCompressed());
			assertEquals(format, newddsimage.getPixelFormat());
		} catch (UnsupportedCompressionFormatException e) {
			throw new RuntimeException(e);
		}
	}

	private void buildnewDDSImageFile(int format, boolean hasMipmaps, String filename) {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		File newFile = new File(filename);
		DDSFile ddsimage;
		try {
			ddsimage = new DDSFile(textureDDS1024);

			// Compress again
			if (format != -1) {
				// not original compression
				ddsimage.setPixelFormat(format);
			}
			ddsimage.setHasMipMaps(hasMipmaps);

			DDSFile buildDDSimage = new DDSFile(new File(filename), ddsimage.getBufferedImage(), format, hasMipmaps );

			stopwatch.stop();
			stopwatch.printMilliseconds("Time for test:");

			// test
			assertDDSImage(ddsimage, buildDDSimage);
			assertEquals(hasMipmaps, buildDDSimage.hasMipMaps());
			if(format != -1)
				assertEquals(format, buildDDSimage.getPixelFormat());
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
			assertTrue(false);
		}
	}
}
