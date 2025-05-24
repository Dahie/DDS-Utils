package org.dds_utils.dropps.ui;

import org.dds_utils.dropps.models.ResourceMap;

import javax.swing.*;
import java.awt.*;

public class DropList extends JList {
    private final ResourceMap resourceMap;

    public DropList(DefaultListModel dlm, ResourceMap resourceMap) {
        super(dlm);
        this.resourceMap = resourceMap;
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