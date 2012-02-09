/**
 * 
 */
package de.danielsenff.badds.view;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTabbedPane;

import de.danielsenff.badds.actions.ActionAddFile;
import de.danielsenff.badds.actions.ActionCancelSaveAll;
import de.danielsenff.badds.actions.ActionClearFilelist;
import de.danielsenff.badds.actions.ActionExitApplication;
import de.danielsenff.badds.actions.ActionImportFolder;
import de.danielsenff.badds.actions.ActionPreview;
import de.danielsenff.badds.actions.ActionRemoveFile;
import de.danielsenff.badds.actions.ActionSaveAll;
import de.danielsenff.badds.actions.ActionSelectAll;
import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.util.OS;
import de.danielsenff.badds.util.ResourceLoader;
import de.danielsenff.badds.util.FileDrop.FileDrop;
import de.danielsenff.badds.view.GUI.FilesTable;
import de.danielsenff.badds.view.GUI.InfoPanel;
import de.danielsenff.badds.view.GUI.JCFrame;
import de.danielsenff.badds.view.GUI.JToolbar;
import de.danielsenff.badds.view.GUI.SettingsPanel;
import de.danielsenff.badds.view.GridBagConstraints.GBConstraints;
import de.danielsenff.badds.view.worker.WorkingView;




/**
 * @author danielsenff
 *
 */
