package org.io;

import java.io.File;

public abstract class AbstractImageWriter implements ImageWriter {
    protected final File targetFile;

    public AbstractImageWriter(final File targetFile) {
        this.targetFile = targetFile;
    }

    @Override
    public File getTargetFile() {
        return this.targetFile;
    }
}
