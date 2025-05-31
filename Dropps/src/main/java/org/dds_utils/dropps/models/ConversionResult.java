package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.awt.image.BufferedImage;
import java.io.*;


public class ConversionResult {
    public String error;
    public ConversionExecution conversionExecution;
    public TextureImage textureImage;

    public ConversionResult(final String error, TextureImage textureImage, ConversionExecution conversionExecution) {
        this.error = error;
        this.conversionExecution = conversionExecution;
        this.textureImage = textureImage;
    }
}

