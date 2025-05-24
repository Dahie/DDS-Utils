package org.dds_utils.dropps.models;

import java.io.*;
import java.awt.image.BufferedImage;
import org.ddsutil.MipMapsUtil;
import javax.imageio.ImageIO;
import org.util.ImageIOUtils;
import org.ddsutil.DDSUtil;
import org.util.FileUtil;

import static org.util.ImageIOUtils.isImageIOSupported;

public class DDSConverter implements IConverter {

    private final File sourceFile;
    private final File targetFile;
    private final ExportOptions options;
    
    public DDSConverter(File sourceFile, File targetFile, ExportOptions options) {
        this.sourceFile = sourceFile;
        this.targetFile = targetFile;
        this.options = options;
    }

    public ConversionResult perform() throws IOException {
		BufferedImage imageToConvert = BufferedImageReader.read(this.sourceFile);

		if(imageToConvert == null) {
			return new ConversionResultBuilder().error("Error: Image not loadable").buildConversionResult(); // todo split to different messages
		}

		if (!MipMapsUtil.isPowerOfTwo(imageToConvert.getWidth())
				&& !MipMapsUtil.isPowerOfTwo(imageToConvert.getHeight())
				&& options.hasGeneratedMipMaps()) {
			return new ConversionResultBuilder().error("Error: Mipmaps generation failed").buildConversionResult(); // todo split to different messages
		}

		DDSUtil.write(this.targetFile, imageToConvert, options.getNewPixelformat(), options.hasGeneratedMipMaps());

		return new ConversionResultBuilder().image(imageToConvert).targetFile(targetFile).buildConversionResult();
    }

}