/**
 * 
 */
package org.dds_utils.radds;

import org.dds_utils.radds.models.ClipImage;
import org.dds_utils.radds.models.FilesListModel;
import org.dds_utils.radds.tasks.LoadImageTask;
import org.dds_utils.radds.util.OS;
import org.dds_utils.radds.view.FilesPanel;
import org.dds_utils.radds.view.InfoPanel;
import org.dds_utils.radds.view.canvas.BICanvas;
import org.dds_utils.radds.view.canvas.CanvasControlsPanel;
import org.model.TextureImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


/**
 * @author danielsenff
 *
 */
public class RaddsView extends JFrame implements Runnable  {

	private FilesListModel openFilesModel;
	
	private final FilesPanel filesPanel;
	private File file;
	private final CanvasControlsPanel canvasPanel;
	private static JProgressBar progressBar;
	

	private boolean opened = false;
	private boolean modified = false;
	private final boolean paste = false;

	private File chooserDirectory;
	
	private TextureImage textureImage;

	public RaddsView() {

	}

	private JMenuBar newMenuBar() {
		final JMenuBar menuBar = new JMenuBar();

		String menuName = OS.isMacOS() ? "file.osx.menu" : "file.menu";
		JMenu menuFile = new JMenu(getResourceString(menuName));
		menuFile.add(getAction("open"));
		menuFile.add(getAction("export"));
		menuBar.add(menuFile);
		
		final JMenu menuEdit = new JMenu(getResourceString("edit.menu"));
		menuEdit.add(getAction("copy"));
		menuBar.add(menuEdit);
		
		JMenu menuHelp = new JMenu(getResourceString("help.menu"));
		menuHelp.add(getAction("openAuthorURL"));
		menuHelp.add(getAction("openWebsiteURL"));
		menuHelp.add(getAction("openBugTrackerURL"));
		menuBar.add(menuHelp);
		return menuBar;
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
	 * Returns the currently opened {@link TextureImage}.
	 * @return
	 */
	public TextureImage getTextureImage() {
		return this.textureImage;
	}
	
	/**
	 * Sets the image in the Canvas to the specified {@link TextureImage}.
	 * @param image
	 */
	public void setImage(final TextureImage image) {
		TextureImage oldvalue = this.textureImage;
		this.textureImage = image;
		setOpened(true);
		setFile(image.getFile());
		getInfoPanel().setTextureFile(image);
		this.canvasPanel.updateNumMipMaps();
		firePropertyChange("textureImage", oldvalue, this.textureImage);
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
	

	
	/**
	 * Copy the opened image
	 */
	@Action
	(enabledProperty = "opened")
	public void copy() { 
		/* Returns the currently loaded {@link BufferedImage} shown on the canvas.
		 * This may be different from the origin Image, as the canvas 
		 * may be only displaying certain channels.
		 */
		BufferedImage bi = getCanvas().getCanvas();
		Clipboard myClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();	
		ClipImage ci = new ClipImage(bi); 
		myClipboard.setContents(ci, new ClipboardOwner() {
			public void lostOwnership(Clipboard clipboard, Transferable contents) {	}
		});
	}
	
	/**
	 * Export the opened image
	 */
	@Action
	(enabledProperty = "opened")
	public void export() {
		final JFileChooser fileChooser = newExportFileChooser();

		if(fileChooser.showSaveDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			try {
				File file = fileChooser.getSelectedFile();
				chooserDirectory = file.getParentFile();
				// TODO if(file.exists()= blabla 
				
				if(file.getName().toLowerCase().contains(".png")) {
					//Get source image including alpha.
					BufferedImage bi = getCanvas().getSource();
					// TODO maybe option to say whether to save with or without alpha
					// Save as PNG
					ImageIO.write(bi, "png", file);
				} else if(file.getName().toLowerCase().contains(".jpg") || file.getName().toLowerCase().contains(".jpeg")) {
					//Get source image including alpha.
					BufferedImage bi = getCanvas().getCanvas();
					// TODO maybe option to say whether to save with or without alpha
					// Save as jpg
					ImageIO.write(bi, "jpg", file);
				}
				
			} catch (IOException e) {
				final String msg = getResourceMap().getString("exportFailedMessage", getFile()) + e.getMessage();
				final String title = getResourceMap().getString("exportFailedTitle");
				final int type = JOptionPane.ERROR_MESSAGE;
				RaddsView.getProgressBar().setIndeterminate(false);
				RaddsView.getProgressBar().setVisible(false);
				JOptionPane.showMessageDialog(getFrame(), msg, title, type);
			}
		}
	}

	private JFileChooser newExportFileChooser() {
		final JFileChooser fileChooser = new JFileChooser(chooserDirectory);
		fileChooser.setSelectedFile(newDefaultFile());
		fileChooser.setFileFilter(new FileNameExtensionFilter("JPEG", "jpg"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("PNG", "png"));
		return fileChooser;
	}

	private File newDefaultFile() {
		String currentFileName = getFile().getName();
		String filename = currentFileName.substring(0, currentFileName.lastIndexOf('.')) + ".png";
		return new File(filename);
	}
	
	/**
	 * Open a texture image file via FileChooser dialogue.
	 * @return
	 */
	@Action
	(block = BlockingScope.APPLICATION)
	public LoadImageTask open() {
		final JFileChooser fileChooser = newOpenFileChooser();

		LoadImageTask task = null;
		if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			this.setModified(false);
			task = new LoadImageTask(fileChooser.getSelectedFile());
		}
		return task;		
	}

	private JFileChooser newOpenFileChooser() {
		final JFileChooser fileChooser = new JFileChooser();
		final String[] allExtensions = {"dds", "tga", "bmp", "gif", "jpg", "jpeg"};
		fileChooser.setFileFilter(new FileNameExtensionFilter("DirectDrawSurface texture", "dds"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("GrandPrix4 TEX texture", "tex"));
		fileChooser.setFileFilter(new FileNameExtensionFilter("All supported images", allExtensions));
		return fileChooser;
	}
	
	/**
     * Opens the URL to the BugTracker.
     */
    @Action 
    public void openBugTrackerURL() {
		OS.openURLinDefaultBrowser(getResourceString("help.menu.bugtracker_url"));
    }
    
    /**
     * Opens the URL to the BugTracker.
     */
    @Action 
    public void openAuthorURL() {
		OS.openURLinDefaultBrowser(getResourceString("help.menu.author_url"));
    }
    
    /**
     * Opens the URL to the BugTracker.
     */
    @Action 
    public void openWebsiteURL() {
		OS.openURLinDefaultBrowser(getResourceString("help.menu.website_url"));
    }

	@Override
	public void run() {

	}
}
