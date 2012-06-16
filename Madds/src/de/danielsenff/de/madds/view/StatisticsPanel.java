package de.danielsenff.de.madds.view;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableModel;

import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.SummaryTableModel;

public class StatisticsPanel extends JPanel {

	JTable summaryTable;
	
	public StatisticsPanel(Inventorizer inventory) {
		
		
		TableModel tModel = new SummaryTableModel(inventory.getTextureFiles());
		
		this.summaryTable = new JTable();
		summaryTable.setModel(tModel);
		System.out.println("he2");
		add(summaryTable);
	}
	
}
