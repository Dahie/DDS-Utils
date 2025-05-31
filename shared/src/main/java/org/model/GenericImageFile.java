package org.model;

import org.ddsutil.UnsupportedCompressionFormatException;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;

public class GenericImageFile extends AbstractTextureImage{

    private BufferedImage image;

    public GenericImageFile(File file, BufferedImage image) {
        super(file);
        this.image = image;
    }

    @Override
    public BufferedImage getTopMipMap() {
        return this.image;
    }


    @Override
    public TextureType getTextureType() {
        return null;
    }

    @Override
    public BufferedImage getMipMap(int index) {
        return image;
    }

    @Override
    public void loadImageData() throws UnsupportedCompressionFormatException {

    }

    @Override
    public BufferedImage[] getAllMipMapsBI() {
        return new BufferedImage[0];
    }

    @Override
    public List<BufferedImage> generateAllMipMaps() {
        return List.of();
    }
}
