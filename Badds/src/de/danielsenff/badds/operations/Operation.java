/**
 * 
 */
package de.danielsenff.badds.operations;

import java.awt.image.BufferedImage;


/**
 * @author danielsenff
 *
 */
public interface Operation {

	
	public BufferedImage run(final BufferedImage bi);
	@Override
	public String toString();
	
}
