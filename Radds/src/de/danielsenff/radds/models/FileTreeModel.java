package de.danielsenff.radds.models;

import java.awt.Component;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

/**
 * {@link TreeModel} for displaying a {@link FileSystemView}.
 * @author dahie
 *
 */
public class FileTreeModel implements TreeModel
{
	private static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();
	private FileNode _rootNode; 
	private FileFilter fileFilter;
	
	/**
	 * @param dir
	 */
	public FileTreeModel(final File dir) { 
		this(dir, new FileFilter() {
			
			@Override
			public boolean accept(File pathname) {
				return true;
			}
		});
	} 

	/**
	 * Constructor takes the root directory of this file tree.
	 * The {@link FileFilter} defines a filter for files to include in the tree. 
	 * @param dir
	 * @param fileFilter
	 */
	public FileTreeModel(final File dir, final FileFilter fileFilter) {
		this.fileFilter = fileFilter;
		_rootNode = new FileNode(dir, fileFilter);
	}
	
	@Override
	public Object getRoot() {
		return _rootNode;
	} 

	private List<File> getFiles(final File directory) {
		return Arrays.asList(directory.listFiles(fileFilter));
	} 
	
	private List<File> getSortedFiles(final File directory) {
		List<File> files = getFiles(directory);
		Collections.sort(files); // just stupid javasorting
		return files;
	}

	@Override
	public int getChildCount(final Object parent) {
		return ((FileNode) parent).getChildrenCount();
	}

	@Override
	public boolean isLeaf(final Object node) {
		return !((FileNode) node).isDirectory() || !((FileNode) node).hasChildren();
	}

	@Override
	public Object getChild(final Object parent, final int index) {
		final FileNode node = ((FileNode) parent);
//		List<File> sortedFiles = getSortedFiles(file);
		return node.isDirectory() ? new FileNode(node.getChild(index), fileFilter) : null;
	}

	@Override
	public int getIndexOfChild(final Object parent, final Object child) {
		 final File file = ((FileNode) child).getFile(); 
		final FileNode node = (FileNode) parent;
		return node.getIndexOfChild(file);
	}
	
	@Override
	public void addTreeModelListener(final TreeModelListener l) {}
	
	@Override
	public void removeTreeModelListener(final TreeModelListener l) {}
	
	@Override
	public void valueForPathChanged(final TreePath path, final Object newValue) {}
	
	/**
	 * Test-Frame for FileTreeModel
	 * @param args
	 */
	public static void main(final String[] args) {
		final File userHomeDir = new File(System.getProperty("user.home"));
		final JTree tree = new JTree(new FileTreeModel(userHomeDir));
		tree.setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(final JTree tree,
														  final Object value,
														  final boolean sel,
														  final boolean expanded,
														  final boolean leaf,
														  final int row,
														  final boolean hasFocus) {
				final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				final File f = ((FileNode)value).getFile();
				label.setText(FILE_SYSTEM_VIEW.getSystemDisplayName(f));
				label.setIcon(FILE_SYSTEM_VIEW.getSystemIcon(f));
				return label;
			}
			
		});
		tree.addKeyListener(new KeyAdapter() {

			@Override
			public void keyPressed(final KeyEvent event) {
			final TreePath selectionPath = tree.getSelectionPath();
			if ((event.isMetaDown() || event.isControlDown())
					&& event.getKeyCode() == KeyEvent.VK_R	) {
				tree.setModel(new FileTreeModel(userHomeDir));
				tree.expandPath(selectionPath);
				tree.setSelectionPath(selectionPath);
				tree.invalidate();
			}
			}
		});
		
		final JFrame frame = new JFrame("FileBrowser"); 
		frame.getContentPane().add(new JScrollPane(tree)); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(300, 500);
		frame.setVisible(true);
	}
}