package de.danielsenff.badds.operations;

/**
 * 
 */

import java.awt.Image;
import java.awt.image.BufferedImage;


import com.mortennobel.imagescaling.ResampleOp;

import ddsutil.Rescaler;

/**
 * @author danielsenff
 *
 */
public class ImageScalingRescaler extends Rescaler {

		private int scaleAlgorithm;

		/**
		 * 
		 */
		public ImageScalingRescaler() {
			scaleAlgorithm = Image.SCALE_SMOOTH;
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

			ResampleOp  resampleOp = new ResampleOp (width, height);
			BufferedImage rescaledImage = resampleOp.filter(image, null);
			
			return rescaledImage;
		}

}
