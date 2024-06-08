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
		TextureMap maps;
		if (generateMipMaps) {
			maps = new MipMaps();
			((MipMaps)maps).generateMipMaps(sourceImage);
		} else
			maps = new SingleTextureMap(sourceImage);
		
		return maps;
	}
}
