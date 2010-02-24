package de.danielsenff.radds.controller;


import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;

import model.DDSFile;
import model.TEXFile;
import model.TextureImage;
import util.FileUtil;
import util.ImageIOUtils;
import ddsutil.DDSUtil;
import de.danielsenff.radds.models.FilesListModel;
import de.danielsenff.radds.models.TextureImageFormatLoaderTGA;
import de.danielsenff.radds.view.View;

/**
 * 
 */

/**
 * @author danielsenff
 *
 */
public class Application extends org.jdesktop.application.SingleFrameApplication {

	//GUI
	private View view;
	private ImageFileChooser imageFileChooser;
	// List with all operations	

	private FilesListModel openFilesModel;

	private static ResourceBundle bundle = ResourceBundle.getBundle("Texte");

	/**
	 * 
	 */
	public Application() {
		this.openFilesModel = new FilesListModel();
		this.view = new View(this);
	}

	public void removeFile(int index) {
		this.openFilesModel.remove(index);
	}

	/* (non-Javadoc)
	 * @see main.Controller#getImageFileChooser()
	 */
	public ImageFileChooser showImageFileChooser() {
		return this.imageFileChooser;
	}


	public JProgressBar getProgressBar() {
		return this.view.getProgressbar();
	};

	public FilesListModel getFilesListModel() {
		return this.openFilesModel;
	}

	public static ResourceBundle getBundle() {
		return bundle;
	}

	public View getView() {
		return this.view;
	}

	public BufferedImage getCurrentImage() {
		BufferedImage image = view.getCanvas().getCanvas();
		//		DDSImageFile image = this.openFilesModel.getSelectedItem();
		return image;  
	}

	@Override
	protected void startup() {
	}

	/**
	 * Evaluates the file and if possible loads the image.
	 * @param file
	 */
	public void setImage(File file) {
		long mem0;
		try {
			if(ImageIOUtils.isImageIOSupported(file)) {
				readImageIOImage(file);
			} else if (FileUtil.getFileSuffix(file).endsWith("tga")) {
				readTGAImage(file);
			} else if (DDSUtil.isReadSupported(file))
				readDDSUtilImage(file);
			else {
				JOptionPane.showMessageDialog(null, 
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
			JOptionPane.showMessageDialog(getView(), 
					"<html>Error: Out of memory: " + mem0 +
					"<br>The operation is aborted. </html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void readTGAImage(File file) throws IOException {
		TextureImageFormatLoaderTGA loader = new TextureImageFormatLoaderTGA();
		BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));
		BufferedImage image = loader.loadTextureImage(in , true, false); 
		getView().setImage(image);
	}

	private static void initRaster(BufferedImage bi, int width, int height, Buffer buffer) {
		WritableRaster wr = bi.getRaster();
		byte[] rgba = new byte[buffer.capacity()];
		((ByteBuffer)buffer).get(rgba);
		
		wr.setDataElements(0,0, width, height, rgba);
	}
	
    private static void swapBGR(byte[] data, int bWidth, int height, int bpp) {
        byte r,b;
        int k;
        for(int i=0; i<height; ++i) {
            for(int j=0; j<bWidth; j+=bpp) {
                k=i*bWidth+j;
                b=data[k+0];
                r=data[k+2];
                data[k+0]=r;
                data[k+2]=b;
            }
        }
    }


	private void readImageIOImage(File file) throws IOException {
		BufferedImage image = ImageIO.read(file);
		System.out.println(image.getType());
		System.out.println(image.getColorModel().hasAlpha());
		System.out.println(image.getColorModel().isAlphaPremultiplied());
		System.out.println(image.getColorModel().getNumComponents());
		getView().setImage(image);
	}
	
	private void readDDSUtilImage(File file)
			throws IOException {
		TextureImage image = null;
		String filename = file.getAbsolutePath();
		if(DDSFile.isValidDDSImage(file)) {
			image = new DDSFile(filename);
			if(image.getTextureType() == DDSFile.TextureType.CUBEMAP ||
					image.getTextureType() == DDSFile.TextureType.VOLUME) {
				JOptionPane.showMessageDialog(null, 
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
		}
	}
}
