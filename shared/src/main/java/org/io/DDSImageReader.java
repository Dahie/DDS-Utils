package org.io;

import org.ddsutil.ByteBufferedImage;
import org.ddsutil.UnsupportedCompressionFormatException;
import org.jogl.DDSImage;
import org.model.DDSFile;
import org.model.TextureImage;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DDSImageReader implements ImageReader {

    private final File sourceFile;
    private DDSFile image;

    public DDSImageReader(File sourceFile) {
        this.sourceFile = sourceFile;
    }

    @Override
    public TextureImage read() throws IOException {
        image = new DDSFile(sourceFile);
        try {
            image.loadImageData();
        } catch (UnsupportedCompressionFormatException e) {
            e.printStackTrace();
        }

//        this.image = DDSImage.read(sourceFile);
//
//        if (image.isCompressed())
//            return new DXTBufferDecompressor(
//                    image.getMipMap(0).getData(),
//                    image.getWidth(),
//                    image.getHeight(),
//                    getSquishCompressionFormat(image.getPixelFormat())).getImage();
//        else
//            return loadBufferedImageFromByteBuffer(
//                    image.getMipMap(0).getData(),
//                    image.getWidth(),
//                    image.getHeight());
        return image;
    }

    private BufferedImage loadBufferedImageFromByteBuffer(
            ByteBuffer data, int width, int height) {

        if(image.getPixelFormat() == DDSImage.D3DFMT_A8R8G8B8) {
            // data in buffer in 4 byte chunks ordered ARGB
            ByteBufferedImage bi = new ByteBufferedImage(width, height, data);

        } else if(image.getPixelFormat() == DDSImage.D3DFMT_A8R8G8B8) {
            // TODO tbd

        }


        // if weird or unknown check RGB-offsets


        return null;
    }

}
