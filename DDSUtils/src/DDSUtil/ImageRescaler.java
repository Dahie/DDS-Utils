/**
 * 
 */
package DDSUtil;

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
		
		public ImageRescaler(int scaleMethod) {
			scaleAlgorithm = scaleMethod;
		}
		
		/**
		 * @param image
		 * @param width
		 * @param height
		 * @return
		 */
		public BufferedImage rescaleBI(BufferedImage image,
				int width, int height) {

			Image rescale = image.getScaledInstance(width, height, scaleAlgorithm);
			BufferedImage bi = BIUtil.convertImageToBufferedImage(rescale, BufferedImage.TYPE_4BYTE_ABGR);
			rescale.flush();
			
			return bi;
		}

		public int getScaleAlgorithm() {
			return this.scaleAlgorithm;
		}

		public void setScaleAlgorithm(int scaleAlgorithm) {
			this.scaleAlgorithm = scaleAlgorithm;
		}
	
	
}
