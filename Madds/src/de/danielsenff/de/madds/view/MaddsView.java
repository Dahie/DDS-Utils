package de.danielsenff.de.madds.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import de.danielsenff.de.madds.models.Inventorizer;

public class MaddsView extends JFrame {

	public MaddsView(Inventorizer inventorizer, Container view) {
		setPreferredSize(new Dimension(700,500));
		
		//view.getAlgorithm().setCushion(true);
		setLayout(new BorderLayout());
		add(view, BorderLayout.CENTER);
		
		JPanel statisticsPanel = new StatisticsPanel(inventorizer);
		
		add(statisticsPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
}
