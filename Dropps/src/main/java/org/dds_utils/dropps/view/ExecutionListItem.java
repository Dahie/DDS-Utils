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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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

    //ImageIO.read(CellRendererTest.class.getResource(String.format("flags/%s", imageName)));

    public ExecutionListItem() {
        component = UILoader.load(this, "ExecutionListItem.xml");
        progressBar.setIndeterminate(true);
//        activityIndicator.start();
//        menuButton.setText(resourceBundle.getString("showMenu"));
//        menuButton.setFocusable(true);
//
//        var popupMenu = new JPopupMenu();
//
//        popupMenu.add(new JMenuItem(resourceBundle.getString("item1")));
//        popupMenu.add(new JMenuItem(resourceBundle.getString("item2")));
//        popupMenu.add(new JMenuItem(resourceBundle.getString("item3")));
//
//        menuButton.setPopupMenu(popupMenu);
//        menuButton.setEnabled(true);
//        component.addMouseListener(new MouseAdapter() {
//            @Override
//            public void mouseClicked(MouseEvent e) {
//                System.out.println("click");
//            }
//        });
    }

    public void updateProgress(int max, int current) {
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(max);
        progressBar.setValue(current);
        descriptionLabel.setText(
                String.format(resourceBundle.getString("descriptionLabel.in_progress"), current));
    }

    public void finishProgress() {
        progressBar.setIndeterminate(false);
        progressBar.setMaximum(100);
        progressBar.setMaximum(100);
        descriptionLabel.setText(
                String.format(resourceBundle.getString("descriptionLabel.completed")));
    }

    @Override
    public Component getListCellRendererComponent(JList<? extends ConversionExecution> list, ConversionExecution conversionExecution, int index, boolean selected, boolean cellHasFocus) {
        nameLabel.setText(conversionExecution.getTitle());
        if(conversionExecution.textureImage != null && conversionExecution.textureImage.getBufferedImage() != null) {
            imagePane.setImage(conversionExecution.textureImage.getBufferedImage());
            imagePane.setVisible(true);
        }

        if(conversionExecution.status == "in_progress") {
            updateProgress(conversionExecution.maxProgress, conversionExecution.currentProgress);
        } else {
            finishProgress();
        }

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
