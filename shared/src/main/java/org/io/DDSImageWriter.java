package org.io;

import org.ddsutil.PixelFormats;
import org.ddsutil.TextureFactory;
import org.ddsutil.UnsupportedCompressionFormatException;
import org.jogl.DDSImage;
import org.model.ExportConfiguration;
import org.model.TextureImage;
import org.model.TextureMap;
import org.util.ImageUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DDSImageWriter extends AbstractImageWriter {

    private final ExportConfiguration options;

    public DDSImageWriter(final File targetFile, ExportConfiguration options) {
        super(targetFile);
        this.options = options;
    }

    @Override
    public void write(TextureImage textureImage) throws IOException {
        //DDSUtil.write(this.targetFile, sourceImage, options.getNewPixelformat(), options.hasGeneratedMipMaps());
        System.out.println(textureImage);

        final int pixelFormat = options.getNewPixelformat();
        BufferedImage sourceImage = textureImage.getBufferedImage();

        // convert RGB to RGBA image
        if(!sourceImage.getColorModel().hasAlpha())
            sourceImage = ImageUtils.convert(sourceImage, BufferedImage.TYPE_4BYTE_ABGR);

        TextureMap maps = TextureFactory.createTextureMap(options.hasGeneratedMipMaps(), sourceImage);

        ByteBuffer[] mipmapBuffer;

        System.out.println(pixelFormat);
        System.out.println(PixelFormats.isDXTCompressed(pixelFormat));

        if (PixelFormats.isDXTCompressed(pixelFormat)) {
            try {
                mipmapBuffer = maps.getDXTCompressedBuffer(pixelFormat); // TODO parallelize
            } catch (UnsupportedCompressionFormatException e) {
                throw new RuntimeException(e);
            }
        } else
            mipmapBuffer = maps.getUncompressedBuffer();

        System.out.println(mipmapBuffer.length);

        writeFile(pixelFormat, sourceImage.getWidth(), sourceImage.getHeight(), mipmapBuffer);
    }

    private void writeFile(int pixelFormat, int width, int height, ByteBuffer[] mipmapBuffer) throws IOException {
        DDSImage outputImage = DDSImage.createFromData(pixelFormat, width, height, mipmapBuffer);
        outputImage.write(this.targetFile);
        outputImage.close();
    }

}