public class View extends JCFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	final String title = bundle.getString("application_title");
	
	private final WorkingView workingView;
	
	private SettingsPanel scalePanel;
	private FilesTable openFilesTable;
	private JTabbedPane tabOperations;

	private ActionPreview actionPreview;
	private ActionSaveAll actionSaveAll;
	private ActionRemoveFile actionRemoveFile;
	private ActionImportFolder actionImportFolder;
	private ActionAddFile actionAddFile;
	private ActionClearFilelist actionClearFilelist;
	private ActionSelectAll actionSelectAll;
	
	private ActionCancelSaveAll actionCancelSaveAll;

	


	/**
	 * 
	 */
	public View(final Application controller) {
	
		super(controller);
		this.workingView = new WorkingView(this);
		initFrame();
		getContentPane().setLayout(new BorderLayout());
		
		initActions();
		
		initMenu();
		
		JPanel panel = new JPanel(new GridBagLayout());
//		panel.add(new JLabel(ResourceLoader.getResourceIcon("/Resource/ajax-loader.gif")), new RemainderConstraintsNoFill());
		
		openFilesTable = new FilesTable(controller);
		panel.add(openFilesTable,	
				new GBConstraints(4,1,GridBagConstraints.BOTH,GridBagConstraints.CENTER, 0.5, 1));
		
		tabOperations = new JTabbedPane();
		this.scalePanel = new SettingsPanel(controller);
		tabOperations.add(bundle.getString("Settings"), scalePanel);
		tabOperations.add(bundle.getString("Info"), new InfoPanel(controller));
		panel.add(tabOperations, 
				new GBConstraints(GridBagConstraints.REMAINDER,1,GridBagConstraints.HORIZONTAL,GridBagConstraints.PAGE_START, 0.0, 0.7));
		
		
		
		Icon icon = new ImageIcon("ajax-loader.gif");
        JLabel labelAnimation = new JLabel(icon);
//        panel.add(labelAnimation, new RemainderConstraintsNoFill());
		
		getContentPane().add(panel, BorderLayout.CENTER);
		
		
		new FileDrop( System.out, this.getOpenFilesTable(), /*dragBorder,*/ new FileDrop.Listener()
        {   
			@Override
			public void filesDropped( java.io.File[] files )
            {   
        		for( int i = 0; i < files.length; i++ )
                {   
            		controller.addFile(files[i]);
                }   // end for: through each dropped file
            }   // end filesDropped
        }); // end FileDrop.Listener
		
		setVisible(true);
		pack();
		
	}

	
	/**
	 * 
	 */
	private void initActions() {
		actionAddFile = new ActionAddFile(controller);
		actionImportFolder = new ActionImportFolder(controller);
		
		actionPreview = new ActionPreview(controller);
		actionPreview.setEnabled(false);
		
		actionSaveAll = new ActionSaveAll(controller);
		actionSaveAll.setEnabled(false);
		
		actionCancelSaveAll = new ActionCancelSaveAll(controller);
		actionCancelSaveAll.setEnabled(false);
		
		actionRemoveFile = new ActionRemoveFile(controller);
		actionRemoveFile.setEnabled(false);
		
		actionClearFilelist = new ActionClearFilelist(controller);
		actionClearFilelist.setEnabled(false);

//		actionSelectAll = new ActionSelectAll(controller);
//		actionSelectAll.setEnabled(false);
		
	}

	/**
	 * 
	 */
	private void initMenu() {
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menuFile; 
		if (OS.isMacOS()) {
			menuFile = new JMenu(bundle.getString("File_menu_osx"));
		} else {
			menuFile = new JMenu(bundle.getString("File_menu"));
		}
		
		menuFile.add(actionAddFile).setIcon(null);
		menuFile.add(actionImportFolder).setIcon(null);
		
		menuFile.addSeparator();
		menuFile.add(actionSaveAll).setIcon(null);
		
		if(!OS.isMacOS()) {
			menuFile.add(new JSeparator());
			menuFile.add(new ActionExitApplication(controller)).setIcon(null);
		}
		
		JMenu menuEdit = new JMenu(bundle.getString("Edit_menu"));
		menuEdit.add(actionRemoveFile).setIcon(null);
		menuEdit.add(actionClearFilelist).setIcon(null);
//		menuEdit.add(actionSelectAll);
		
		
		JMenu menuView;
		if (OS.isMacOS()) {
			menuView = new JMenu(bundle.getString("View_menu_osx"));
		} else {
			menuView = new JMenu(bundle.getString("View_menu"));
		}
		menuView.add(actionPreview).setIcon(null);
		
		
		menuBar.add(menuFile);
		menuBar.add(menuEdit);
		menuBar.add(menuView);
		menuBar.setVisible(true);
		setJMenuBar(menuBar);
		
		
		JToolbar bar = new JToolbar();
		bar.setMargin(new Insets(6,6,6,6));
		bar.setFloatable(false);
		bar.setBorderPainted(false);
		
		bar.add(actionAddFile);
		bar.add(actionImportFolder);
		bar.addSeparator();
		bar.add(actionRemoveFile);
		bar.add(actionClearFilelist);
		bar.addSeparator();
		bar.add(actionPreview);
		bar.addSeparator();
		bar.add(actionSaveAll);
		
		add(bar, BorderLayout.NORTH);
	}


	private void initFrame() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationByPlatform(true);
		setSize(400, 400);
		setResizable(true); 
		setTitle(title);
		setName(title);
		BufferedImage applicationIcon;
		try {
			applicationIcon = ImageIO.read(ResourceLoader.getResource(bundle.getString("application_icon")));
			setIconImage(applicationIcon);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}

	
	/**
	 * 
	 */
	public void setNewHeight(final int newHeight) {
		this.scalePanel.setNewHeight(newHeight);
	}

	public void setNewWidth(final int newWidth) {
		this.scalePanel.setNewWidth(newWidth);
	}

	public FilesTable getOpenFilesTable() {
		return this.openFilesTable;
	}

	public ActionPreview getActionPreview() {
		return this.actionPreview;
	}

	public ActionSaveAll getActionSaveAll() {
		return this.actionSaveAll;
	}

	public ActionRemoveFile getActionRemoveFile() {
		return this.actionRemoveFile;
	}

	public ActionImportFolder getActionImportFolder() {
		return this.actionImportFolder;
	}

	public ActionAddFile getActionAddFile() {
		return this.actionAddFile;
	}

	public ActionClearFilelist getActionClearFilelist() {
		return this.actionClearFilelist;
	}

	public JTabbedPane getTabOperations() {
		return this.tabOperations;
	}

	public ActionCancelSaveAll getActionCancelSaveAll() {
		return this.actionCancelSaveAll;
	}

	public WorkingView getWorkingView() {
		return this.workingView;
	}

	public ActionSelectAll getActionSelectAll() {
		return this.actionSelectAll;
	}

}
