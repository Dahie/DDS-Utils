package org.dds_utils.dropps.models;

import org.ddsutil.DDSUtil;
import org.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static org.util.ImageIOUtils.isImageIOSupported;

public class BufferedImageReader {
    public static BufferedImage read(final File sourceFile) throws IOException {
        final InputStream fis = new FileInputStream(sourceFile);
        BufferedImage image = null;

        if(isImageIOSupported(sourceFile)) {
            image = ImageIO.read(fis);
            System.out.println(image);
        } else if (FileUtil.getFileSuffix(sourceFile).contains("tex")) {
            image = DDSUtil.decompressTexture(sourceFile);
        } else if (FileUtil.getFileSuffix(sourceFile).contains("tga")) {
            final TextureImageFormatLoaderTGA loader = new TextureImageFormatLoaderTGA();

            final BufferedInputStream in = new BufferedInputStream(fis);
            image = loader.loadTextureImage(in , true, false);
        }
        fis.close();
        return image;
    }
}
