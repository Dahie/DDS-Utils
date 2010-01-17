/**
 * 
 */
package ddsutil;

import java.awt.Image;
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
		public BufferedImage rescaleBI(final BufferedImage image,
				final int width, final int height) {

			Image rescaledImage = image.getScaledInstance(width, height, scaleAlgorithm);
			BufferedImage bi;
			if(rescaledImage instanceof BufferedImage)
				bi = (BufferedImage)rescaledImage;
			else
				bi = BIUtil.convertImageToBufferedImage(rescaledImage, BufferedImage.TYPE_4BYTE_ABGR);
			//rescale.flush();
			
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
