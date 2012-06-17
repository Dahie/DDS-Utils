package de.danielsenff.de.madds.view;

import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.SummaryTableModel;

public class StatisticsPanel extends JPanel {

	JTable summaryTable;
	
	public StatisticsPanel(Inventorizer inventory) {
		LegendPanel legend = new LegendPanel();
		add(legend);
		
		TableModel tModel = new SummaryTableModel(inventory.getTextureFiles().values());
		this.summaryTable = new JTable();
		summaryTable.setModel(tModel);
		summaryTable.setShowHorizontalLines(true);
		JScrollPane scrollPane = new JScrollPane(summaryTable);
		scrollPane.setPreferredSize(new Dimension(300, 120));
		add(scrollPane);
		
	}
	
}
