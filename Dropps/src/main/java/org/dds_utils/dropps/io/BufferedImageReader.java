package org.dds_utils.dropps.io;

import org.ddsutil.DDSUtil;
import org.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static org.util.ImageIOUtils.isImageIOSupported;

public class BufferedImageReader implements ImageReader {

    public File sourceFile;

    public BufferedImageReader(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public BufferedImage read() throws IOException {
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
