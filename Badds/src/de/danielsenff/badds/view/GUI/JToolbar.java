/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JToolBar;

/**
 * @author danielsenff
 *
 */
public class JToolbar extends JToolBar {

	/* (non-Javadoc)
	 * @see javax.swing.JToolBar#add(javax.swing.Action)
	 */
	@Override
	public JButton add(Action action) {
		return (JButton) super.add(new JToolbarButton(action));
	}
	
}
