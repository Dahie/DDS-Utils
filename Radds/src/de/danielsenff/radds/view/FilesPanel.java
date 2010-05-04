/**
 * 
 */
package de.danielsenff.radds.view;


import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import util.FileUtil;
import de.danielsenff.radds.controller.Application;

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
		final FileSystemTree fileTree = new FileSystemTree();
		fileTree.setExpandsSelectedPaths(true);
		fileTree.addMouseListener(new LoadListener());
		fileTree.addKeyListener(new LoadListener());

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
			
		}

		public void keyReleased(final KeyEvent e) {}
		public void keyTyped(final KeyEvent e) {}

		public void mouseClicked(final MouseEvent click) {
			if(click.getClickCount() == 2) {
				final FileSystemTree fileTree = (FileSystemTree) click.getSource();
				final TreeNode node = (TreeNode) fileTree.getSelectionPath().getLastPathComponent();
				loadImage(node);
			}
		}

		private void loadImage(final TreeNode node) {
			controller.getView().setBusyImage();

			final String filename = node.toString();
			final File file = new File(filename);
			if( (FileUtil.getFileSuffix(file).contains("dds") 
					|| FileUtil.getFileSuffix(file).contains("tex") )
					&& !file.isDirectory()) {
				controller.setImage(file);
			}
		}

		public void mouseEntered(final MouseEvent arg0) {}
		public void mouseExited(final MouseEvent arg0) {}
		public void mousePressed(final MouseEvent arg0) {}
		public void mouseReleased(final MouseEvent arg0) {}
	}
}