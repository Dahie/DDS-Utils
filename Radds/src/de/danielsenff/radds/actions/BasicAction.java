package de.danielsenff.radds.actions;

import java.util.ResourceBundle;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.util.OS;




abstract public class BasicAction extends AbstractAction {

	ResourceBundle bundle = Application.getBundle();
	Application controller;
	
	/**
	 * @param controller
	 */
	public BasicAction(final Application controller) {
		this(controller, true);
	}

	public BasicAction(final Application controller, final boolean hasBundle) {
		this.controller = controller;

		if (hasBundle){
			String name = getClass().getName();
			putValue(NAME, bundle.getString(name+".name"));
			String string = bundle.getString(name+".description");
			putValue(SHORT_DESCRIPTION, string);
			
			String hotkey;
			if (OS.isMacOS()) { 
				hotkey = bundle.getString(name+".hotkey_osx");
			} else { 
				hotkey = bundle.getString(name+".hotkey");
			}
			putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(hotkey));
//			putValue(SMALL_ICON, ResourceLoader.getResourceIcon(bundle.getString(name+".icon")));
			
//			putValue(MNEMONIC_KEY, new Integer('S'));
		}
	}

}
