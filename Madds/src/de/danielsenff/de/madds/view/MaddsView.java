package de.danielsenff.de.madds.view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

import net.bouthier.treemapSwing.TMView;

import de.danielsenff.de.madds.models.Inventorizer;

public class MaddsView extends JFrame {

	public MaddsView(Inventorizer inventorizer, Container view) {
		setPreferredSize(new Dimension(700,500));
		
		JPanel statisticsPanel = new StatisticsPanel(inventorizer);
		
		view.add(statisticsPanel, BorderLayout.SOUTH);
		
		setContentPane(view);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		pack();
		setVisible(true);
	}
	
}
