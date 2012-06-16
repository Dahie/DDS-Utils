package de.danielsenff.de.madds.view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.danielsenff.de.madds.models.Inventorizer;

public class MaddsView extends JFrame {

	public MaddsView(Inventorizer inventorizer) {
		setPreferredSize(new Dimension(500,500));
		
		setLayout(new BorderLayout());
		
//		JPanel treePanel = new PartitionTreeVisPanel(tree);
		
		
		
//		add(partitionPanel, BorderLayout.CENTER);
		
		JPanel statisticsPanel = new StatisticsPanel(inventorizer);
		
		add(statisticsPanel, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
}
