package de.danielsenff.de.madds.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.danielsenff.de.madds.models.SizableNode;
import de.danielsenff.de.madds.models.Tree;

public class TreeFrame extends JFrame {

	public TreeFrame(Tree<SizableNode> tree) {
		setPreferredSize(new Dimension(500,500));
		
		setLayout(new BorderLayout());
		
		JPanel treePanel = new PartitionTreeVisPanel(tree);
		
		add(treePanel, BorderLayout.CENTER);
		
		
		setVisible(true);
	}
	
}
