/**
 * 
 */
package test;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import jogl.DDSImage;

import model.DDSFile;

import org.junit.Test;

import ddsutil.DDSUtil;
import ddsutil.MipMapsUtil;


import static org.junit.Assert.*;

/**
 * @author danielsenff
 *
 */
public class TextureSaving extends DDSTestCase {

	@Test
	public void testWriteBI2File_DXT5MipMaps() {
		try {
			System.out.println("start reading...");
			BufferedImage bi = ImageIO.read(texturePNG2048);
			File file = new File(outputDirectory + "test_WriteBI2File_DXT5MipMaps.dds");
			if(file.exists()) file.delete();
			
			System.out.println("start writing...");
			writeFile(bi, file, true, DDSImage.D3DFMT_DXT5);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWriteBI2DDSFile_DXT5MipMaps() {
		try {
			System.out.println("start reading...");
			BufferedImage bi = ImageIO.read(texturePNG2048);
			File file = new File(outputDirectory + "test_WriteBI2DDSFile_DXT5MipMaps.dds");
			if(file.exists()) file.delete();
			
			DDSFile imagefile = new DDSFile(file, bi, DDSImage.D3DFMT_DXT5, true);
			
			System.out.println("start writing...");
			imagefile.write();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testWriteBI2DDSFileSetData_DXT5MipMaps() {
		try {
			System.out.println("start reading...");
			BufferedImage bi = ImageIO.read(texturePNG2048);
			File file = new File(outputDirectory + "test_WriteBI2DDSFileSetData_DXT5MipMaps.dds");
			if(file.exists()) file.delete();
			
			DDSFile imagefile = new DDSFile(file, bi, DDSImage.D3DFMT_DXT5, true);
			imagefile.setData(bi);
			
			System.out.println("start writing...");
			imagefile.write();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testWriteBI2File_DXT5NoMipMaps() {
		try {
			System.out.println("start reading...");
			BufferedImage bi = ImageIO.read(texturePNG2048);
			File file = new File(outputDirectory + "test_WriteBI2File_DXT5NoMipMaps.dds");
			if(file.exists()) file.delete();
			
			System.out.println("start writing...");
			writeFile(bi, file, false, DDSImage.D3DFMT_DXT5);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void testWriteBI2File_DXT1MipMaps() {
		try {
			System.out.println("start reading...");
			BufferedImage bi = ImageIO.read(texturePNG2048);
			File file = new File(outputDirectory + "test_WriteBI2File_DXT1MipMaps.dds");
			if(file.exists()) file.delete();
			
			System.out.println("start writing...");
			writeFile(bi, file, true, DDSImage.D3DFMT_DXT1);
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private void writeFile(BufferedImage bi, File file,
			Boolean generateMipMaps, int pixelformat) throws IOException {
		DDSUtil.write(file, bi, pixelformat, generateMipMaps);
		assertEquals("file was created", true, file.exists());
		
		DDSImage newddsimage = DDSImage.read(file);
		
		assertEquals("height",bi.getHeight(), newddsimage.getHeight());
		assertEquals("width", bi.getWidth(), newddsimage.getWidth());
		assertEquals("pixelformat", pixelformat, newddsimage.getPixelFormat());
		if(generateMipMaps)
			assertEquals("number of MipMaps", MipMapsUtil.calculateMaxNumberOfMipMaps(bi.getWidth(), bi.getHeight()), newddsimage.getNumMipMaps());
	}
	
}
