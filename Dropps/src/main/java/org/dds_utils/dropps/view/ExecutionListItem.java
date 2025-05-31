package org.dds_utils.dropps.view;

import org.dds_utils.dropps.models.ConversionExecution;
import org.httprpc.sierra.ActivityIndicator;
import org.httprpc.sierra.ImagePane;
import org.httprpc.sierra.MenuButton;
import org.httprpc.sierra.UILoader;
import org.model.ExportConfiguration;
import org.model.TextureImage;
import org.w3c.dom.Text;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ResourceBundle;

public class ExecutionListItem implements ListCellRenderer<ConversionExecution> {
    JComponent component;

    ImagePane imagePane = null;
    JLabel nameLabel = null;
    JLabel descriptionLabel = null;
    JProgressBar progressBar = null;
    MenuButton menuButton = null;
    private ActivityIndicator activityIndicator = null;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(ExecutionListItem.class.getName());

    public ExecutionListItem() {
        component = UILoader.load(this, "ExecutionListItem.xml");
        progressBar.setIndeterminate(true);
        activityIndicator.start();
        menuButton.setText(resourceBundle.getString("showMenu"));

        var popupMenu = new JPopupMenu();

        popupMenu.add(new JMenuItem(resourceBundle.getString("item1")));
        popupMenu.add(new JMenuItem(resourceBundle.getString("item2")));
        popupMenu.add(new JMenuItem(resourceBundle.getString("item3")));

        menuButton.setPopupMenu(popupMenu);
    }

    public void updateProgress(int max, int current) {
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(max);
        progressBar.setValue(current);
        descriptionLabel.setText(
                String.format(resourceBundle.getString("descriptionLabel.in_progress"), (current/max)));
    }

    public void finishProgress() {
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(100);
        progressBar.setMaximum(100);
        descriptionLabel.setText(
                String.format(resourceBundle.getString("descriptionLabel.completed")));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ConversionExecution> list, ConversionExecution value, int index, boolean selected, boolean cellHasFocus) {
        nameLabel.setText(value.getTitle());
        if(value.textureImage != null && value.textureImage.getBufferedImage() != null) {
            imagePane.setImage(value.textureImage.getBufferedImage());
            imagePane.setVisible(true);
            activityIndicator.stop();
            activityIndicator.setVisible(false);
        }

        //descriptionLabel.setText("value.description");

        Color background;
        Color foreground;
        if (selected) {
            background = list.getSelectionBackground();
            foreground = list.getSelectionForeground();
        } else {
            background = list.getBackground();
            foreground = list.getForeground();
        }

        component.setBackground(background);

        nameLabel.setForeground(foreground);
        descriptionLabel.setForeground(foreground);

        return component;
    }
}
