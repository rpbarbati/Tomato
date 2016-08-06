package com.mrsoftware.udb.util.common.datamodels;

//package common.datamodels;
//
//import com.mrsoftware.udb.Entity;
//import javax.swing.table.DefaultTableModel;
//
//// I think this is the schemaObjects table stuff
//
//public class FormMetaDataObjectsModel extends DefaultTableModel {
//	
//	Entity ec = Entity.createEntityOrView("edp_dd.formschemaobjects");
//	
//	public FormMetaDataObjectsModel(String objectType)
//	{
//		ec.setFilter("objectType = '" + objectType + "'");
//		
//		ec.load();
//	}
//	
//	public Class getColumnClass(int columnIndex) {
//			return String.class;
//	}
//	
//	public boolean isCellEditable(int row, int column) {
//		return true;
//	}
//	
//	@Override
//	public int getColumnCount() {
//		return 2;
//	}
//	
//	public int getRowCount() {
//		return (ec == null) ? 0 : ec.getChildren().size();
//	}
//	
//	@Override
//	public Object getValueAt(int row, int column) {
//		return ec.getChild(row).getColumnValue((column == 0) ? "objectName" : "objectValue");
//	}
//	
//}
