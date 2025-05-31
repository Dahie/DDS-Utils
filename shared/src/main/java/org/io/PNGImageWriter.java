package org.io;

import org.model.ExportConfiguration;
import org.model.TextureImage;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class PNGImageWriter extends AbstractImageWriter {

    private final ExportConfiguration options;

    public PNGImageWriter(final File targetFile, ExportConfiguration options) {
        super(targetFile);
        this.options = options;
    }

    @Override
    public void write(TextureImage textureImage) throws IOException {
        // TODO support enforce white alpha configuration
        ImageIO.write(textureImage.getBufferedImage(), "png", targetFile);
    }
}
