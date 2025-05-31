/**
 * 
 */
package org.model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import org.jogl.DDSImage;


/**
 * @author danielsenff
 *
 */
public class DDSCubeImageFile extends DDSFile {

	/**
	 * 0 - top
	 * 1 - bottom
	 * 2 - front
	 * 3 - back
	 * 4 - left
	 * 5 - right
	 */
	private BufferedImage[] cubeFaces;
	
	/**
	 * Faces of the Cube
	 *
	 */
	public enum Faces {
		top, bottom, front, back, left, right
	}
	

	/**
	 * @param file
	 * @throws IOException 
	 * @throws IOException
	 */
	public DDSCubeImageFile(File file) throws IOException {
		super(file);

	}

	/**
	 * @param file
	 * @param bufferedImage
	 * @param pixelFormat
	 * @param hasMipMaps
	 * @throws IOException
	 */
	public DDSCubeImageFile(File file, BufferedImage bufferedImage, int pixelFormat,
			boolean hasMipMaps) {
		super(file, bufferedImage, pixelFormat, hasMipMaps);
	}

	/**
	 * @param file
	 * @param ddsImage
	 */
	public DDSCubeImageFile(File file, DDSImage ddsImage) {
		super(file, ddsImage);
		// TODO Auto-generated constructor stub
	}
}
