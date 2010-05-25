/**
 * 
 */
package de.danielsenff.radds;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.ActionMap;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JProgressBar;
import javax.swing.JSplitPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import model.TextureImage;

import org.jdesktop.application.Action;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task.BlockingScope;

import de.danielsenff.radds.models.ClipImage;
import de.danielsenff.radds.models.FilesListModel;
import de.danielsenff.radds.tasks.LoadImageTask;
import de.danielsenff.radds.util.OS;
import de.danielsenff.radds.view.FilesPanel;
import de.danielsenff.radds.view.InfoPanel;
import de.danielsenff.radds.view.canvas.BICanvas;
import de.danielsenff.radds.view.canvas.CanvasControlsPanel;


/**
 * @author danielsenff
 *
 */
public class RaddsView extends FrameView {

	private FilesListModel openFilesModel;
	
	private final FilesPanel filesPanel;
	private File file;
	private final CanvasControlsPanel canvasPanel;
	private static JProgressBar progressBar;
	

	private boolean opened = false;
	private boolean modified = false;
	private boolean paste = false;
	
	/**
	 * @param radds 
	 * 
	 */
	public RaddsView(final Radds radds) {
		super(radds);
		
		this.openFilesModel = new FilesListModel();
		
		// menu
		initMenu();
		
		// general layout
		filesPanel = new FilesPanel();
		
		canvasPanel = new CanvasControlsPanel(this);
		final JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, filesPanel, canvasPanel);
		splitPane.setDividerLocation(200);
		splitPane.setContinuousLayout(true);
		splitPane.setResizeWeight(0.0);
		
		this.progressBar = new JProgressBar();
		
		setComponent(splitPane);
	}

	private void initMenu() {
		final JMenuBar menuBar = new JMenuBar();
		
		final ResourceMap resourceMap = getResourceMap();
		
		JMenu menuFile;
		if(OS.isMacOS()) 
			menuFile = new JMenu(resourceMap.getString("file.osx.menu"));
		else
			menuFile = new JMenu(resourceMap.getString("file.menu"));
		menuFile.add(getAction("open"));
		menuBar.add(menuFile);
		
		final JMenu menuEdit = new JMenu(resourceMap.getString("edit.menu"));
		menuEdit.add(getAction("copy"));
		menuBar.add(menuEdit);
		
		setMenuBar(menuBar);
	}
	
	/**
	 * @return the progressbar
	 */
	public static JProgressBar getProgressbar() {
		return progressBar;
	}

	/**
	 * Get the BufferedImage Canvas of this view.
	 * @return
	 */
	public BICanvas getCanvas() {
		return this.canvasPanel.getCanvas();
	}

	/**
	 * Unused FileList of opened files. Currently only one opened file is supported.
	 * @return
	 */
	public FilesListModel getFilesListModel() {
		return this.openFilesModel;
	}
	
	/**
	 * @return the progressBar
	 */
	public static synchronized final JProgressBar getProgressBar() {
		return progressBar;
	}

	/**
	 *  Set the bound file property and update the GUI.
	 * @param file 
     */
    public void setFile(final File file) {
    	File oldValue = this.file;
    	this.file = file;
    	String appId = getResourceString("Application.id");
    	getFrame().setTitle(file.getName() + " - " + appId);
    	firePropertyChange("file", oldValue, this.file);	
    }

    /**
     * True if the file value has been modified but not saved.  The 
     * default value of this property is false.
     * <p>
     * This is a bound read-only property.  
     * 
     * @return the value of the modified property.
     * @see #isModified
     */
    public boolean isModified() { 
        return this.modified;
    }
	
    /**
     * Sets the modified flag.
     * @param modified
     */
    public void setModified(final boolean modified) {
        boolean oldValue = this.modified;
        this.modified = modified;
        // on program start, file may not be initialized
        if(file != null){
        	String appId = getResourceString("Application.id");
            String changed = modified ? "*" : "";
        	getFrame().setTitle(file.getName() + changed +" - " + appId);	
        }
        firePropertyChange("modified", oldValue, this.modified);
    }
	
	/**
	 * @return the fileOpened
	 */
	public final boolean isOpened() {
		return opened;
	}

	/**
	 * @param fileOpened the fileOpened to set
	 */
	public final void setOpened(boolean fileOpened) {
		boolean oldvalue = this.opened;
		this.opened = fileOpened;
		firePropertyChange("opened", oldvalue, this.opened);
	}
    
	/**
	 * Returns the currently loaded image-file.
	 * @return
	 */
	public File getFile() {
		return file;
	}
	
	/**
	 * Returns the currently loaded {@link BufferedImage} shown on the canvas.
	 * This may be different from the origin Image, as the canvas 
	 * may be only displaying certain channels.
	 * @return
	 */
	public BufferedImage getImage() {
		return getCanvas().getCanvas();
	}
	
	/**
	 * Sets the image in the Canvas to the specified {@link TextureImage}.
	 * @param image
	 */
	public void setImage(final TextureImage image) {
		getCanvas().setSourceBI(image.getData());
		setOpened(true);
		setFile(image.getFile());
		getInfoPanel().setTextureFile(image);
	}

	/**
	 * Sets the image in the Canvas to the specified {@link BufferedImage}.
	 * @param image
	 * @param file 
	 */
	public void setImage(final BufferedImage image, final File file) {
		getCanvas().setSourceBI(image);
		setOpened(true);
		setFile(file);
		getInfoPanel().setTextureFile(image);
	}
	
	private InfoPanel getInfoPanel() {
		return filesPanel.getInfoPanel();
	}
	
	private String getResourceString(final String key) {
		return getResourceMap().getString(key);
	}
	
	
	/*
	 * Action definitions
	 * 
	 */
	
	/**
	 * Convenience function for getting the {@link Action} by the given name.
	 * @param actionName
	 * @return
	 */
	public javax.swing.Action getAction(final String actionName) {
		ActionMap actionMap = getContext().getActionMap(RaddsView.class, this);
	    return actionMap.get(actionName);
	}
	
	/**
	 * Copy the opened image
	 */
	@Action
	(enabledProperty = "opened")
	public void copy() { 
		BufferedImage bi = getImage();
		Clipboard myClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();	
		ClipImage ci = new ClipImage(bi); 
		myClipboard.setContents(ci, new ClipboardOwner() {
			public void lostOwnership(Clipboard clipboard, Transferable contents) {	}
		});
	}
	
	/**
	 * Open a texture image file via FileChooser dialogue.
	 * @return
	 */
	@Action
	(block = BlockingScope.APPLICATION)
	public LoadImageTask open() {
		/* unused so far, as radds doesn't modify
		 * if(isModified()) {
			final int optionSave = showSaveConfirmation();
			if(optionSave == JOptionPane.OK_OPTION) {
				save().run();
			}else if(optionSave == JOptionPane.CANCEL_OPTION) {
				return null;
			}
		} */
		final JFileChooser fc = new JFileChooser();
        final String[] allExtensions = {"dds", "tga", "bmp", "gif", "jpg", "jpeg"};
        fc.setFileFilter(new FileNameExtensionFilter("DirectDrawSurface texture", "dds"));
        fc.setFileFilter(new FileNameExtensionFilter("GrandPrix4 TEX texture", "tex"));
        fc.setFileFilter(new FileNameExtensionFilter("All supported images", allExtensions));

		LoadImageTask task = null;
		final int option = fc.showOpenDialog(null);
		if (option == JFileChooser.APPROVE_OPTION) {
			this.setModified(false);
			task = new LoadImageTask(fc.getSelectedFile());
		}
		return task;		
	}
}
