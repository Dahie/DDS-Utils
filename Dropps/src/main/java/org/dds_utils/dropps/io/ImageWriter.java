package org.dds_utils.dropps.io;

import org.dds_utils.dropps.models.ConversionResult;

import java.awt.image.BufferedImage;
import java.io.IOException;

public interface ImageWriter {

    ConversionResult write(BufferedImage imageToConvert) throws IOException;

}
