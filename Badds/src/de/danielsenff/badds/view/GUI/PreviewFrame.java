/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import java.awt.image.BufferedImage;

import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;

import ddsutil.ImageOperations;
import de.danielsenff.badds.actions.ActionClose;
import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.view.canvas.BICanvas;
import de.danielsenff.badds.view.canvas.ScrollCanvasListener;


/**
 * @author danielsenff
 *
 */
public class PreviewFrame extends JCFrame{

	/**
	 * @param filename 
	 * 
	 */
	public PreviewFrame(final Application controller, String filename, BufferedImage bi) {
		super(controller);
		setLocationByPlatform(true);
		setSize(400, 400);
		addKeyListener(new ActionClose(controller, this));
		try {
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
		} catch (ClassNotFoundException e) {

		} catch (InstantiationException e) {

		} catch (IllegalAccessException e) {

		} catch (UnsupportedLookAndFeelException e) {

		}
		
		setResizable(true); 
		setTitle(bundle.getString("Preview") + " - " + filename);
		setName(bundle.getString("Preview") + " - " + filename);
		
		final BICanvas canvas = new BICanvas(controller, bi, ImageOperations.ChannelMode.RGB);
		final JScrollPane scrollViewPane = new JScrollPane(canvas);
		
		final ScrollCanvasListener scrollCanvasListener = new ScrollCanvasListener(scrollViewPane);
		canvas.addMouseMotionListener(scrollCanvasListener);
//		canvas.addMouseWheelListener(scrollCanvasListener);
		canvas.addAncestorListener(new AncestorListener() {
			@Override
			public void ancestorAdded(AncestorEvent arg0) {		}
			@Override
			public void ancestorMoved(AncestorEvent arg0) {
				scrollViewPane.repaint();
			}
			@Override
			public void ancestorRemoved(AncestorEvent arg0) {	}
			
		});
		this.getContentPane().add(scrollViewPane);
	}
	
	/**
	 * 
	 */
	public void close() {
		this.dispose();
	}
	
}
