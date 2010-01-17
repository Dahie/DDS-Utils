/**
 * 
 */
package de.danielsenff.badds.actions;

import gr.zdimensions.jsquish.Squish;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;

import javax.swing.JOptionPane;

import model.DDSFile;

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
		final DDSFile ddsimagefile ;

		try {
			ddsimagefile = new DDSFile(ddsfile.getFile());
			ddsimagefile.loadImageData();
			BufferedImage bi = ddsimagefile.getData();
			
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
