package org.dds_utils.dropps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.*;

import org.dds_utils.dropps.models.IConvertListener;

import static org.httprpc.sierra.UIBuilder.*;
import org.httprpc.sierra.ActivityIndicator;
import org.httprpc.sierra.TaskExecutor;
import java.util.concurrent.Executors;
import org.dds_utils.dropps.models.ResourceMap;

import javax.swing.border.EmptyBorder;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


/**
 * View and Mainframe
 * @author Daniel Senff
 *
 */
public class DroppsView extends JFrame implements Runnable {

    private javax.swing.JPanel mainPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;
    private ActivityIndicator activityIndicator;

    private final ConvertController controller;
    private DefaultListModel<File> fileListModel;
    private JList dropPanel;

    private static TaskExecutor taskExecutor = new TaskExecutor(Executors.newCachedThreadPool(runnable -> {
        var thread = new Thread(runnable);

        thread.setDaemon(true);

        return thread;
    }));


    public DroppsView(final ConvertController controller) {
        super("Dropps");
        this.controller = controller;
	}


	private void initComponents(ResourceMap resourceMap) {
        fileListModel = new DefaultListModel<File>();
        fileListModel.addListDataListener(new ListDataListener() {
            @Override
            public void intervalAdded(ListDataEvent e) {
                executeConversions(getFileListModel().elementAt(e.getIndex1()));
            }

            @Override
            public void intervalRemoved(ListDataEvent e) {

            }

            @Override
            public void contentsChanged(ListDataEvent e) {

            }
        });
        dropPanel = new DropList(fileListModel, resourceMap);
        JScrollPane scrollpane = new JScrollPane(dropPanel);
        scrollpane.setPreferredSize(new Dimension(450, 190));

        setupFileDrop();

        activityIndicator = new ActivityIndicator(48);

        setContentPane(column(
                row(4,
                        stack(
                                cell(scrollpane),
                                cell(activityIndicator)
                        )
                ),
                row(
                        glue(),
                        cell(new SettingsPanel(this.controller.getOptions())),
                        glue()
                )
        ).with(contentPane -> contentPane.setBorder(new EmptyBorder(8, 8, 8, 8))).getComponent());
    }

    private FileDrop setupFileDrop() {
        return new FileDrop(null, dropPanel, /*dragBorder,*/ new FileDrop.Listener() {
            @Override
            public void filesDropped(final File[] files) {
                for (File file : files) {
                    getFileListModel().addElement(file);
                }
            }
        });
    }

    public DefaultListModel<File> getFileListModel() {
        return fileListModel;
    }

    @Override
    public void run() {
        final ResourceMap resourceMap = new ResourceMap("DroppsView.properties");

        setDefaultCloseOperation(EXIT_ON_CLOSE);

        setSize(680, 350);

        initComponents(resourceMap);

        setLocationByPlatform(true);
        setResizable(false);
        pack();
        setVisible(true);

        controller.addListener(new IConvertListener() {
            @Override
            public void convertBegin(File originalFile) {
                activityIndicator.start();
            }

            @Override
            public void convertEnd(File originalFile) {
                ((DefaultListModel<Object>)dropPanel.getModel()).removeElement(originalFile);;
            }
        });
    }

    private void executeConversions(final File file) {
        taskExecutor.execute(() -> {
            controller.convertFile(file);

            return 100.0;
        }, (result, exception) -> {
            if (exception == null) {
                //label.setText(String.format("Task %.1f%% complete", result));
            } else {
                //label.setText("Task failed");
            }

            if(getFileListModel().getSize() == 0) {
                activityIndicator.stop();
            }
        });
    }

    private class DropList extends JList {
        private final ResourceMap resourceMap;

        public DropList(DefaultListModel dlm, ResourceMap resourceMap) {
            super(dlm);
            this.resourceMap = resourceMap;
        }

        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);

            if(this.getModel().getSize() == 0) {
                final int fontsize = 24;
                final Font font = g.getFont();
                final Font newFont = new Font(font.getFamily(), Font.BOLD, fontsize);
                g.setFont(newFont);
                g.setColor(new Color(128, 128, 128));
                // and if even now to small, then cut
                g.drawString(resourceMap.getString("DropsPanel.title"), 15, 25);
                g.setFont(font);
                g.drawString(resourceMap.getString("DropsPanel.text"), 15, 45);
            }
        }
    }
}


