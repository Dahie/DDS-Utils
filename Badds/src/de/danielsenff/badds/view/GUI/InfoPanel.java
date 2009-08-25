/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JLabel;

import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.util.ResourceLoader;



/**
 * @author danielsenff
 *
 */
public class InfoPanel extends JCPanel {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * @param controller
	 */
	public InfoPanel(Application controller) {
		super(controller);
		
		setLayout(new BorderLayout());
		add(new JLabel(ResourceLoader.getResourceIcon("/de/danielsenff/badds/resources/Badds128.png")), BorderLayout.NORTH);
		
		String text = "<html><p><b>Badds</b><br> " +
				"Batch a DDS<br>" +
				"by Daniel Senff<br><br></p>" +
				"<p>Batch manipulation of DDS files.</p>" +
				"<p>"+
				"<p>Contact<br>" +
				"Mail: mail@danielsenff.de</p>" +
				"Website: http://www.danielsenff.de<br><br></p>" +
				"</html>";
		
		JLabel textlabel = new JLabel(text);
		add(textlabel, BorderLayout.CENTER);
		
	}


}
