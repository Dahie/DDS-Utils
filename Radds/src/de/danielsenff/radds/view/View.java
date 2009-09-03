/**
 * 
 */
package de.danielsenff.radds.view;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;

import Model.DDSImageFile;
import de.danielsenff.radds.actions.ActionCopy;
import de.danielsenff.radds.actions.ActionExport;
import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.util.OS;
import de.danielsenff.radds.view.canvas.BICanvas;
import de.danielsenff.radds.view.canvas.CanvasControlsPanel;


/**
 * @author danielsenff
 *
 */
public class View extends JCFrame {

	final String title = bundle.getString("application_title");
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
	public View(final Application controller) {
		super(controller);
		
		try {
			this.setIconImage(ImageIO.read(this.getClass().getResourceAsStream("/de/danielsenff/radds/resources/Radds128.png")));
			this.busy = ImageIO.read(this.getClass().getResourceAsStream("/de/danielsenff/radds/resources/defaultimage.png"));
		} catch (final IOException e) {
			e.printStackTrace();
		}
		
		initFrame();
		
		// init actions
		initActions(controller);
		
		// menu
		initMenu(controller);
		
		// general layout
		filesPanel = new FilesPanel(controller);
		
		canvasPanel = new CanvasControlsPanel(controller);
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filesPanel, canvasPanel);
		splitPane.setDividerLocation(200);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(0.0);
		
		this.progressbar = new JProgressBar();
		
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(splitPane, BorderLayout.CENTER);

		setVisible(true);
		pack();
	}

	private void initActions(final Application controller) {
		actionCopy = new ActionCopy(controller);
		actionCopy.setEnabled(false);
	}

	private void initMenu(final Application controller) {
		final JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile;
		if(OS.isMacOS()) 
			menuFile = new JMenu(bundle.getString("File_menu_osx"));
		else
			menuFile = new JMenu(bundle.getString("File_menu"));
		
		final JMenu menuEdit = new JMenu(bundle.getString("Edit"));
		menuEdit.add(actionCopy);
		menuBar.add(menuEdit);
		
		if(OS.isMacOS()) 
			menuFile = new JMenu(bundle.getString("View_menu_osx"));
		else
			menuFile = new JMenu(bundle.getString("View_menu"));
		
		setJMenuBar(menuBar);
	}


	private void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(400, 400);
	
		setResizable(true); 
		setTitle(title);
		setName(title);
		
		final BufferedImage applicationIcon;
		/*try {
			applicationIcon = ImageIO.read(ResourceLoader.getResource(bundle.getString("application_icon")));
			setIconImage(applicationIcon);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
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

	public ActionCopy getActionCopy() {
		return this.actionCopy;
	}

	public ActionExport getActionExport() {
		return this.actionExport;
	}

	public FilesPanel getFilesPanel() {
		return this.filesPanel;
	}

	/**
	 * Sets the image in the Canvas to the specified Image.
	 * @param image
	 */
	public void setImage(final DDSImageFile image) {
		getCanvas().setSourceBI(image.getData());
		filesPanel.getInfoPanel().setDDSFile(image);
		this.actionCopy.setEnabled(true);
	}
	
	/**
	 * Sets the icon on the Canvas to "busy"
	 */
	public void setBusyImage() {
		this.actionCopy.setEnabled(false);
		getCanvas().setSourceBI(busy);
	}
	
}
