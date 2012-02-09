/**
 * 
 */
package de.danielsenff.badds.actions;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.KeyStroke;

import de.danielsenff.badds.controller.Application;


/**
 * @author danielsenff
 *
 */
public class ActionClose extends BasicAction implements KeyListener {

	
	private JFrame frame;
	
	/**
	 * @param controller
	 */
	public ActionClose(Application controller, JFrame frame) {
		super(controller);
		this.frame = frame;
	}



	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent arg0) {
		frame.dispose();
	}



	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyPressed(KeyEvent event) {
		int keyCode = event.getKeyCode();
		int expectedKeyCode = ((KeyStroke) this.getValue(ACCELERATOR_KEY)).getKeyCode();
		
		if(keyCode == expectedKeyCode && event.isMetaDown()) {
			this.frame.dispose();
		}
		
	}



	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyReleased(KeyEvent arg0) {}



	/* (non-Javadoc)
	 * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
	 */
	@Override
	public void keyTyped(KeyEvent arg0) {}

}
