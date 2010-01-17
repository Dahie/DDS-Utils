/**
 * 
 */
package model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import jogl.DDSImage;

import org.junit.Test;

import test.DDSTestCase;

import static org.junit.Assert.*;


/**
 * @author danielsenff
 *
 */
public class DDSImageTests extends DDSTestCase {

	@Test
	public void testFourCC() throws FileNotFoundException, IOException {
		assertTrue("is a dds image", DDSImage.isDDSImage(new FileInputStream(textureDDS1024)));
	}
	
	@Test
	public void testByteBuffer() {
		try {
			DDSImage image = DDSImage.read(textureDDS1024);
			ByteBuffer buffer = image.getMipMap(0).getData();
			assertEquals("buffer length not zero",true, (buffer.capacity() > 0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void testByteBufferGet() {
		try {
			DDSImage image = DDSImage.read(textureDDS1024);
			ByteBuffer buffer = image.getMipMap(0).getData();
			assertEquals("buffer length not zero",true, (buffer.capacity() > 0));
			byte firstByte = buffer.get();
			System.out.println(firstByte);
			assertEquals("first byte not zero",true, (firstByte > 0));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
