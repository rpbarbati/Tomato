package com.mrsoftware.udb.util.common;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTable;

public class ValueListEditorPanel extends JPanel {

    private JTable table;

    /**
     * Create the panel.
     */
    public ValueListEditorPanel() {
        setLayout(null);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(86, 11, 383, 215);
        add(scrollPane);

        table = new JTable();
        table.setFillsViewportHeight(true);
        scrollPane.setViewportView(table);

        JButton btnNewButton_1 = new JButton("Add");
        btnNewButton_1.setBounds(479, 11, 75, 23);
        add(btnNewButton_1);

        JButton btnDelete = new JButton("Delete");
        btnDelete.setBounds(479, 48, 75, 23);
        add(btnDelete);

    }
}
