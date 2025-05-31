package org.ddsutil;

import java.awt.image.BufferedImage;

import org.model.MipMaps;
import org.model.SingleTextureMap;
import org.model.TextureMap;

/**
 * @author danielsenff
 *
 */
public class TextureFactory {

	/**
	 * @param generateMipMaps
	 * @param sourceImage
	 * @return
	 */
	public static TextureMap createTextureMap(final boolean generateMipMaps, final BufferedImage sourceImage) {
		TextureMap map;
		if (generateMipMaps) {
			map = new MipMaps();
			((MipMaps)map).generateMipMaps(sourceImage);
		} else
			map = new SingleTextureMap(sourceImage);
		
		return map;
	}
}
