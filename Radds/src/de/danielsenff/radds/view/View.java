/**
 * 
 */
package de.danielsenff.radds.view;

import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;

import de.danielsenff.radds.actions.ActionAddFile;
import de.danielsenff.radds.actions.ActionCloseAll;
import de.danielsenff.radds.actions.ActionCopy;
import de.danielsenff.radds.actions.ActionExitApplication;
import de.danielsenff.radds.actions.ActionExport;
import de.danielsenff.radds.actions.ActionRemoveFile;
import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.util.OS;
import de.danielsenff.radds.util.ResourceLoader;
import de.danielsenff.radds.view.canvas.BICanvas;
import de.danielsenff.radds.view.canvas.CanvasControlsPanel;


import Model.DDSImageFile;


/**
 * @author danielsenff
 *
 */
public class View extends JCFrame {

	final String title = bundle.getString("application_title");
	private final JProgressBar progressbar;

	private ActionAddFile actionOpenFile;
	private ActionRemoveFile actionCloseFile;
	private ActionCopy actionCopy;
	private ActionExport actionExport;
	private FilesPanel filesPanel;
	private ActionCloseAll actionCloseAllFiles;
	private CanvasControlsPanel canvasPanel;
	
	private BufferedImage busy;
	
	/**
	 * 
	 */
	public View(final Application controller) {
		super(controller);
		
		try {
			this.busy = ImageIO.read(new File("de/danielsenff/radds/resources/defaultimage.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		initFrame();
		
		// init actions
		initActions(controller);
		
		// menu
		initMenu(controller);
		
		// general layout
		filesPanel = new FilesPanel(controller);
		
//		body.add(scrollViewPane, 
//				new GBConstraints(1,2,GridBagConstraints.BOTH,GridBagConstraints.CENTER, 1.0, 1.0));
		
		canvasPanel = new CanvasControlsPanel(controller);
		JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filesPanel, canvasPanel);
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
		/*actionOpenFile = new ActionAddFile(controller);
		actionExport = new ActionExport(controller);
		actionExport.setEnabled(false);
		actionCloseFile = new ActionRemoveFile(controller);
		actionCloseFile.setEnabled(false);
		actionCloseAllFiles = new ActionCloseAll(controller);
		actionCloseAllFiles.setEnabled(false);*/
		actionCopy = new ActionCopy(controller);
		actionCopy.setEnabled(false);
	}

	private void initMenu(final Application controller) {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu menuFile;
		if(OS.isMacOS()) 
			menuFile = new JMenu(bundle.getString("File_menu_osx"));
		else
			menuFile = new JMenu(bundle.getString("File_menu"));
		
		/*menuFile.add(actionOpenFile);
		menuFile.add(new JSeparator());
		menuFile.add(actionCloseFile);
		menuFile.add(actionCloseAllFiles);
		menuFile.add(new JSeparator());
		menuFile.add(actionExport);
		
		if (OS.isWindows()) {
			menuFile.add(new JSeparator());
			menuFile.add(new ActionExitApplication(controller));
		}
		
		menuBar.add(menuFile);*/
		
		
		
		JMenu menuEdit = new JMenu(bundle.getString("Edit"));
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
		
		BufferedImage applicationIcon;
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

	public ActionAddFile getActionAddFile() {
		return this.actionOpenFile;
	}

	public ActionRemoveFile getActionCloseFile() {
		return this.actionCloseFile;
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

	public ActionCloseAll getActionCloseAllFiles() {
		return this.actionCloseAllFiles;
	}

	public void setImage(DDSImageFile image) {
		getCanvas().setSourceBI(image.getData());
		filesPanel.getInfoPanel().setDDSFile(image);
		this.actionCopy.setEnabled(true);
	}
	
	public void setBusyImage() {
		this.actionCopy.setEnabled(false);
		getCanvas().setSourceBI(busy);
	}
	
}
