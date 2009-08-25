/**
 * 
 */
package test;

import java.io.File;
import java.io.IOException;

import junit.framework.TestCase;
import Helper.Stopwatch;
import JOGL.DDSImage;
import Model.DDSImageFile;

/**
 * @author danielsenff
 *
 */
public class DDSImageFileCompressionTests extends DDSTestCase {

	
	
	public void testOriginal() {
		DDSImageFile ddsimage;
		try {
			ddsimage = new DDSImageFile(textureDDS1024);

			// write
			File newFile = new File(outputDirectory+"RAIKKONENEXTRA0_original.dds");
			ddsimage.write(newFile);


			// test
			DDSImageFile newddsimage;

			newddsimage = new DDSImageFile(newFile);
			assertDDSImage(ddsimage, newddsimage);
			ddsimage.close();
			newddsimage.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		System.gc();
	}
	
	public void testDXT5() {
		writeWithOriginalBuffer(DDSImage.D3DFMT_DXT5, outputDirectory+"RAIKKONENEXTRA0_dxt5resave.dds");
	}
	
	public void testDXT3() {
		writeWithOriginalBuffer(DDSImage.D3DFMT_DXT3, outputDirectory+"RAIKKONENEXTRA0_dxt3resave.dds");
	}
	
	public void testDXT1() {
		writeWithOriginalBuffer(DDSImage.D3DFMT_DXT1, outputDirectory+"RAIKKONENEXTRA0_dxt1resave.dds");
	}

	private void writeWithOriginalBuffer(final int format,  final String filename) {
		DDSImageFile ddsimage;
		try {
			ddsimage = new DDSImageFile(textureDDS1024);

			ddsimage.setPixelformat(format);

			// write
			File newFile = new File(filename);
			ddsimage.write(newFile);


			// test
			DDSImageFile newddsimage = new DDSImageFile(newFile);

			assertEquals(ddsimage.getHeight(), newddsimage.getHeight());
			assertEquals(ddsimage.getWidth(), newddsimage.getWidth());
			assertEquals(true, newddsimage.isCompressed());
			assertEquals(format, newddsimage.getPixelformat());


			ddsimage.close();
			newddsimage.close();
		} catch (IOException e1) {
			e1.printStackTrace();
			assertEquals(true, false);
		}
		System.gc();
	}
		public void testBuildDDSImageFile() {
//		buildnewDDSImageFile(DDSImage.D3DFMT_DXT5, true, 
//				directory + "RAIKKONENEXTRA0_buildDDSImageDXT5.dds");
	}
	
	private void buildnewDDSImageFile(int format, boolean canCreateMipmaps, String filename) {
		
		Stopwatch stopwatch = new Stopwatch();
		stopwatch.start();
		
		File newFile = new File(filename);
		DDSImageFile ddsimage;
		try {
			ddsimage = new DDSImageFile(textureDDS1024.getAbsolutePath());

			// Compress again
			if (format != -1) {
				// not original compression
				ddsimage.setPixelformat(format);
			}
			ddsimage.activateMipMaps(canCreateMipmaps);

			DDSImageFile buildDDSimage = new DDSImageFile(filename, ddsimage.getData(),format, canCreateMipmaps );


			stopwatch.stop();
			stopwatch.printMilliseconds("Time for test:");

			// test
			//DDSImageFile newddsimage = new DDSImageFile(newFile);
			assertDDSImage(ddsimage, buildDDSimage);
			assertEquals(canCreateMipmaps, buildDDSimage.hasMipMaps());
			if(format != -1)
				assertEquals(format, buildDDSimage.getPixelformat());
			

			ddsimage.close();
			buildDDSimage.close();
		} catch (IllegalArgumentException e2) {
			e2.printStackTrace();
			assertEquals(true, false);
		}
		System.gc();
	}


}
