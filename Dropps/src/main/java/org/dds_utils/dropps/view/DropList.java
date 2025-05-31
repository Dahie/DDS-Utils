package org.dds_utils.dropps.view;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ResourceBundle;

public class DropList extends JList<File> {
    private final ResourceBundle resourceMap;

    public DropList(DefaultListModel dlm, ResourceBundle resourceBundle) {
        super(dlm);
        this.resourceMap = resourceBundle;
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
            g.drawString(resourceMap.getString("DropsPanel.title"), 15, 25);
            g.setFont(font);
            g.drawString(resourceMap.getString("DropsPanel.text"), 15, 45);
        }
    }
}