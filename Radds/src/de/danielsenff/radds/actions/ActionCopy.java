package de.danielsenff.radds.actions;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;

import de.danielsenff.radds.controller.Application;

import Model.DDSImageFile;

public class ActionCopy extends BasicAction implements ClipboardOwner {

	private Clipboard myClipboard;
	
	public ActionCopy(final Application controller) {
		super(controller);
		myClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
	}

	public void actionPerformed(ActionEvent arg0){
	
		BufferedImage bi = controller.getCurrentImage();
		
		ClipImage ci = new ClipImage(bi); 
		myClipboard.setContents(ci, this);
	
	}

	private class ClipImage implements Transferable 	{
		private DataFlavor[] myFlavors; 
		private BufferedImage myImage;

		public ClipImage(BufferedImage theImage)
		{
			myFlavors = new DataFlavor[]{DataFlavor.imageFlavor};
			myImage = theImage;
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
		{
			if (flavor != DataFlavor.imageFlavor) {
				throw new UnsupportedFlavorException(flavor);
			}
			return myImage;
		}

		public DataFlavor[] getTransferDataFlavors() {
			return myFlavors;
		}

		public boolean isDataFlavorSupported(DataFlavor flavor) {
			return (flavor == DataFlavor.imageFlavor);
		}
	}

	public void lostOwnership(Clipboard arg0, Transferable arg1) {	}

}
