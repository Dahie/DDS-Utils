package org.io;

import org.model.ExportConfiguration;
import org.util.FileUtil;

import java.io.File;
import java.io.IOException;

public class ImageWriterFactory {
    public static ImageWriter build(final File sourceFile, ExportConfiguration options) throws IOException {
        final String destinationPath = sourceFile.getAbsolutePath().substring(0, sourceFile.getAbsolutePath().lastIndexOf('.'));

        if (FileUtil.getFileSuffix(sourceFile).contains("dds")) {
            File targetFile = new File(destinationPath + "." + "png");
            return new PNGImageWriter(targetFile, options);
        }

        File targetFile = new File(destinationPath + "." + "dds");
        return new DDSImageWriter(targetFile, options);
    }
}
