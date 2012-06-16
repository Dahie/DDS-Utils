package de.danielsenff.de.madds.models;

import java.util.Collection;
import java.util.HashMap;

import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import de.danielsenff.de.madds.util.ByteConverter;
import de.danielsenff.de.madds.util.Logger;

public class SummaryTableModel extends AbstractTableModel {

	Collection<TextureFile> files;
	HashMap<Integer,CalculatedValue> table;
	
	public SummaryTableModel(Collection<TextureFile> collection) {
		this.files = collection;
		this.table = new HashMap<>();
		
		for (TextureFile textureFile : collection) {
			
			if(!table.containsKey(textureFile.getMaterial().ordinal())) {
				CalculatedValue cv = new CalculatedValue();
				cv.material = textureFile.getMaterial();
				table.put(textureFile.getMaterial().ordinal(), cv);
			} else {
				CalculatedValue cv = table.get(textureFile.getMaterial().ordinal());
				cv.fileCount++;
				cv.size += textureFile.getSize();
			}
		}
	}
	
	@Override
	public int getRowCount() {
		return this.table.keySet().size();
	}

	@Override
	public int getColumnCount() {
		return 3;
	}

	@Override
	public String getColumnName(int columnIndex) {
		switch(columnIndex) {
		case 0: return "Material";
		case 1: return "Number of files";
		case 2: return "Sum in MibiByte";
		}
		return "";
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		CalculatedValue calculatedValue = table.get(rowIndex);
		switch(columnIndex) {
		case 0:	return calculatedValue.material.toString();
		case 1:	return calculatedValue.fileCount;
		case 2:	return ByteConverter.bit2MibiByte(calculatedValue.size);
		}
		return "";
	}

	
	class CalculatedValue {
		Material material;
		long size = 0;
		int fileCount = 0;
	}

}
