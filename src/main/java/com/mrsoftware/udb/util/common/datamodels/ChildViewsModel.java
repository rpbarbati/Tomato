package com.mrsoftware.udb.util.common.datamodels;

//package common.datamodels;
//
//import javax.swing.table.DefaultTableModel;
//
//import com.wellsfargo.docsys.edp.AutoEntity.View;
//import com.wellsfargo.docsys.edp.AutoEntity.shared.ChildView;
//
//public class ChildViewsModel extends DefaultTableModel {
//	
//	Object[] columnIdentifiers = new Object[] {
//			"View Name", "Collection"
//	};
//
//	Class[] columnTypes = new Class[] {
//			String.class, Boolean.class
//	};
//	
//
//	public ChildViewsModel()
//	{
//		setColumnCount(2);
//		setColumnIdentifiers(columnIdentifiers);
//	}
//	
//	public ChildViewsModel(View v)
//	{
//		this();
//		
//		int row = 0;
//		
//		for (ChildView childView : v.getChildViews().getChildViews())
//		{
//			Object[] values = new Object[] {childView.getViewName(), childView.isAsCollection()};
//			
//			insertRow(row++, values);
//		}
//	}
//	
//	
//	public Class getColumnClass(int columnIndex) {
//			return columnTypes[columnIndex];
//	}
//	
//	boolean[] columnEditables = new boolean[] {
//			true, true
//	};
//	
//	public boolean isCellEditable(int row, int column) {
//		return columnEditables[column];
//	}
//	
//}
