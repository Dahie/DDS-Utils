package de.danielsenff.radds.models;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Vector;

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
	private final FileNode _rootNode; 
	private HashSet<FileFilter> fileFilters;
	
	public FileTreeModel(final File dir) { 
		_rootNode = new FileNode(dir);
		fileFilters = new HashSet<FileFilter>();
	} 

	/**
	 * Constructor takes the root directory of this file tree.
	 * The {@link FileFilter} defines a filter for files to include in the tree. 
	 * @param dir
	 * @param fileFilter
	 */
	public FileTreeModel(final File dir, final FileFilter fileFilter) {
		this(dir);
		fileFilters.add(fileFilter);
	}
	
	/**
	 * Add a {@link FileFilter} for this file tree.
	 * @param fileFilter
	 */
	public void addFileFilter(final FileFilter fileFilter) {
		this.fileFilters.add(fileFilter);
	}
	
	public Object getRoot() {
		return _rootNode;
	} 

	private static File[] getFiles(final File file) {
		return FILE_SYSTEM_VIEW.getFiles(file, true);
	} 
	
	private Vector<File> getSortedFiles(final File directory) {
		File[] filesArray = getFiles(directory);
		Vector<File> files = new Vector<File>();
		for (int i = 0; i < filesArray.length; i++) {
			File file = filesArray[i];
			if ( filterFile(file))
				files.add(file);
		}
		Collections.sort(files);
		return files;
	}

	private boolean filterFile(File file) {
		for (FileFilter fileFilter : this.fileFilters) {
			if (fileFilter.accept(file))
				return true;
		}
		return false;
	}

	public int getChildCount(final Object parent) {
		final File file = ((FileNode) parent).getFile(); 
		return file.isDirectory() ? getSortedFiles(file).size() : 0;
	}


	public boolean isLeaf(final Object node) {
		final File file = ((FileNode) node).getFile();
		return (file.isDirectory() == false || getSortedFiles(file).size() == 0 );
	}

	public Object getChild(final Object parent, final int index) {
		final File file = ((FileNode) parent).getFile();
		return file.isDirectory() ? new FileNode(getSortedFiles(file).get(index)) : null;
	} 

	public int getIndexOfChild(final Object parent, final Object child) {
		final File dir = ((FileNode) parent).getFile(); 
		final File file = ((FileNode) child).getFile(); 
		return dir.isDirectory() ? Arrays.asList(getSortedFiles(dir)).indexOf(file) : -1;
	}

	public void addTreeModelListener(final TreeModelListener l) {}

	public void removeTreeModelListener(final TreeModelListener l) {}

	public void valueForPathChanged(final TreePath path, final Object newValue) {}
	
	/**
	 * Test-Frame for FileTreeModel
	 * @param args
	 */
	public static void main(final String[] args) {
		final File userHomeDir = new File(System.getProperty("user.home"));
		final JTree tree = new JTree(new FileTreeModel(userHomeDir));
		tree.setCellRenderer(new DefaultTreeCellRenderer() 
		{
			@Override
			public Component getTreeCellRendererComponent(final JTree tree, final Object value, 
					final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) 
			{
				final JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				final File f = ((FileNode)value).getFile();
				label.setText(FILE_SYSTEM_VIEW.getSystemDisplayName(f));
				label.setIcon(FILE_SYSTEM_VIEW.getSystemIcon(f));
				return label;
			}
			
		});
		tree.addKeyListener(new KeyListener() {

			public void keyPressed(final KeyEvent event) {
				final TreePath selectionPath = tree.getSelectionPath();
				if (	(event.isMetaDown() || event.isControlDown())
						&& event.getKeyCode() == KeyEvent.VK_R	) {
					tree.setModel(new FileTreeModel(userHomeDir));
					tree.expandPath(selectionPath);
					tree.setSelectionPath(selectionPath);
					tree.invalidate();
				}
				
			}

			public void keyReleased(final KeyEvent e) {}
			public void keyTyped(final KeyEvent e) {}
			
		}
		);
		
		final JFrame frame = new JFrame("FileBrowser"); 
		frame.getContentPane().add(new JScrollPane(tree)); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(300, 500);
		frame.setVisible(true);
	}
}