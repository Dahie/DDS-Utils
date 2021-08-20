
package de.danielsenff.radds.view;
import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.util.Enumeration;
import java.util.Arrays;

import javax.swing.JLabel;
import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.event.TreeModelEvent;
import javax.swing.filechooser.FileSystemView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import util.FileUtil;

public class FileSystemTree extends JTree {

	private FileSystemView fileSystemView = FileSystemView.getFileSystemView();

	public FileSystemTree() {
		super();

		updateTreeNodes();
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		setRootVisible(true);
		setCellRenderer(new DefaultTreeCellRenderer() {
			@Override
			public Component getTreeCellRendererComponent(JTree tree, Object value, 
					boolean sel, boolean expanded, boolean leaf, int row, boolean hasFocus) 
			{
			JLabel label = (JLabel) super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
			File f = (File)node.getUserObject();
			label.setText(fileSystemView.getSystemDisplayName(f));
			label.setIcon(fileSystemView.getSystemIcon(f));
			return label;
			}
		});
		addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent event) {
				TreePath selectionPath = getSelectionPath();
				if(event.getKeyCode() == KeyEvent.VK_ENTER) {
					final TreeNode node = (TreeNode) selectionPath.getLastPathComponent();
//					loadImage(node);
				} else if (	(event.isMetaDown() || event.isControlDown())
						&& event.getKeyCode() == KeyEvent.VK_R	) {
					System.out.println("selection path: " + selectionPath);
					updateTreeNodes();
					//preparePath(selectionPath, 1, (DefaultMutableTreeNode) getModel().getRoot());
					expandPath(selectionPath);
					setSelectionPath(selectionPath);
					invalidate();
				}
			}

			public void keyReleased(KeyEvent e) {}
			public void keyTyped(KeyEvent e) {}
		});
		addTreeExpansionListener(new TreeExpansionListener() {
			public void treeCollapsed(TreeExpansionEvent event) 
			{
				TreePath path = event.getPath();
				TreeNode collapsedNode = (TreeNode) path.getLastPathComponent();
				TreePath selectedPath = getSelectionPath();
				TreeNode selectedNode = null;

				if (selectedPath != null) {
					selectedNode = (TreeNode) selectedPath.getLastPathComponent();
					if (isSelectedNodeInCollapsedNode(selectedNode.getParent(), collapsedNode))
						setSelectionPath(path);
				}
			}

			private boolean isSelectedNodeInCollapsedNode(TreeNode selectedNodeParent, TreeNode collapsedNode){
				if(selectedNodeParent == null)
					return false;
				else if(collapsedNode.equals(selectedNodeParent))
					return true;
				else
					return isSelectedNodeInCollapsedNode(selectedNodeParent.getParent(), collapsedNode);
			}
			
			public void treeExpanded(TreeExpansionEvent event)    {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
				prepareChildTreeNodes(node);
			}
		});
	}
	
	/**
	 * 
	 */
	public void updateTreeNodes() {
		File file = fileSystemView.getHomeDirectory();
		DefaultMutableTreeNode node = new FileSystemTreeNode(file);
		prepareTreeNode(node);
		prepareChildTreeNodes(node);
		expandRow(0); // expand root
		((DefaultTreeModel) getModel()).setRoot(node);
	}

	/**
	 * add new leaves to the node
	 * @param node
	 * @return
	 */
	public void prepareTreeNode(DefaultMutableTreeNode node) {
		File f = (File) node.getUserObject();
		File[] files = fileSystemView.getFiles(f, true);
		Arrays.sort(files);
		for(int i = 0; i < files.length; i++) {
			File file = files[i];
			if(file.isDirectory() || supportedFileSuffix(FileUtil.getFileSuffix(file)) )
				node.add(new FileSystemTreeNode(file));
		}
	}

	protected boolean supportedFileSuffix(String fileSuffix) {
		return fileSuffix.contains("dds")
				|| fileSuffix.contains("tex")
				|| fileSuffix.contains("png")
				|| fileSuffix.contains("jpg")
				|| fileSuffix.contains("bmp")
				|| fileSuffix.contains("tga");
	}
	
	/**
	 * prepare each child of this parent node
	 * @param node
	 */
	private void prepareChildTreeNodes(DefaultMutableTreeNode node)	{
		Enumeration<TreeNode> e = node.children();
		while(e.hasMoreElements()) {
			TreeNode child = e.nextElement();
			System.out.println("child: "+ child);	
			prepareTreeNode((DefaultMutableTreeNode) child);
		}
	}
	
	
	class FileSystemTreeNode extends DefaultMutableTreeNode {
		
		public FileSystemTreeNode(File file) {	super(file); }
		
		@Override
		public int hashCode() { 
		    int hc = 17; 
		    int hashMultiplier = 59; 
		    hc = hc * hashMultiplier + this.toString().length(); 
		    hc = hc * hashMultiplier + this.toString().hashCode(); 
		    return hc; 
		}

		@Override
		public boolean equals(Object obj) {
			//System.out.println(obj.toString() + "  " + this.toString());
			//System.out.println(obj.toString().equals(this.toString()));
			return obj.toString().equals(this.toString());
		}
	}
}