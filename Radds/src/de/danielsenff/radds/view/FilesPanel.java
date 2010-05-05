/**
 * 
 */
package de.danielsenff.radds.view;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileFilter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import util.FileUtil;
import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.models.FileNode;
import de.danielsenff.radds.models.FileTreeModel;

/**
 * @author danielsenff
 *
 */
public class FilesPanel extends JCPanel {

	private JList list;
	static int panelWidth = 200;
	static int panelHeight = 300;
	private InfoPanel infoPanel;

	/**
	 * 
	 */
	public FilesPanel(final Application controller) {
		super(controller);
		this.setLayout(new BorderLayout());
		init();
	}

	private void init() {
		// FileViewTree
		final FileSystemView fileSystemView = FileSystemView.getFileSystemView();
		FileTreeModel fileTreeModel = new FileTreeModel(fileSystemView.getHomeDirectory(), new FileFilter() {

			public boolean accept(File file) {
				String[] extensions = {"dds", "tga", "tex"};
				if (!file.isHidden() && !file.isDirectory())
					return FileUtil.isExtension(file, extensions);
				else if (file.isDirectory()) 
					return true;
				else 
					return false;
			}
			
		});
		final JTree fileTree = new JTree(fileTreeModel);
		fileTree.setExpandsSelectedPaths(true);
		fileTree.addMouseListener(new LoadListener());
		fileTree.addKeyListener(new LoadListener());
		fileTree.setCellRenderer(new DefaultTreeCellRenderer() 
		{
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, 
					boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
			{
				JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				File f = ((FileNode)value).getFile();
				label.setText(fileSystemView.getSystemDisplayName(f));
				label.setIcon(fileSystemView.getSystemIcon(f));
				return label;
			}
		});
		fileTree.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				TreePath selectionPath = fileTree.getSelectionPath();
				if (	(event.isMetaDown() || event.isControlDown())
						&& event.getKeyCode() == KeyEvent.VK_R	) {
					fileTree.setModel(new FileTreeModel(fileSystemView.getHomeDirectory()));
					//updateTreeNodes();
					//preparePath(selectionPath, 1, (DefaultMutableTreeNode) getModel().getRoot());
					fileTree.expandPath(selectionPath);
					fileTree.setSelectionPath(selectionPath);
					fileTree.invalidate();
				}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});

		final JScrollPane treeScroller = new JScrollPane(fileTree);
		add(treeScroller, BorderLayout.CENTER);

		// information
		JButton gotoButton = new JButton("Go to…");
		gotoButton.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String url = JOptionPane.showInputDialog("Please enter the URL to the file.");
				url.trim();
				try {
					File file = new File(url);
					controller.setImage(file);
				} catch (Exception ex) {
					
				}
			}
			
		});
		this.add(gotoButton, BorderLayout.NORTH);
		
		// information
		infoPanel = new InfoPanel(controller);
		this.add(infoPanel, BorderLayout.SOUTH);
	}

	/**
	 * @return
	 */
	public InfoPanel getInfoPanel() {
		return this.infoPanel;
	}

	class LoadListener implements KeyListener, MouseListener {

		public void keyPressed(final KeyEvent event) {
			if(event.getKeyCode() == KeyEvent.VK_ENTER) {
				processEvent(event);
			}
			
		}

		public void keyReleased(final KeyEvent e) {}
		public void keyTyped(final KeyEvent e) {}

		public void mouseClicked(final MouseEvent click) {
			if(click.getClickCount() == 2) {
				processEvent(click);
			}
		}

		private void processEvent(final InputEvent click) {
			final JTree fileTree = ((JTree) click.getSource());
			final FileNode node = (FileNode) fileTree.getSelectionPath().getLastPathComponent();
			File file = node.getFile();
			if( !file.isDirectory()) 
				controller.setImage(file);
		}
		
		public void mouseEntered(final MouseEvent arg0) {}
		public void mouseExited(final MouseEvent arg0) {}
		public void mousePressed(final MouseEvent arg0) {}
		public void mouseReleased(final MouseEvent arg0) {}
	}
}