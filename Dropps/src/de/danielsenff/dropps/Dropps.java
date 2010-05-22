package de.danielsenff.dropps;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.jdesktop.application.Application;
import org.jdesktop.application.LocalStorage;
import org.jdesktop.application.SingleFrameApplication;



/**
 * @author danielsenff
 *
 */
public class Dropps extends SingleFrameApplication {

	private static Logger logger = Logger.getLogger(Dropps.class.getName());
	private static final String sessionFile = "session.xml";

	/**
	 * At startup create and show the main frame of the application.
	 */
	@Override 
	protected void startup() {
		DroppsView mainView = new DroppsView(this);
		//    	getMainFrame().setLocationRelativeTo(null);
		//    	getMainFrame().setResizable(false);
		//    	getMainFrame().pack();
		//    	getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//    	getMainFrame().setVisible(true);
		show(mainView);
	}

	/**
	 * A convenient static getter for the application instance.
	 * @return the instance of DocumentEditorApp
	 */
	public static Dropps getApplication() {
		return Application.getInstance(Dropps.class);
	}

	/**
	 * Main method launching the application.
	 * @param args 
	 */
	public static void main(final String[] args) {
		launch(Dropps.class, args);
	}

}
