/**
 * 
 */
package Model;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import JOGL.DDSImage;

/**
 * @author danielsenff
 *
 */
public class DDSCubeImageFile extends DDSImageFile {

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
	 * @param filename
	 * @throws IOException
	 */
	public DDSCubeImageFile(String filename) throws IOException {
		super(filename);
		this.cubeFaces = new BufferedImage[6];
	}

	/**
	 * @param file
	 * @throws IOException
	 */
	public DDSCubeImageFile(File file) {
		super(file);

	}

	/**
	 * @param filename
	 * @param bi
	 * @param pixelformat
	 * @param hasmipmaps
	 * @throws IOException
	 */
	public DDSCubeImageFile(String filename, BufferedImage bi, int pixelformat,
			boolean hasmipmaps) {
		super(filename, bi, pixelformat, hasmipmaps);

	}

	/**
	 * @param file
	 * @param ddsimage
	 */
	public DDSCubeImageFile(File file, DDSImage ddsimage) {
		super(file, ddsimage);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param filename
	 * @param ddsimage
	 */
	public DDSCubeImageFile(String filename, DDSImage ddsimage) {
		super(filename, ddsimage);
	}
	
	
}
