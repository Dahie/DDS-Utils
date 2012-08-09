package de.danielsenff.de.madds.view;

import java.awt.Dimension;
import java.awt.Font;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.swing.JTextPane;
import javax.swing.UIManager;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

public class MoreWebbableView extends JTextPane {

	public MoreWebbableView(String path) {

		// add a CSS rule to force body tags to use the default label font
		// instead of the value in javax.swing.text.html.default.csss
		Font font = UIManager.getFont("Label.font");
		String bodyRule = "body { font-family: " + font.getFamily() + "; " +
				"font-size: " + font.getSize() + "pt; }";

		try {
			File file = new File(path);
			URL descriptionUrl;
			if(file.exists())
				descriptionUrl = file.toURL();
			else 
				descriptionUrl = getClass().getResource("/"+path);
			setPage(descriptionUrl);
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		((HTMLDocument)getDocument()).getStyleSheet().addRule(bodyRule);
		setOpaque(false);
		setEditable(false);
		addHyperlinkListener(new HTMLListener());
	}

	private class HTMLListener implements HyperlinkListener {
		public void hyperlinkUpdate(HyperlinkEvent e) {
			if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
				try {
					openURL(e.getURL().toURI());
				} catch (URISyntaxException e2) {
					e2.printStackTrace();
				}
			}
		}
	}

	public static void openURL(URI uri) {
		if( !java.awt.Desktop.isDesktopSupported() ) {
			System.err.println( "Desktop is not supported (fatal)" );
			System.exit( 1 );
		}

		java.awt.Desktop desktop = java.awt.Desktop.getDesktop();

		if( !desktop.isSupported( java.awt.Desktop.Action.BROWSE ) ) {
			System.err.println( "Desktop doesn't support the browse action (fatal)" );
			System.exit( 1 );
		}

		try {
			desktop.browse( uri );
		}
		catch ( Exception e ) {
			System.err.println( e.getMessage() );
		}
	}

	public boolean getScrollableTracksViewportWidth() {  
		return (getSize().width < getParent().getSize().width);  
	}  
	public void setSize(Dimension d) {  
		if (d.width < getParent().getSize().width) {  
			d.width = getParent().getSize().width;  
		}  
		super.setSize(d);  
	}  
	
}
