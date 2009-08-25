package de.danielsenff.dropps;

import javax.swing.JFrame;

import org.jdesktop.application.Application;
import org.jdesktop.application.SingleFrameApplication;



/**
 * @author danielsenff
 *
 */
public class Dropps extends SingleFrameApplication {

	/**
     * At startup create and show the main frame of the application.
     */
    @Override protected void startup() {
        show(new DroppsView(this));
    	getMainFrame().setLocationRelativeTo(null);
    	getMainFrame().setResizable(false);
    	getMainFrame().pack();
//    	getMainFrame().addWindowListener(new MainFrameListener());
    	getMainFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//    	getMainFrame().setIconImage(new ImageIcon("icon.png").getImage());
    }

    /**
     * This method is to initialize the specified window by injecting resources.
     * Windows shown in our application come fully initialized from the GUI
     * builder, so this additional configuration is not needed.
     */
    @Override protected void configureWindow(final java.awt.Window root) {
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
     */
    public static void main(final String[] args) {
        launch(Dropps.class, args);
    }
    
}
