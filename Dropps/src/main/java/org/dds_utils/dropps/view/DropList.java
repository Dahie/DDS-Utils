package org.dds_utils.dropps.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

public class DropList extends JList<File> {
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(DropList.class.getName());

    public DropList() {
        super();
    }

    public DropList(DefaultListModel dlm) {
        super(dlm);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(this.getModel().getSize() == 0) {
            final int fontSize = 24;
            final Font font = g.getFont();
            final Font newFont = new Font(font.getFamily(), Font.BOLD, fontSize);
            g.setFont(newFont);
            g.setColor(new Color(128, 128, 128));
            // and if even now to small, then cut
            g.drawString(resourceBundle.getString("title"), 15, 25);
            g.setFont(font);
            g.drawString(resourceBundle.getString("text"), 15, 45);
        }
    }
}