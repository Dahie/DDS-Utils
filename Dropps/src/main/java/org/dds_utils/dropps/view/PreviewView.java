package org.dds_utils.dropps.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.dds_utils.dropps.view.canvas.BICanvas;
import org.ddsutil.UnsupportedCompressionFormatException;
import org.httprpc.sierra.ActivityIndicator;
import org.httprpc.sierra.TaskExecutor;
import org.httprpc.sierra.UILoader;
import org.model.DDSFile;
import org.model.TextureImage;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class PreviewView extends JFrame implements Runnable {

    private TextureImage image;
    BICanvas bufferedImageCanvas;
    private JButton button = null;
    private JLabel label = null;
    private ActivityIndicator activityIndicator = null;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(PreviewView.class.getName());

    private static final TaskExecutor taskExecutor = new TaskExecutor(Executors.newCachedThreadPool(runnable -> {
        var thread = new Thread(runnable);

        thread.setDaemon(true);

        return thread;
    }));

    public static void main(String[] args) {
        FlatLightLaf.setup();

        TextureImage image = new DDSFile(new File("/Users/danielsenff/PARA/Projects/03 WIP/DDS-Utils/DDS-Utils-repository/UVMap.dds"));
        try {
            image.loadImageData();
        } catch (UnsupportedCompressionFormatException e) {
            throw new RuntimeException(e);
        }
        SwingUtilities.invokeLater(new PreviewView(image));
    }

    public PreviewView(TextureImage image) {
        super(resourceBundle.getString("title"));
        this.image = image;
    }

    public void run(){
        UILoader.bind("buffered-image-canvas", BICanvas.class);
        setContentPane(UILoader.load(this, "PreviewView.xml", resourceBundle));

        setSize(680, 350);

        button.addActionListener(event -> executeTask());

        setLocationByPlatform(true);
        setResizable(true);
        setVisible(true);

        bufferedImageCanvas.setSourceImage(image.getBufferedImage());
    }


    private void executeTask() {
        button.setEnabled(false);

        label.setText(resourceBundle.getString("executingTask"));

        activityIndicator.start();

        taskExecutor.execute(() -> {
            var value = Math.random();

            Thread.sleep((long)(5000 * value));

            if (value < 0.5) {
                throw new Exception();
            }

            return 100.0;
        }, (result, exception) -> {
            button.setEnabled(true);

            if (exception == null) {
                label.setText(String.format(resourceBundle.getString("taskCompleteFormat"), result));
            } else {
                label.setText(resourceBundle.getString("taskFailed:") + exception.getMessage());
            }

            activityIndicator.stop();
        });
    }
}
