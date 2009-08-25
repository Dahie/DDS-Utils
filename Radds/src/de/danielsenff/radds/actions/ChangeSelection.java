/**
 * 
 */
package de.danielsenff.radds.actions;

import java.awt.Cursor;
import java.awt.image.BufferedImage;

import javax.swing.JList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.models.SwingWorker;
import de.danielsenff.radds.view.View;

import Model.DDSImageFile;



/**
 * @author danielsenff
 *
 */
public class ChangeSelection implements ListSelectionListener {

	private Application controller;

	/**
	 * 
	 */
	public ChangeSelection(Application controller) {
		this.controller = controller;
	}	
	
	/* (non-Javadoc)
	 * @see javax.swing.event.ListSelectionListener#valueChanged(javax.swing.event.ListSelectionEvent)
	 */

	public void valueChanged(ListSelectionEvent event) {
		final JList list = (JList) event.getSource();
		View view = controller.getView();
		
		
		if ( !event.getValueIsAdjusting() ) {
			if (!list.isSelectionEmpty()) {

				SwingWorker worker = new SwingWorker() {

					@Override
					public Object construct() {
						controller.getView().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
						switchBI(list);
						
						return "Image switched";
					}

					@Override
					public void finished() {
						controller.getView().setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
					}

				};
				worker.start();


				view.getActionCloseFile().setEnabled(true);
				view.getActionCopy().setEnabled(true);
				view.getActionExport().setEnabled(true);
			} else {
				view.getActionCloseFile().setEnabled(false);
				view.getActionCopy().setEnabled(false);
				view.getActionExport().setEnabled(false);
			}
		}
	}

	private void switchBI(JList list) {
		DDSImageFile ddsimagefile = (DDSImageFile) list.getSelectedValue();
		BufferedImage bi = ddsimagefile.getData();
		controller.getView().getCanvas().setSourceBI(bi);
		controller.getView().getFilesPanel().getInfoPanel().setDDSFile(ddsimagefile);
	}

}
