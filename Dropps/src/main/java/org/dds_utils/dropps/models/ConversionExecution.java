package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.io.File;

public class ConversionExecution {
    public File sourceFile;
    public TextureImage textureImage;

    public ConversionExecution(File file) {
        this.sourceFile = file;
    }

    public String getTitle() {
        return this.sourceFile.getName();
    }

}
