package org.dds_utils.dropps.view;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.dds_utils.dropps.ConvertController;
import org.dds_utils.dropps.models.ConversionExecution;
import org.dds_utils.dropps.models.IProgressListener;
import org.dds_utils.dropps.models.ProgressStatus;
import org.ddsutil.UnsupportedCompressionFormatException;
import org.httprpc.sierra.ActivityIndicator;
import org.httprpc.sierra.TaskExecutor;
import org.httprpc.sierra.UILoader;
import org.model.DDSFile;
import org.model.TextureImage;

import java.util.ResourceBundle;
import java.util.concurrent.Executors;

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
    private JCheckBox chkWhiteAlpha;
    private JCheckBox chkMipMaps;

    private final ConvertController controller;
    private DefaultListModel<ConversionExecution> executionsListModel;
    private JList<ConversionExecution> dropList;

    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(DroppsView.class.getName());

    private final static TaskExecutor taskExecutor = new TaskExecutor(Executors.newCachedThreadPool(runnable -> {
        var thread = new Thread(runnable);

        thread.setDaemon(true);

        return thread;
    }));


    public DroppsView(final ConvertController controller) {
        super(resourceBundle.getString("title"));
        this.controller = controller;
        this.setIconImage(new ImageIcon("src/main/resources/icon.png").getImage());
	}

	private void initComponents() {
        executionsListModel = new DefaultListModel<ConversionExecution>();
        executionsListModel.addListDataListener(new DropListDataListener());
        dropList.setModel(executionsListModel);
        dropList.setCellRenderer(new ExecutionListItem());

        setupFileDrop();


        dropList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList<ConversionExecution> list = (JList<ConversionExecution>)evt.getSource();
                if (evt.getClickCount() == 2) {

                    // Double-click detected
                    int index = list.locationToIndex(evt.getPoint());
                    TextureImage image = list.getModel().getElementAt(index).textureImage;
                    if(image != null) {
                        new PreviewView(image).run();
                    }
                } else if (evt.getClickCount() == 3) {

                    // Triple-click detected
                    int index = list.locationToIndex(evt.getPoint());
                }
            }
        });

        activityIndicator = new ActivityIndicator(48);
    }

    private FileDrop setupFileDrop() {
        return new FileDrop(null, dropList, /*dragBorder,*/ new FileDrop.Listener() {
            @Override
            public void filesDropped(final File[] files) {
                for (final File file : files) {
                    ConversionExecution conversionExecution = new ConversionExecution(file);
                    getExecutionsListModel().addElement(conversionExecution);
                }
            }
        });
    }

    public DefaultListModel<ConversionExecution> getExecutionsListModel() {
        return executionsListModel;
    }

    @Override
    public void run() {
        UILoader.bind("drop-list", DropList.class);
        UILoader.bind("settings-panel", SettingsPanel.class);
        setContentPane(UILoader.load(this, "DroppsView.xml", resourceBundle));

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(680, 350);

        initComponents();

        setLocationByPlatform(true);
        setVisible(true);
    }

    private void executeConversions(final ConversionExecution conversionExecution) {
        taskExecutor.execute(() -> {
            controller.convertFile(conversionExecution);
            controller.addListener(new ConversionProgressListener());

            return 100.0;
        }, (result, exception) -> {
            if (exception == null) {
                //label.setText(String.format("Task %.1f%% complete", result));
            } else {
                exception.printStackTrace();
                //label.setText("Task failed", exception.getMessage());
            }
        });
    }

    private class ConversionProgressListener implements IProgressListener {
        @Override
        public void begin(ProgressStatus status) {
            activityIndicator.start();
        }

        @Override
        public void imageLoaded(ProgressStatus status) {
            dropList.repaint();
        }

        @Override
        public void update(ProgressStatus status) {

        }

        @Override
        public void error(ProgressStatus status) {

        }

        @Override
        public void end(ProgressStatus status) {
            //((DefaultListModel<ConversionExecution>) dropList.getModel()).removeElement(status.conversionExecution);;
            dropList.repaint();
            activityIndicator.stop();
        }
    }

    private class DropListDataListener implements ListDataListener {
        @Override
        public void intervalAdded(ListDataEvent e) {
            ConversionExecution conversionExecution = getExecutionsListModel().elementAt(e.getIndex1());
            executeConversions(conversionExecution);
        }

        @Override
        public void intervalRemoved(ListDataEvent e) { }

        @Override
        public void contentsChanged(ListDataEvent e) { }
    }
}


