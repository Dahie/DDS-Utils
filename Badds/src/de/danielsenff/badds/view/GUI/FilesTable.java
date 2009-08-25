/**
 * 
 */
package de.danielsenff.badds.view.GUI;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.DefaultListSelectionModel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import de.danielsenff.badds.actions.ActionPreview;
import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.model.FilesListModel;
import de.danielsenff.badds.view.View;



/**
 * @author danielsenff
 *
 */
public class FilesTable extends JCPanel {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTable table;
	

	/**
	 * 
	 */
	public FilesTable(Application controller) {
		super(controller);
		this.controller = controller;
		this.setLayout(new BorderLayout());
		
		
		this.table = initTable();
		
		JScrollPane scrollPane = new JScrollPane(this.table);
		this.add(scrollPane, BorderLayout.CENTER);
	}


	/**
	 * @return
	 */
	private JTable initTable() {
	
		final JTable table = new JTable();
		// set models
		table.setModel(controller.getFilesListModel());
		final DefaultListSelectionModel selectionModel = (DefaultListSelectionModel) controller.getFilesListModel().getSelectionModel();
		table.setSelectionModel(selectionModel);
		
		TableColumnModel tcm = table.getColumnModel();
		TableColumn col1 = tcm.getColumn(0);
//		col1.setCellRenderer(new FileCellRenderer());
		
		table.setCellSelectionEnabled(false);
		table.setColumnSelectionAllowed(false);
		table.setRowSelectionAllowed(true);
		
		// start preview on double-click
		table.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) 
			{
		         if (e.getClickCount() == 2) 
		         {
		        	 new ActionPreview(controller).actionPerformed(null);
		          }
			}

			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		// enable and disable buttons on (de)selection
		selectionModel.addListSelectionListener(new ListSelectionListener() 
		{
			public void valueChanged(ListSelectionEvent arg0) 
			{
				enableSelectionButtons(selectionModel);
			}

		});
		
		controller.getFilesListModel().addTableModelListener(new TableModelListener() 
		{
			public void tableChanged(TableModelEvent arg0) 
			{
				enableListOpButton(controller.getFilesListModel());
				invalidate();
			}
			
		});
		
		return table;
	}
	

	private void enableListOpButton(FilesListModel filesListModel) {
		View view = controller.getView();
		if (!filesListModel.isEmpty()) {
			
			view.getActionSaveAll().setEnabled(true);
			view.getActionClearFilelist().setEnabled(true);
//			view.getActionSelectAll().setEnabled(true);
		} else {
			view.getActionSaveAll().setEnabled(false);
			view.getActionClearFilelist().setEnabled(false);
//			view.getActionSelectAll().setEnabled(false);
		}
		
	}

	private void enableSelectionButtons(
			final DefaultListSelectionModel lsm) {
		if(lsm.isSelectionEmpty() && !hasSelectedRow()) {
			controller.getView().getActionRemoveFile().setEnabled(false);
			controller.getView().getActionPreview().setEnabled(false);
		} else {
			controller.getView().getActionRemoveFile().setEnabled(true);
			controller.getView().getActionPreview().setEnabled(true);
		}
	}
	
	public int getSelectedRow() {
		return table.getSelectedRow();
	}
	
	

	public JTable getTable() {
		return this.table;
	}
	
	public boolean hasSelectedRow() {

		for (int i = 0; i < table.getRowCount(); i++) {
			if (table.isRowSelected(i)) 
				return true;
		}
		
		return false;
	}


}
