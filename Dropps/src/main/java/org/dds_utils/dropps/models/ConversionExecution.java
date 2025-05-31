package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.io.File;

public class ConversionExecution {
    private File sourceFile;
    private TextureImage textureImage;

    public ConversionExecution(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getSourceFile() {
        return this.sourceFile;
    }

    public void setTextureImage(TextureImage textureImage) {
        this.textureImage = textureImage;
    }

    public TextureImage getTextureImage() {
        return this.textureImage;
    }
}
