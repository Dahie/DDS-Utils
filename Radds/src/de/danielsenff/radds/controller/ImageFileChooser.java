/**
 * 
 */
package de.danielsenff.radds.controller;

import java.awt.GridLayout;
import java.io.File;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.filechooser.FileFilter;

import util.FileUtil;

import JOGL.DDSImage;
import Model.DDSFile;
import de.danielsenff.radds.models.ExampleFileFilter;


/**
 * @author danielsenff
 */
public class ImageFileChooser extends JFileChooser {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int selectedCompressionIndex;
	private JComboBox compressionList;
	
	private FileFilter ddsFilter;
	private FileFilter texFilter;
	private FileFilter bmpFilter;
	private FileFilter jpgFilter;
	
	protected static final String[] compressions = {
			"Uncompressed", "DXT1", "DXT3", "DXT5"
	};
	final static int UNCOMPRESSED = 0;
	final static int DXT1 = 1;
	final static int DXT3 = 2;
	final static int DXT5 = 3;
	
	private ResourceBundle bundle = Application.getBundle();
	
	
	/**
	 * 
	 */
	public ImageFileChooser() {
		super();
		init();
	}
	
	public ImageFileChooser(final File currentDirectory) {
		super(currentDirectory);
		init();
	}
	
	public ImageFileChooser(final String currentDirectoryPath) {
		super(currentDirectoryPath);
		init();
	}
	

	private void init() {
		this.setMultiSelectionEnabled(true);
		this.setAcceptAllFileFilterUsed(false);
		this.setAccessory(compressionSettings());
		this.getAccessory().setVisible(false);

		this.ddsFilter = new ExampleFileFilter("dds", "Direct Draw Surface texture");
		this.texFilter = new ExampleFileFilter("tex", "Tex texture");
        this.bmpFilter = new ExampleFileFilter("bmp", "Windows Bitmap");
        String[] jpg= {"jpg", "jpeg" };
        this.jpgFilter = new ExampleFileFilter(jpg, "JPEG");
		
	}

	public void setSelectedCompression(final int index) {
		this.compressionList.setSelectedIndex(index);
		this.compressionList.revalidate();
	}
	
	public int getSelectedBICompression() {
		switch(this.compressionList.getSelectedIndex()) {
		default:
		case 0: // uncompressed
			break;
		case 1: // DXT1
			return DDSImage.D3DFMT_DXT1;
		case 2: // DXT3
			return DDSImage.D3DFMT_DXT3;
		case 3: // DXT5
			return DDSImage.D3DFMT_DXT5;
		}

		return DDSImage.D3DFMT_A8R8G8B8;
	}
	
	public int getSelectedDDSImageCompression() {
		switch(this.compressionList.getSelectedIndex()) {
		default:
		case 0: // uncompressed
			break;
		case 1: // DXT1
			return DDSImage.D3DFMT_DXT1;
		case 2: // DXT3
			return DDSImage.D3DFMT_DXT3;
		case 3: // DXT5
			return DDSImage.D3DFMT_DXT5;
		}
	
		return DDSImage.D3DFMT_A8R8G8B8;
	}
	
	private int selectedTypeFromBI(int compression) {

		switch(compression) {
		default:
		case DDSImage.D3DFMT_A8R8G8B8: // uncompressed
		case DDSImage.D3DFMT_R8G8B8: // uncompressed
		case DDSImage.D3DFMT_X8R8G8B8: // uncompressed
			return UNCOMPRESSED;
		case DDSImage.D3DFMT_DXT1: // DXT1
			return DXT1;
		case DDSImage.D3DFMT_DXT3: // DXT3
			return DXT3;
		case DDSImage.D3DFMT_DXT5: // DXT5
			return DXT5;
		}
	}
	
	
	private JComponent compressionSettings() {
		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(0,1));
		
		
		Border paneEdge = BorderFactory.createEmptyBorder(0,5,5,5);
		JLabel lblCompression = new JLabel(bundle.getString("Compression")+":");
		
		lblCompression.setBorder(paneEdge);
		JLabel lblMipmaps = new JLabel(bundle.getString("MipMaps")+":");
		//TODO checkbox for mipmaps
		lblMipmaps.setBorder(paneEdge);
		
		
		
		compressionList = new JComboBox(compressions);
		lblCompression.setLabelFor(compressionList);
		
		panel.add(lblCompression);
		panel.add(compressionList);
		//panel.add(lblMipmaps);
		
		
		return panel;
	}

	
	public File showFileDialog() {

		addChoosableFileFilter(ddsFilter);
        getAccessory().setVisible(false);
        this.setMultiSelectionEnabled(false);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.setDialogTitle(bundle.getString("Open_file"));
		this.setApproveButtonText(bundle.getString("Open_file"));
        final int res = showOpenDialog(null);
        
        if (res == JFileChooser.APPROVE_OPTION) {
        	
            final File file = getSelectedFile();
            return file;
        } 
		return null;
    }
	
	public File[] showFilesDialog() throws NullPointerException {

		addChoosableFileFilter(ddsFilter);
		setMultiSelectionEnabled(true);
		getAccessory().setVisible(false);
		this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		this.setDialogTitle(bundle.getString("Open_files"));
		this.setApproveButtonText(bundle.getString("Open_files"));
		final int res = showOpenDialog(null);
		
		if (res == JFileChooser.APPROVE_OPTION) {
			final File[] files = getSelectedFiles();
			return files;
		} 
		return null;
	}
	
	public File showDirectoryDialog() {
		
        getAccessory().setVisible(false);
        this.setMultiSelectionEnabled(false);
        super.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        this.setDialogTitle(bundle.getString("Import_files_from_folder"));
		this.setApproveButtonText(bundle.getString("Import"));
        final int res = showOpenDialog(null);
        
        if (res == JFileChooser.APPROVE_OPTION) {
            final File file = super.getSelectedFile();
            if(file.isDirectory())
            	return file;
        } 
		return null;
	}
	
	public File showSaveDialogue(DDSFile ddsImageFile) {
	 	
		removeChoosableFileFilter(bmpFilter);
		
		getAccessory().setVisible(true);
		this.setMultiSelectionEnabled(false);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		setSelectedCompression(ddsImageFile.getPixelformat());
		
        int res = showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = getSelectedFile();
            if (!FileUtil.getFileSuffix(file).contains("dds")) {
            	file = new File(file.getAbsoluteFile() + ".dds");
            }
            
//            System.out.println("File selected: " + file.getAbsolutePath());
            return file;
        } 
		return null;
    }
	
	
	public File showExportDialogue() {

		removeChoosableFileFilter(ddsFilter);
		addChoosableFileFilter(bmpFilter);
		addChoosableFileFilter(jpgFilter);
		getAccessory().setVisible(false);
		this.setMultiSelectionEnabled(false);
        this.setFileSelectionMode(JFileChooser.FILES_ONLY);
		
        int res = showSaveDialog(null);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = getSelectedFile();
            return file;
        } 
		return null;
    }
	
}
