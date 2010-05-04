package de.danielsenff.radds.models;

import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeModelListener;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

public class FileTreeModel implements TreeModel
{
	private static final FileSystemView FILE_SYSTEM_VIEW = FileSystemView.getFileSystemView();
	private final FileNode _rootNode; 
	
	public FileTreeModel(File dir) { 
		_rootNode = new FileNode(dir);
	} 

	public Object getRoot() {
		return _rootNode;
	} 

	public int getChildCount(Object parent) {
		File file = ((FileNode) parent).getFile(); 
		return file.isDirectory() ? FILE_SYSTEM_VIEW.getFiles(file, true).length : 0;
	} 

	public boolean isLeaf(Object node) {
		File file = ((FileNode) node).getFile();
		return (file.isDirectory() == false || FILE_SYSTEM_VIEW.getFiles(file, true).length == 0 );
	}

	public Object getChild(Object parent, int index) {
		File file = ((FileNode) parent).getFile();
		return file.isDirectory() ? new FileNode(FILE_SYSTEM_VIEW.getFiles(file, true)[index]) : null;
	} 

	public int getIndexOfChild(Object parent, Object child) {
		File dir = ((FileNode) parent).getFile(); 
		File file = ((FileNode) child).getFile(); 
		return dir.isDirectory() ? Arrays.asList(FILE_SYSTEM_VIEW.getFiles(dir, true)).indexOf(file) : -1;
	}

	public void addTreeModelListener(TreeModelListener l) {
	}

	public void removeTreeModelListener(TreeModelListener l) {
	}

	public void valueForPathChanged(TreePath path, Object newValue) {
	}
	
	public static void main(String[] args) {
		final File userHomeDir = new File(System.getProperty("user.home"));
		final JTree tree = new JTree(new FileTreeModel(userHomeDir));
		tree.setCellRenderer(new DefaultTreeCellRenderer() 
		{
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, 
					boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
			{
				JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
				File f = ((FileNode)value).getFile();
				label.setText(FILE_SYSTEM_VIEW.getSystemDisplayName(f));
				label.setIcon(FILE_SYSTEM_VIEW.getSystemIcon(f));
				return label;
			}
		});
		tree.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				TreePath selectionPath = tree.getSelectionPath();
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					final TreeNode node = (TreeNode) selectionPath.getLastPathComponent();
//					loadImage(node);
				} else if (	(event.isMetaDown() || event.isControlDown())
						&& event.getKeyCode() == KeyEvent.VK_R	) {
					System.out.println("selection path: " + selectionPath);
					tree.setModel(new FileTreeModel(userHomeDir));
					//updateTreeNodes();
					//preparePath(selectionPath, 1, (DefaultMutableTreeNode) getModel().getRoot());
					tree.expandPath(selectionPath);
					tree.setSelectionPath(selectionPath);
					tree.invalidate();
				}
				
			}

			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}

			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
		}
		);
		
		JFrame frame = new JFrame("FileBrowser"); 
		frame.getContentPane().add(new JScrollPane(tree)); 
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		frame.setSize(300, 500);
		frame.show();
	}
}