package org.dds_utils.dropps.io;

import org.dds_utils.dropps.models.ExportConfiguration;

import java.io.File;
import java.io.IOException;

public class ImageWriterFactory {
    public static ImageWriter build(final File targetFile, ExportConfiguration options) throws IOException {
        return new DDSImageWriter(targetFile, options);
    }
}
