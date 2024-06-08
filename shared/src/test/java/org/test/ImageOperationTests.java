/**
 * 
 */
package org.test;

import org.ddsutil.ImageOperations;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


/**
 * @author danielsenff
 *
 */
public class ImageOperationTests {

	@Test
	public void testWithinBoundaries() {
		int value = 5;
		int upperValue = 10;
		int lowerValue = -10;
		int newValue = ImageOperations.checkValueLimits(value, lowerValue, upperValue);
		assertEquals(value, newValue);
	}
	
	@Test
	public void testLowerBoundaries() {
		int value = -15;
		int upperValue = 10;
		int lowerValue = -10;
		int newValue = ImageOperations.checkValueLimits(value, lowerValue, upperValue);
		assertEquals(lowerValue, newValue);
	}
	
	@Test
	public void testUpperBoundaries() {
		int value = 15;
		int upperValue = 10;
		int lowerValue = -10;
		int newValue = ImageOperations.checkValueLimits(value, lowerValue, upperValue);
		assertEquals(upperValue, newValue);
	}
	
	@Test
	public void testReadARGB() {
		int[] color = {255, 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelARGB(color);
		assertEquals(-16777128, colorInt);
	}
	
	@Test
	public void testReadWriteARGB(){
		int[] color = {255, 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelARGB(color);
		assertEquals(-16777128, colorInt);
		
		
		int[] newColor = ImageOperations.readPixelARGB(colorInt);
		for (int i = 0; i < color.length; i++) {
			assertEquals(color[i], newColor[i]);	
		}
	}
	
	@Test
	public void testReadRGB() {
		int[] color = { 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelRGB(color);
		assertEquals(-16777128, colorInt);
	}
	
	@Test
	public void testReadWriteRGB(){
		int[] color = { 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelRGB(color);
		assertEquals(-16777128, colorInt);
		int[] newColor = ImageOperations.readPixelRGB(colorInt);
		int[] argb = { 255, color[0], color[1], color[2]};
		for (int i = 0; i < color.length+1; i++) {
			assertEquals(argb[i], newColor[i]);	
		}
	}
	
}
