package org.io;

import org.model.TextureImage;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageReader {
    public TextureImage read() throws IOException;
}
