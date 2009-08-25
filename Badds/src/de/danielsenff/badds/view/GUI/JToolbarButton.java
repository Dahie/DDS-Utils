/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.SwingConstants;

/**
 * @author danielsenff
 *
 */
public class JToolbarButton extends JButton {

	/**
	 * 
	 */
	public JToolbarButton(Action action) {
		super(action);
		this.setText((String) action.getValue(action.NAME));
		this.setVerticalTextPosition(SwingConstants.BOTTOM);
		this.setHorizontalTextPosition(SwingConstants.CENTER);
	}
	
}
