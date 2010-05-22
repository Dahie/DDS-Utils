/**
 * 
 */
package de.danielsenff.radds;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;

import model.TextureImage;

import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;

import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.tasks.ActionCopy;
import de.danielsenff.radds.tasks.ActionExport;
import de.danielsenff.radds.util.OS;
import de.danielsenff.radds.view.FilesPanel;
import de.danielsenff.radds.view.canvas.BICanvas;
import de.danielsenff.radds.view.canvas.CanvasControlsPanel;


/**
 * @author danielsenff
 *
 */
public class RaddsView extends FrameView {

	private final JProgressBar progressbar;

	private ActionCopy actionCopy;
	private ActionExport actionExport;
	private final FilesPanel filesPanel;
	private final CanvasControlsPanel canvasPanel;
	
	private BufferedImage busy;
	
	/**
	 * @param controller 
	 * 
	 */
	public RaddsView(final Radds radds) {
		super(radds);
		
		try {
//			this.setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/de/danielsenff/radds/resources/Radds128.png")));
			this.busy = ImageIO.read(this.getClass().getResourceAsStream("/de/danielsenff/radds/resources/defaultimage.png"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		initComponnents();
		
		// menu
		initMenu();
		
		// general layout
		filesPanel = new FilesPanel();
		
		canvasPanel = new CanvasControlsPanel(this);
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filesPanel, canvasPanel);
		splitPane.setDividerLocation(200);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(0.0);
		
		this.progressbar = new JProgressBar();
		
//		setLayout(new BorderLayout());
//		getContentPane().add(splitPane, BorderLayout.CENTER);
		setComponent(splitPane);

		
	}

	private void initMenu() {
		final JMenuBar menuBar = new JMenuBar();
		
		final ResourceMap resourceMap = getResourceMap();
		
		JMenu menuFile;
		if(OS.isMacOS()) 
			menuFile = new JMenu(resourceMap.getString("File_menu_osx"));
		else
			menuFile = new JMenu(resourceMap.getString("File_menu"));
		
		final JMenu menuEdit = new JMenu(resourceMap.getString("Edit"));
		menuEdit.add(actionCopy);
		menuBar.add(menuEdit);
		
		if(OS.isMacOS()) 
			menuFile = new JMenu(resourceMap.getString("View_menu_osx"));
		else
			menuFile = new JMenu(resourceMap.getString("View_menu"));
		
		setMenuBar(menuBar);
	}


	private void initComponnents() {
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setLocationByPlatform(true);
//		setPreferredSize(new Dimension(900, 600));
//	
//		setResizable(true); 
//		setTitle(title + "unknown");
//		setName(title);
//		setVisible(true);
//		pack();
	}

	
	/**
	 * @return the progressbar
	 */
	public JProgressBar getProgressbar() {
		return this.progressbar;
	}

	public BICanvas getCanvas() {
		return this.canvasPanel.getCanvas();
	}

	/**
	 * Sets the image in the Canvas to the specified Image.
	 * @param image
	 */
	public void setImage(final TextureImage image) {
		getCanvas().setSourceBI(image.getData());
		filesPanel.getInfoPanel().setTextureFile(image);
//		getFrame().setTitle(title+image.getFile().getName());
		this.actionCopy.setEnabled(true);
	}

	public void setImage(final BufferedImage image) {
		getCanvas().setSourceBI(image);
		filesPanel.getInfoPanel().setTextureFile(image);
		this.actionCopy.setEnabled(true);
	}
	
	
	/**
	 * Sets the icon on the Canvas to "busy"
	 */
	public void setBusyImage() {
		this.actionCopy.setEnabled(false);
		getCanvas().setSourceBI(busy);
	}
	
	
	
	/*
	 * Action definitions
	 * 
	 */
	
}
