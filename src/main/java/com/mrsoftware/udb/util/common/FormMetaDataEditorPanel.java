//package com.mrsoftware.udb.util.common;
//
//import com.mrsoftware.udb.Entity;
//import com.mrsoftware.udb.meta.ColumnData;
//import com.mrsoftware.udb.meta.FormMetaData;
//import java.awt.Font;
//import java.awt.event.ActionEvent;
//import java.awt.event.ActionListener;
//
//import javax.swing.DefaultComboBoxModel;
//import javax.swing.JButton;
//import javax.swing.JComboBox;
//import javax.swing.JLabel;
//import javax.swing.JPanel;
//import javax.swing.JScrollPane;
//import javax.swing.JTable;
//import javax.swing.JTextField;
//import javax.swing.ListSelectionModel;
//import javax.swing.event.ListSelectionEvent;
//import javax.swing.event.ListSelectionListener;
//
//import com.mrsoftware.udb.util.common.datamodels.ColumnDataModel;
//import com.mrsoftware.udb.util.common.datamodels.FieldListModel;
//
//public class FormMetaDataEditorPanel extends JPanel {
//
//    private JTextField name;
////	private JTextField view;
//    private JTable fieldTable;
//    private JTable fieldDataTable;
//    private JTextField newFieldName;
//    private String selectedField = null;
//    private JComboBox descriptorName = null;
//
//    boolean dirty = false;
//    Entity entity = null;
//
//    private FormMetaDataEditorPanel This = null;
//
//    /**
//     * Create the panel.
//     */
//    public FormMetaDataEditorPanel() {
//        setLayout(null);
//
//        This = this;
//
//        name = new JTextField();
//        name.setBounds(24, 81, 182, 20);
//        add(name);
//        name.setColumns(10);
//
//        JLabel lblSchematable = new JLabel("schema.table");
//        lblSchematable.setBounds(24, 66, 84, 14);
//        add(lblSchematable);
//
//        JLabel lblView = new JLabel("View");
//        lblView.setBounds(227, 66, 46, 14);
//        add(lblView);
//
////		view = new JTextField();
////		view.setBounds(227, 81, 149, 20);
////		add(view);
////		view.setColumns(10);
//        JButton btnEdit = new JButton("Edit");
//        btnEdit.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                FieldListModel fsfm = new FieldListModel();
//
//                fieldTable.setModel(fsfm);
//
//                fieldTable.getColumnModel().getColumn(0).setPreferredWidth(5);
//                fieldTable.getColumnModel().getColumn(1).setPreferredWidth(50);
//
//                fsfm.load(getViewName());
//            }
//        });
//        btnEdit.setBounds(386, 80, 89, 23);
//
//        add(btnEdit);
//
//        JLabel lblFields = new JLabel("Fields");
//        lblFields.setBounds(24, 116, 46, 14);
//        add(lblFields);
//
//        JScrollPane scrollPane = new JScrollPane();
//        scrollPane.setBounds(24, 137, 182, 213);
//        add(scrollPane);
//
//        fieldTable = new JTable();
//        fieldTable.setFillsViewportHeight(true);
//        fieldTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        ListSelectionModel rowSM = fieldTable.getSelectionModel();
//
//        rowSM.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                if (e.getValueIsAdjusting()) {
//                    return;
//                }
//
//                ListSelectionModel lsm = (ListSelectionModel) e.getSource();
//                if (lsm.isSelectionEmpty()) {
//                    System.out.println("No rows are selected.");
//                } else {
//                    int selectedRow = lsm.getMinSelectionIndex();
//
//                    FormMetaData dfs = ((FieldListModel) fieldTable.getModel()).getFormMetaData();
//
//                    ColumnDataModel fsfdm = (ColumnDataModel) fieldDataTable.getModel();
//
//                    selectedField = (String) fieldTable.getModel().getValueAt(selectedRow, 1);
//
//                    ColumnData cd = dfs.getColumns().get(selectedField);
//
//                    fsfdm.setFieldData(cd);
//                }
//            }
//        });
//
//        scrollPane.setViewportView(fieldTable);
//
//        JScrollPane scrollPane_1 = new JScrollPane();
//        scrollPane_1.setBounds(227, 137, 300, 213);
//        add(scrollPane_1);
//
//        fieldDataTable = new JTable();
//        fieldDataTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//        fieldDataTable.setFillsViewportHeight(true);
//        fieldDataTable.setModel(new ColumnDataModel(this, fieldDataTable));
//        scrollPane_1.setViewportView(fieldDataTable);
//
//        JButton btnNewButton = new JButton("Add");
//        btnNewButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//                // Add a new FieldData to the current form schema
//                FormMetaData dfs = ((FieldListModel) fieldTable.getModel()).getFormMetaData();
//
//                ColumnData fd = new ColumnData(newFieldName.getText());
//
//                dfs.add(fd);
//
//                fieldTable.invalidate();
//                fieldTable.repaint();
//            }
//        });
//        btnNewButton.setBounds(155, 355, 51, 23);
//        add(btnNewButton);
//
//        newFieldName = new JTextField();
//        newFieldName.setBounds(24, 356, 129, 20);
//        add(newFieldName);
//        newFieldName.setColumns(10);
//
//        JButton button = new JButton("Add");
//        button.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent e) {
//
//                // Add a new value to the field Data
//                ColumnDataModel fsfdm = (ColumnDataModel) fieldDataTable.getModel();
//
//                String nameToAdd = descriptorName.getSelectedItem().toString();
//
//                Object[] o = {nameToAdd};
//
//                fsfdm.addRow(o);
//
//                fieldDataTable.invalidate();
//                fieldDataTable.repaint();
//
//            }
//        });
//        button.setBounds(427, 355, 51, 23);
//        add(button);
//
//        descriptorName = new JComboBox();
//        descriptorName.setModel(new DefaultComboBoxModel(new String[]{"Class", "Default", "Directive", "DisplayWidth", "Expression", "File", "HelpText", "HTML", "JS", "KeyHandler", "Rows", "Type", "Validator", "ValueList"}));
//        descriptorName.setBounds(227, 356, 190, 20);
//        add(descriptorName);
//
//        JLabel lblFieldDescriptors = new JLabel("Field Descriptors");
//        lblFieldDescriptors.setBounds(227, 116, 149, 14);
//        add(lblFieldDescriptors);
//
//        JLabel lblDynamicForms = new JLabel("Dynamic Forms - Form Schema Editor");
//        lblDynamicForms.setFont(new Font("Tahoma", Font.PLAIN, 18));
//        lblDynamicForms.setBounds(24, 11, 366, 32);
//        add(lblDynamicForms);
//
//    }
//
//    public String getTableName() {
//        return name.getText();
//    }
//
//    public String getViewName() {
//        return getTableName();
//    }
//
//    public String getFieldName() {
//        return selectedField;
//    }
//
//    public void setDirty(boolean set) {
//        this.dirty = true;
//    }
//}
