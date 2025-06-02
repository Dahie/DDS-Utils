package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.io.File;

public class ConversionExecution {
    public File sourceFile;
    public TextureImage textureImage;
    public String status = "in_progress";
    public int maxProgress = 100;
    public int currentProgress = 100;

    public ConversionExecution(File file) {
        this.sourceFile = file;
    }

    public String getTitle() {
        return this.sourceFile.getName();
    }

}
