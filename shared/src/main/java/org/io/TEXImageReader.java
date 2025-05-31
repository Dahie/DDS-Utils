package org.io;

import org.ddsutil.UnsupportedCompressionFormatException;
import org.model.TEXFile;
import org.model.TextureImage;

import java.io.File;
import java.io.IOException;

public class TEXImageReader implements ImageReader {

    private final File sourceFile;

    public TEXImageReader(File sourceFile) {
        this.sourceFile = sourceFile;
    }
    
    @Override
    public TextureImage read() throws IOException {
        TEXFile texFile = new TEXFile(sourceFile);
        try {
            texFile.loadImageData();
        } catch (UnsupportedCompressionFormatException e) {
            throw new RuntimeException(e);
        }
        return texFile;
    }
}
