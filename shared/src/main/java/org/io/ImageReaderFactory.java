package org.io;

import org.util.FileUtil;
import org.util.ImageIOUtils;

import java.io.File;
import java.io.IOException;

public class ImageReaderFactory {
    public static ImageReader build(final File sourceFile) throws IOException {
        if(isFiletypeSupported(sourceFile)) {
            return new ImageIOReader(sourceFile);
        } else if (FileUtil.getFileSuffix(sourceFile).contains("dds")) {
            return new DDSImageReader(sourceFile);
//        } else if (FileUtil.getFileSuffix(sourceFile).contains("tex")) {
//            return new TEXImageReader(sourceFile);
        }
        return null;
    }


    private static boolean isFiletypeSupported(final File file) {
        return (ImageIOUtils.isImageIOSupported(file)
                || FileUtil.getFileSuffix(file).contains("tex")
                || FileUtil.getFileSuffix(file).contains("tga"));
    }

}
