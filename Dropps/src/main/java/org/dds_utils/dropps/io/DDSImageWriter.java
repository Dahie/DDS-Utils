package org.dds_utils.dropps.io;

import org.dds_utils.dropps.models.ConversionResult;
import org.dds_utils.dropps.models.ConversionResultBuilder;
import org.dds_utils.dropps.models.ExportConfiguration;
import org.ddsutil.DDSUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class DDSImageWriter implements ImageWriter {

    private File targetFile;
    private ExportConfiguration options;

    public DDSImageWriter(final File targetFile, ExportConfiguration options) {
        this.targetFile = targetFile;
        this.options = options;
    }

    @Override
    public ConversionResult write(BufferedImage image) throws IOException {
        DDSUtil.write(this.targetFile, image, options.getNewPixelformat(), options.hasGeneratedMipMaps());
        return new ConversionResultBuilder().image(image).targetFile(this.targetFile).buildConversionResult();
    }
}
