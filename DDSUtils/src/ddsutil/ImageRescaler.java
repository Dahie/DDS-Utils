/**
 * 
 */
package ddsutil;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 * @author danielsenff
 *
 */
public class ImageRescaler extends Rescaler {


		private int scaleAlgorithm;

		/**
		 * 
		 */
		public ImageRescaler() {
			scaleAlgorithm = Image.SCALE_SMOOTH;
		}
		
		/**
		 * @param scaleMethod
		 */
		public ImageRescaler(final int scaleMethod) {
			scaleAlgorithm = scaleMethod;
		}
		
		/**
		 * @param image
		 * @param width
		 * @param height
		 * @return
		 */
		@Override
		public BufferedImage rescaleBI(final BufferedImage originalImage,
				final int newWidth, final int newHeight) {

			BufferedImage bi = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
			Graphics2D g = bi.createGraphics();
	        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);			
			return bi;
		}

		/**
		 * @return
		 */
		public int getScaleAlgorithm() {
			return this.scaleAlgorithm;
		}

		/**
		 * @param scaleAlgorithm
		 */
		public void setScaleAlgorithm(final int scaleAlgorithm) {
			this.scaleAlgorithm = scaleAlgorithm;
		}
	
	
}
