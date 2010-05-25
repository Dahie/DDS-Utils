package de.danielsenff.radds.models;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;

public class ClipImage implements Transferable{
	public DataFlavor[] myFlavors;
	public BufferedImage myImage;

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