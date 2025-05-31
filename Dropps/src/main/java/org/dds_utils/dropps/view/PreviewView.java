package org.dds_utils.dropps.view;

import com.formdev.flatlaf.FlatLightLaf;
import org.dds_utils.dropps.models.ClipImage;
import org.dds_utils.dropps.view.canvas.BICanvas;
import org.ddsutil.UnsupportedCompressionFormatException;
import org.httprpc.sierra.ActivityIndicator;
import org.httprpc.sierra.UILoader;
import org.model.DDSFile;
import org.model.TextureImage;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.Transferable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ResourceBundle;

public class PreviewView extends JFrame implements Runnable {

    private TextureImage image;
    BICanvas bufferedImageCanvas;
    private JButton button = null;
    private JLabel fileLabel = null;
    private ActivityIndicator activityIndicator = null;
    private static final ResourceBundle resourceBundle = ResourceBundle.getBundle(PreviewView.class.getName());

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

        button.addActionListener(event -> copy());

        setLocationByPlatform(true);
        setResizable(true);
        setVisible(true);

        bufferedImageCanvas.setSourceImage(image.getBufferedImage());
        fileLabel.setText(String.format(resourceBundle.getString("fileLabel.text"), (String) image.getFile().getName(), (String)image.getPixelformatVerbose()));
    }

    private void copy() {
        button.setEnabled(false);
        activityIndicator.start();
        /* Returns the currently loaded {@link BufferedImage} shown on the canvas.
         * This may be different from the origin Image, as the canvas
         * may be only displaying certain channels.
         */
        BufferedImage bi = bufferedImageCanvas.getCanvas();
        Clipboard myClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        ClipImage ci = new ClipImage(bi);
        myClipboard.setContents(ci, new ClipboardOwner() {
            public void lostOwnership(Clipboard clipboard, Transferable contents) {	}
        });
        activityIndicator.stop();
        button.setEnabled(true);
    }

}
