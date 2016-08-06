package com.mrsoftware.udb.util.common;

import com.mrsoftware.udb.Entity;
import javax.swing.JFrame;

public class DynamicEditor {

    static public void main(String[] args) {
        JFrame frame = new JFrame();

        frame.add(new MetaDataEditorPanel());

        frame.setSize(907, 568);
        
        // Disable Caching while using the meta editor
        Entity.disableCache = true;

        frame.setVisible(true);
        frame.show();
    }
}
