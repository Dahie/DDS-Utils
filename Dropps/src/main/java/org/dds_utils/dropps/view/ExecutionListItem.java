package org.dds_utils.dropps.view;

import org.dds_utils.dropps.models.ConversionExecution;
import org.model.TextureImage;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ExecutionListItem extends DefaultListCellRenderer implements ListCellRenderer<Object> {

    @Override
    public Component getListCellRendererComponent(JList list, Object conversionExecution, int index, boolean isSelected, boolean cellHasFocus) {
        //super.getListCellRendererComponent(list, conversionExecution, index, isSelected, cellHasFocus);

        ConversionExecution conversion = (ConversionExecution) conversionExecution;
        setText(conversion.getSourceFile().getName());

        TextureImage image = conversion.getTextureImage();
        if(image != null) {
            //this.getGraphics().drawImage(image.getBufferedImage(), 0, 0, this.getWidth(), this.getHeight(), null);
        }

        return this;
    }
}
