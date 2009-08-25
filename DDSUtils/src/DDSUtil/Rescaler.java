/**
 * 
 */
package DDSUtil;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * @author danielsenff
 *
 */
public abstract class Rescaler {


	private BufferedImage image;

	
	
	public BufferedImage getScaledInstance(int width, int height) {
		return rescaleBI(this.image, width, height);
	}
	
	public BufferedImage getScaledInstance(Dimension dimension) {
		return rescaleBI(this.image, dimension.width, dimension.height);
	}

	/**
	 * @param image2
	 * @param width
	 * @param height
	 * @return
	 */
	public abstract BufferedImage rescaleBI(BufferedImage image, int width, int height);
	
	
}
