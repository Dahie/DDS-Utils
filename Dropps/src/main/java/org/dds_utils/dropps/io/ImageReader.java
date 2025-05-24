package org.dds_utils.dropps.io;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageReader {
    public BufferedImage read() throws IOException;
}
