/**
 * 
 */
package de.danielsenff.badds.model;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.TransferHandler;

import de.danielsenff.badds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class FileTransferHandler extends TransferHandler implements Transferable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final DataFlavor flavors[] = { DataFlavor.javaFileListFlavor };

	private Application controller;
	private List<File> files;

	/**
	 * 
	 */
	public FileTransferHandler(Application controller) {
		this.controller = controller;
	}
	
	@Override
	public int getSourceActions(JComponent c) {
		return TransferHandler.COPY;
	}

	@Override
	public Transferable createTransferable(JComponent comp) {
		// Clear
		files = null;
		//TODO
		return this;
	}

	@Override
	public boolean importData(JComponent comp, Transferable t) {
		if(comp instanceof JTable) {
//			JTable table = (JTable) comp;
			if (t.isDataFlavorSupported(flavors[0])) {
				try {
					files = (List) t.getTransferData(flavors[0]);
					for (int i = 0; i < files.size(); i++) {
						controller.addFile(files.get(i));	
					}
					
					return true;
				} catch (UnsupportedFlavorException ignored) {
				} catch (IOException ignored) {
				}
			}
		}
		
		

		return false;
	}

	// Transferable
	@Override
	public Object getTransferData(DataFlavor flavor) {
		if (isDataFlavorSupported(flavor)) {
			return files;
		}
		return null;
	}

	@Override
	public DataFlavor[] getTransferDataFlavors() {
		return flavors;
	}

	@Override
	public boolean isDataFlavorSupported(DataFlavor flavor) {
		return flavor.equals(DataFlavor.javaFileListFlavor);
	}
}