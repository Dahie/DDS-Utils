/**
 * 
 */
package de.danielsenff.radds.view;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.tree.TreeNode;

import Model.DDSFile;
import Model.DDSImageFile;
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
		final FileSystemTree fileTree = new FileSystemTree();
		fileTree.addMouseListener(new LoadListener());
		fileTree.addKeyListener(new LoadListener());

		final JScrollPane treeScroller = new JScrollPane(fileTree);


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

		public void keyPressed(final KeyEvent event) {
			if(event.getKeyCode() == KeyEvent.VK_ENTER) {
				final FileSystemTree fileTree = (FileSystemTree) event.getSource();
				final TreeNode node = (TreeNode) fileTree.getSelectionPath().getLastPathComponent();

				loadImage(node);
			}
		}

		public void keyReleased(final KeyEvent e) {
		}

		public void keyTyped(final KeyEvent e) {
		}

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
			if(filename.toLowerCase().contains(".dds") && !file.isDirectory()) {
				try {
					if(DDSImageFile.isValidDDSImage(file)) {

						DDSFile ddsfile = new DDSFile(filename);
						if(ddsfile.getTextureType() == DDSFile.TextureType.CUBEMAP ||
								ddsfile.getTextureType() == DDSFile.TextureType.VOLUME) {
							JOptionPane.showMessageDialog(null, 
									"<html>Error: This programm doesn't support cubemaps or volume textures." +
									"<br>"+ddsfile.getFile().getName()+" can not be loaded.</html>",	"Attention", 
									JOptionPane.INFORMATION_MESSAGE);
							return;
						} 
						
						DDSImageFile image = new DDSImageFile(filename);
						controller.getView().setImage(image);
						final long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
						System.out.println(mem0);
					}
				} catch (final OutOfMemoryError ex) {
					ex.printStackTrace();
					final long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
					JOptionPane.showMessageDialog(controller.getView(), 
							"<html>Error: Out of memory: " + mem0 +
							"<br>The operation is aborted. </html>",	"Error", 
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		public void mouseEntered(final MouseEvent arg0) {
		}

		public void mouseExited(final MouseEvent arg0) {
		}

		public void mousePressed(final MouseEvent arg0) {
		}

		public void mouseReleased(final MouseEvent arg0) {
		}

	}

}