/**
 * 
 */
package test;

import ddsutil.ImageOperations;
import junit.framework.TestCase;

/**
 * @author danielsenff
 *
 */
public class ImageOperationTests extends TestCase {

	
	public void testWithinBoundaries() {
		int value = 5;
		int upperValue = 10;
		int lowerValue = -10;
		int newValue = ImageOperations.checkValueLimits(value, lowerValue, upperValue);
		assertEquals("value within limits", value, newValue);
	}
	
	public void testLowerBoundaries() {
		int value = -15;
		int upperValue = 10;
		int lowerValue = -10;
		int newValue = ImageOperations.checkValueLimits(value, lowerValue, upperValue);
		assertEquals("value below limits", lowerValue, newValue);
	}
	
	public void testUpperBoundaries() {
		int value = 15;
		int upperValue = 10;
		int lowerValue = -10;
		int newValue = ImageOperations.checkValueLimits(value, lowerValue, upperValue);
		assertEquals("value above limits", upperValue, newValue);
	}
	
	public void testReadARGB() {
		int[] color = {255, 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelARGB(color);
		assertEquals("color value as integer", -16777128, colorInt);
	}
	
	public void testReadWriteARGB(){
		int[] color = {255, 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelARGB(color);
		assertEquals("color value as integer", -16777128, colorInt);
		
		
		int[] newColor = ImageOperations.readPixelARGB(colorInt);
		for (int i = 0; i < color.length; i++) {
			assertEquals("color value", color[i], newColor[i]);	
		}
	}
	
	public void testReadRGB() {
		int[] color = { 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelRGB(color);
		assertEquals("color value as integer", -16777128, colorInt);
	}
	
	public void testReadWriteRGB(){
		int[] color = { 0, 0, 88};
		
		int colorInt = ImageOperations.writePixelRGB(color);
		assertEquals("color value as integer", -16777128, colorInt);
		int[] newColor = ImageOperations.readPixelRGB(colorInt);
		int[] argb = { 255, color[0], color[1], color[2]};
		for (int i = 0; i < color.length+1; i++) {
			assertEquals("color value at "+ i, argb[i], newColor[i]);	
		}
	}
	
}
