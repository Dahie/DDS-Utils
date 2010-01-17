/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;

import model.DDSFile;

import org.junit.Test;

import jogl.DDSImage;
import junit.framework.TestCase;
import util.Stopwatch;
import static org.junit.Assert.*;

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
			ddsimage.write(newFile);

			// test
			DDSFile newddsimage;
			newddsimage = new DDSFile(newFile);
			assertDDSImage(ddsimage, newddsimage);
		} catch (IOException e1) {
			e1.printStackTrace();
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

			ddsimage.setPixelformat(format);

			// write
			File newFile = new File(filename);
			ddsimage.write(newFile);


			// test
			DDSFile newddsimage = new DDSFile(newFile);
			ddsimage.loadImageData();

			assertEquals(ddsimage.getHeight(), newddsimage.getHeight());
			assertEquals(ddsimage.getWidth(), newddsimage.getWidth());
			assertEquals(true, newddsimage.isCompressed());
			assertEquals(format, newddsimage.getPixelformat());
		} catch (IOException e1) {
			e1.printStackTrace();
			assertEquals(true, false);
		}
	}

	private void buildnewDDSImageFile(int format, boolean hasMipmaps, String filename) {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		File newFile = new File(filename);
		DDSFile ddsimage;
		try {
			ddsimage = new DDSFile(textureDDS1024.getAbsolutePath());

			// Compress again
			if (format != -1) {
				// not original compression
				ddsimage.setPixelformat(format);
			}
			ddsimage.setHasMipMaps(hasMipmaps);

			DDSFile buildDDSimage = new DDSFile(new File(filename), ddsimage.getData(), format, hasMipmaps );

			stopwatch.stop();
			stopwatch.printMilliseconds("Time for test:");

			// test
			assertDDSImage(ddsimage, buildDDSimage);
			assertEquals(hasMipmaps, buildDDSimage.hasMipMaps());
			if(format != -1)
				assertEquals(format, buildDDSimage.getPixelformat());
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
			assertEquals(true, false);
		}
	}
}
