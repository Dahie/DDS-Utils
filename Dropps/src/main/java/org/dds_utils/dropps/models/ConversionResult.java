package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.awt.image.BufferedImage;
import java.io.*;


public class ConversionResult {
    public String error;
    public File targetFile;
    public TextureImage textureImage;

    public ConversionResult(final String error, TextureImage textureImage, File targetFile) {
        this.error = error;
        this.targetFile = targetFile;
        this.textureImage = textureImage;
    }
}

