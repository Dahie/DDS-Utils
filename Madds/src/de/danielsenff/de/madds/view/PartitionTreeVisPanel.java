package de.danielsenff.de.madds.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import de.danielsenff.de.madds.models.SizableNode;
import de.danielsenff.de.madds.models.Tree;

public class PartitionTreeVisPanel extends JPanel {

	Tree<SizableNode> tree;
	
	public PartitionTreeVisPanel(Tree<SizableNode> tree2) {
		this.tree = tree2;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension maxBounds = this.getSize();
		
		paintSizableNode(tree.getRoot(),0);
		
		
	}

	private void paintSizableNode(SizableNode root, int i) {
		// TODO Auto-generated method stub
		
	}

}
