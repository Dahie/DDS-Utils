package org.io;

import org.jogl.TextureImageFormatLoaderTGA;
import org.model.GenericImageFile;
import org.model.TextureImage;
import org.util.*;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import static org.util.ImageIOUtils.isImageIOSupported;

public class ImageIOReader implements ImageReader {

    public File sourceFile;

    public ImageIOReader(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    public TextureImage read() throws IOException {
        final InputStream fis = new FileInputStream(sourceFile);
        BufferedImage image = null;

        if(isImageIOSupported(sourceFile)) {
            image = ImageIO.read(fis);
        // TODO TEX support
            // } else if (FileUtil.getFileSuffix(sourceFile).contains("tex")) {
            // image = DDSUtil.decompressTexture(sourceFile);
        } else if (FileUtil.getFileSuffix(sourceFile).contains("tga")) {
            final BufferedInputStream in = new BufferedInputStream(fis);
            final TextureImageFormatLoaderTGA loader = new TextureImageFormatLoaderTGA();
            image = loader.loadTextureImage(in , true, false);
        }
        fis.close();

        return new GenericImageFile(sourceFile, image);
    }
}
