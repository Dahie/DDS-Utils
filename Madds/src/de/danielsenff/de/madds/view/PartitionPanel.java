package de.danielsenff.de.madds.view;

import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import de.danielsenff.de.madds.models.Sizable;
import de.danielsenff.de.madds.models.Tree;

public class PartitionPanel extends JPanel {

	Tree<Sizable> tree;
	
	public PartitionPanel(Tree<Sizable> tree2) {
		this.tree = tree2;
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		Dimension maxBounds = this.getSize();
		
		paintSizableNode(tree.getRoot(),0);
		
		
	}

	private void paintSizableNode(Sizable root, int i) {
		// TODO Auto-generated method stub
		
	}

}
