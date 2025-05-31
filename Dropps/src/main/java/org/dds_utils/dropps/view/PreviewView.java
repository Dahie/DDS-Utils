package org.dds_utils.dropps.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.dds_utils.dropps.view.canvas.BICanvas;
import org.ddsutil.UnsupportedCompressionFormatException;
import org.httprpc.sierra.TaskExecutor;
import org.model.DDSFile;
import org.model.TextureImage;

import javax.swing.*;
import java.io.File;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;

public class PreviewView extends JFrame implements Runnable {

    private TextureImage image;
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

    public void run() {
        final ResourceMap resourceMap = new ResourceMap("PreviewView.properties");

        setSize(680, 350);

        setLocationByPlatform(true);
        setResizable(false);
        pack();
        setVisible(true);

        BICanvas canvas = new BICanvas(image.getBufferedImage());
    }
}
