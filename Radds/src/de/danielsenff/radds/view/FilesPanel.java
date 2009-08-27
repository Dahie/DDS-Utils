/**
 * 
 */
package de.danielsenff.radds.view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import de.danielsenff.radds.controller.Application;

import Model.DDSImageFile;

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
		/*list = new JList();
		FilesListModel filesListModel = controller.getFilesListModel();
		filesListModel.addListDataListener(new ListDataListener() {

			public void contentsChanged(ListDataEvent arg0) {}

			public void intervalAdded(ListDataEvent arg0) {
				controller.getView().getActionCloseAllFiles().setEnabled(true);
			}

			public void intervalRemoved(ListDataEvent arg0) {}

		});

		list.setModel(filesListModel);
		list.setSelectionModel(filesListModel.getSelectionModel());
		list.setCellRenderer(new FileCellRenderer());



		list.addListSelectionListener(new ChangeSelection(controller));


		JScrollPane listScroller = new JScrollPane(list);
		listScroller.setPreferredSize(new Dimension(panelWidth, panelHeight));
		this.add(listScroller, BorderLayout.CENTER);
		 */
		// FileViewTree
		FileSystemTree fileTree = new FileSystemTree();
		fileTree.addMouseListener(new LoadListener());
		fileTree.addKeyListener(new LoadListener());

		JScrollPane treeScroller = new JScrollPane(fileTree);


		//Splitpane

		//		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, listScroller, treeScroller);
		//		splitPane.setDividerLocation(0.5);
		//		add(splitPane, BorderLayout.CENTER);
		add(treeScroller, BorderLayout.CENTER);



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

		public void keyPressed(KeyEvent event) {
			if(event.getKeyCode() == KeyEvent.VK_ENTER) {
				FileSystemTree fileTree = (FileSystemTree) event.getSource();
				TreeNode node = (TreeNode) fileTree.getSelectionPath().getLastPathComponent();

				loadImage(node);
			}
		}

		public void keyReleased(KeyEvent e) {
		}

		public void keyTyped(KeyEvent e) {
		}

		public void mouseClicked(MouseEvent click) {
			System.gc();
			if(click.getClickCount() == 2) {
				FileSystemTree fileTree = (FileSystemTree) click.getSource();
				TreeNode node = (TreeNode) fileTree.getSelectionPath().getLastPathComponent();

				loadImage(node);
			}
		}

		private void loadImage(TreeNode node) {
			controller.getView().setBusyImage();
			
			String filename = node.toString();
			File file = new File(filename);
			if(filename.toLowerCase().contains(".dds") && !file.isDirectory()) {
				try {
					DDSImageFile image;
					image = new DDSImageFile(filename);
					controller.getView().setImage(image);
					long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					System.out.println(mem0);
				} catch (OutOfMemoryError ex) {
					ex.printStackTrace();
					long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					JOptionPane.showMessageDialog(controller.getView(), 
							"<html>Error: Out of memory: " + mem0 +
							"<br>The operation is aborted. </html>",	"Error", 
							JOptionPane.ERROR_MESSAGE);
					return;
				}
			}
		}

		public void mouseEntered(MouseEvent arg0) {
		}

		public void mouseExited(MouseEvent arg0) {
		}

		public void mousePressed(MouseEvent arg0) {
		}

		public void mouseReleased(MouseEvent arg0) {
		}

	}

}