/**
 * 
 */
package de.danielsenff.badds.actions;

import gr.zdimensions.jsquish.Squish;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

import org.jagatoo.image.BufferedImageFactory;
import org.jagatoo.loaders.textures.AbstractTexture;
import org.jagatoo.loaders.textures.formats.TextureFormatLoaderDDS;
import org.xith3d.loaders.texture.Xith3DTextureFactory2D;

import DDSUtil.DDSUtil;
import Model.DDSFile;
import Model.DDSImageFile;
import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.model.FilesListModel;
import de.danielsenff.badds.model.SwingWorker;
import de.danielsenff.badds.view.GUI.PreviewFrame;


/**
 * @author danielsenff
 *
 */
public class ActionPreview extends BasicAction {

	/**
	 * @param controller
	 */
	public ActionPreview(final Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(final ActionEvent arg0) {
		controller.getView().getWorkingView().startWorkingState(1);
//		controller.getView().getProgressbar().setIndeterminate(true);
		controller.setSwingWorker(new SwingWorker() {

			@Override
			public Object construct() {
				createPreviewFrame();
				return "All done";
			}
			
			@Override
			public void finished() {
//				controller.getView().getProgressbar().setIndeterminate(false);
				controller.getView().getWorkingView().endWorkingState("Preview loaded");
            }
			
		});
		controller.getSwingWorker().start();
		
	}

	public void createPreviewFrame() {
		final int selectedRow = controller.getView().getOpenFilesTable().getSelectedRow();
		final FilesListModel model = (FilesListModel<DDSFile>) controller.getFilesListModel();
		final DDSFile ddsfile = (DDSFile) model.getRowItem(selectedRow);
		final DDSImageFile ddsimagefile ;

		try {
			ddsimagefile = new DDSImageFile(ddsfile.getFile());
//			BufferedImage bi = ddsimagefile.getData();
			BufferedImage bi = loadViaJagatoo(ddsfile.getFile(), ddsfile.getPixelformat()); 
			
			
			System.out.println("create frame");
			new PreviewFrame(controller, ddsfile.getFile().getName(),  bi).setVisible(true);
		} catch (final OutOfMemoryError e) {
			JOptionPane.showMessageDialog(controller.getView(), 
					"<html>Java is out of memory. Please close other previews before opening yet another.</html>",	"Out of memory", 
					JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
		}

//		controller.getView().getProgressbar().setIndeterminate(false);
		controller.getView().getWorkingView().endWorkingState("Preview loaded");

		

	}

	private BufferedImage loadViaJagatoo(File file, int pixelFormat) {
		TextureFormatLoaderDDS loader = new TextureFormatLoaderDDS();
//		File file = new File("/Users/danielsenff/Pictures/dds/BANNER_1.DDS");
		FileInputStream fis;
		BufferedImage bi = null;
		try {
			fis = new FileInputStream(file);
			System.out.println("Read Buffer");
			BufferedInputStream in = new BufferedInputStream(fis);
			Xith3DTextureFactory2D texFac = Xith3DTextureFactory2D.getInstance();
			System.out.println("Load texture");
			AbstractTexture tex = loader.loadTexture(in, true, false, true, false, texFac);
			System.out.println(tex.getWidth() +"  and " +tex.getHeight());
			
			Squish.CompressionType comp = DDSUtil.getSquishCompressionFormat(pixelFormat);
			
			System.out.println("decompress data");
			ByteBuffer bb = squishDecompressBuffer(tex.getImage(0).getDataBuffer(), 
				tex.getWidth(), 
				tex.getHeight(), 
				comp);
			
			// read buffer image
			int[] bandOffsets = {0, 1, 2, 3};
			System.out.println("create BufferedImage");
			bi = BufferedImageFactory.createDirectBufferedImage(tex.getWidth(), 
					tex.getHeight(), 
					true, 
					bandOffsets, 
					bb);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return bi;
	}


	/**
	 * Decompresses a DXT-compressed {@link ByteBuffer} 
	 * @param byteBuffer
	 * @param width
	 * @param height
	 * @param type
	 * @return
	 * @throws OutOfMemoryError
	 */
	private static ByteBuffer squishDecompressBuffer(final ByteBuffer byteBuffer, 
			final int width, final int height,
			final Squish.CompressionType type) throws OutOfMemoryError {
		
		byte[] data = new byte[byteBuffer.capacity()]; 
		byteBuffer.get(data); 
		// why rgba null?
		return ByteBuffer.wrap(Squish.decompressImage(null, width, height, data, type)); 
	} 
	
}
