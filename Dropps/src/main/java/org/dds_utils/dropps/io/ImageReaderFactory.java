package org.dds_utils.dropps.io;

import org.util.FileUtil;
import org.util.ImageIOUtils;

import java.io.File;
import java.io.IOException;

public class ImageReaderFactory {
    public static ImageReader build(final File sourceFile) throws IOException {
        if(isFiletypeSupported(sourceFile)) {
            return new BufferedImageReader(sourceFile);
        }
        return null;
    }


    private static boolean isFiletypeSupported(final File file) {
        return (ImageIOUtils.isImageIOSupported(file)
                || FileUtil.getFileSuffix(file).contains("tex")
                || FileUtil.getFileSuffix(file).contains("tga"));
    }

}
