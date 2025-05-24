package org.dds_utils.dropps.models;

import java.awt.image.BufferedImage;
import java.io.*;


public class ConversionResult {
    public String error;
    public File targetFile;
    public BufferedImage image;

    public ConversionResult(final String error, BufferedImage image, File targetFile) {
        this.error = error;
        this.targetFile = targetFile;
        this.image = image;
    }
}

