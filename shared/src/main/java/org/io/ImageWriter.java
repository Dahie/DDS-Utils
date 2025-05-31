package org.io;

import org.model.TextureImage;

import java.io.File;
import java.io.IOException;

public interface ImageWriter {

    void write(TextureImage imageToConvert) throws IOException;

    File getTargetFile();
}
