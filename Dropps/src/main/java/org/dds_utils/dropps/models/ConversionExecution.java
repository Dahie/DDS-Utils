package org.dds_utils.dropps.models;

import java.io.File;

public class ConversionExecution {
    private File sourceFile;

    public ConversionExecution(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public File getSourceFile() {
        return this.sourceFile;
    }
}
