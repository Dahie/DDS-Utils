/**
 * 
 */
package de.danielsenff.badds.operations;

import java.awt.image.BufferedImage;


/**
 * @author danielsenff
 *
 */
public class SourceOperation implements Operation {

	

	/* (non-Javadoc)
	 * @see operations.Operation#run(java.awt.image.BufferedImage)
	 */
	@Override
	public BufferedImage run(BufferedImage bi) {
		return bi;
	}
	
	@Override
	public String toString() {
		return "Original Image";
	}

}
