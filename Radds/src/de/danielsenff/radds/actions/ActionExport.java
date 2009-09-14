/**
 * 
 */
package de.danielsenff.radds.actions;

import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import Model.DDSImageFile;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

import de.danielsenff.radds.controller.Application;

/**
 * @author danielsenff
 *
 */
public class ActionExport extends BasicAction {

	/**
	 * 
	 */
	public ActionExport(Application controller) {
		super(controller);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent arg0) {
		BufferedImage bi = ((DDSImageFile) controller.getFilesListModel().getSelectedItem()).getData();
		
		File file = controller.showImageFileChooser().showExportDialogue(); // where to save
		if (file != null) {
			if(file.exists()) {
				int response = JOptionPane.showConfirmDialog(controller.getView(), 
						bundle.getString("save_overwrite_text"),
						bundle.getString("save_overwrite"), 
						JOptionPane.OK_CANCEL_OPTION);
				if (response == JOptionPane.OK_OPTION)
					prepareSaving(bi, file);
			} else {
				prepareSaving(bi, file);
			}
		} else {
			
		}
		
	}

	private void prepareSaving(BufferedImage image, File file) {

		try {
			FileOutputStream out = new FileOutputStream(file);
			//			ImageIO.write(image, "wbmp", out);

			if(file.getName().toLowerCase().contains(".bmp")) {
//				if (!ImageIO.write(image, "WBMP", file)) {
					System.out.println("not via imagio");
					
					// See http://www.jguru.com/faq/view.jsp?EID=127723 

					out.write(0);
					out.write(0);
					out.write(_toMultiByte(image.getWidth()));
					out.write(_toMultiByte(image.getHeight()));

					DataBuffer dataBuffer = image.getData().getDataBuffer();
 
					int size = dataBuffer.getSize();

					for (int i = 0; i < size; i++) {
						out.write((byte)dataBuffer.getElem(i));
					}
//				} 
			} else if (file.getName().toLowerCase().contains(".jpg") ||
					file.getName().toLowerCase().contains("jpeg")) {
//				ImageIO.write(image, "jpg", file);
				
				JPEGImageEncoder jpegImageEncoder = JPEGCodec.createJPEGEncoder(new FileOutputStream(file));
				JPEGEncodeParam param = jpegImageEncoder.getDefaultJPEGEncodeParam(image);
				param.setQuality(0.5F, true);
				jpegImageEncoder.encode(image, param);

			} else if (file.getName().toLowerCase().contains(".png")) {
				ImageIO.write(image, "png", file);
			}				
			
			out.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static byte[] _toMultiByte(int intValue) {
		int numBits = 32;
		int mask = 0x80000000;

		while (mask != 0 && (intValue & mask) == 0) {
			numBits--;
			mask >>>= 1;
		}

		int numBitsLeft = numBits;
		byte[] multiBytes = new byte[(numBitsLeft + 6) / 7];

		int maxIndex = multiBytes.length - 1;

		for (int b = 0; b <= maxIndex; b++) {
			multiBytes[b] = (byte)((intValue >>> ((maxIndex - b) * 7)) & 0x7f);

			if (b != maxIndex) {
				multiBytes[b] |= (byte)0x80;
			}
		}

		return multiBytes;
	}



	
}
