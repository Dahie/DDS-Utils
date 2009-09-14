/**
 * 
 */
package DDSUtil;

import java.awt.image.BufferedImage;

/**
 * @author danielsenff
 *
 */
public abstract class Rescaler {

	/**
	 * @param image2
	 * @param width
	 * @param height
	 * @return
	 */
	public abstract BufferedImage rescaleBI(BufferedImage image, int width, int height);
	
	
}
