package de.danielsenff.radds.controller;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import model.DDSFile;
import model.TEXFile;
import model.TextureImage;
import util.FileUtil;
import util.ImageIOUtils;
import ddsutil.DDSUtil;
import de.danielsenff.radds.RaddsView;
import de.danielsenff.radds.models.TextureImageFormatLoaderTGA;

/**
 * Controller for loading files
 * @author dahie
 *
 */
public class ImageController {

	/**
	 * Current View
	 */
	protected RaddsView view;
	
	/**
	 * @param view
	 */
	public ImageController(final RaddsView view) {
		this.view = view;
	}
	
	public RaddsView getView() {
		return view;
	}
	
	/**
	 * Evaluates the file and if possible loads the image.
	 * @param file
	 */
	public void openImage(final File file) {
		long mem0;
		InputStream fis;
		try {
			fis = new FileInputStream(file);
			if(ImageIOUtils.isImageIOSupported(file)) {
				readImageIOImage(file, fis);
			} else if (FileUtil.getFileSuffix(file).endsWith("tga")) {
				readTGAImage(file, fis);
			} else if (DDSUtil.isReadSupported(file))
				readDDSUtilImage(file);
			else {
				JOptionPane.showMessageDialog(null, // TODO move to locales
						"<html>Error: This programm doesn't support this file format." +
						"<br>"+file.getName()+" can not be loaded.</html>",	"Attention", 
						JOptionPane.INFORMATION_MESSAGE);
				return;
			}

			mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			// System.out.println(mem0);
		} catch (final OutOfMemoryError ex) {
			ex.printStackTrace();
			mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			JOptionPane.showMessageDialog(getView().getFrame(), // TODO move to locales
					"<html>Error: Out of memory: " + mem0 +
					"<br>The operation is aborted. </html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
		} catch (final IOException e) {
			e.printStackTrace();
		} 
	}

	private void readTGAImage(final File file, InputStream is) throws IOException {
		final TextureImageFormatLoaderTGA loader = new TextureImageFormatLoaderTGA();
		final BufferedInputStream in = new BufferedInputStream(is);
		try {
			final BufferedImage image = loader.loadTextureImage(in , true, false);
			getView().setImage(image, file);
			is.close();
		} catch (final Exception ex) {
//			ex.printStackTrace();
			JOptionPane.showMessageDialog(getView().getFrame(), // TODO move to locales
					"<html>Error: The TGA-file could not be loaded. Only 32bit TGA are supported." +
					"<br>The operation is aborted. </html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void readImageIOImage(final File file, InputStream is) throws IOException {
		final BufferedImage image = ImageIO.read(is);
		getView().setImage(image, file);
		is.close();
	}

	private void readDDSUtilImage(final File file)
	throws IOException {
		TextureImage image = null;
		final String filename = file.getAbsolutePath();
		if(DDSFile.isValidDDSImage(file)) {
			image = new DDSFile(filename);
			if(image.getTextureType() == DDSFile.TextureType.CUBEMAP ||
					image.getTextureType() == DDSFile.TextureType.VOLUME) {
				JOptionPane.showMessageDialog(null, // TODO move to locales
						"<html>Error: This programm doesn't support cubemaps or volume textures." +
						"<br>"+image.getFile().getName()+" can not be loaded.</html>",	"Attention", 
						JOptionPane.INFORMATION_MESSAGE);
				return;
			} 
		} else if (TEXFile.isValidTEXImage(file)) {
			image = new TEXFile(filename);
		} 
		if(image != null) {
			image.loadImageData();
			getView().setImage(image);
		} else {
			JOptionPane.showMessageDialog(null, // TODO move to locales
					"<html>Error, image can not be loaded.</html>",	"Attention", 
					JOptionPane.INFORMATION_MESSAGE);
		}
	}
}
