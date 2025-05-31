package org.model;

import io.github.memo33.jsquish.Squish.CompressionType;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.List;


import org.ddsutil.UnsupportedCompressionFormatException;
import org.jogl.DDSImage;

import org.compression.ARGBBufferDecompressor;
import org.compression.BufferDecompressor;
import org.compression.DXTBufferDecompressor;

import org.ddsutil.MipMapsUtil;
import org.ddsutil.PixelFormats;


/**
 * @author danielsenff
 */
public class DDSFile extends AbstractTextureImage {

    protected TextureType textureType;
    private DDSImage ddsImage;

    /**
     * Constructs a DDSFile from a {@link File}
     *
     * @param file
     */
    public DDSFile(final File file) {
        super(file);
        try {
            initFromDDSImage(DDSImage.read(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a DDSFile from a {@link File} and a {@link DDSImage}
     *
     * @param file
     * @param ddsImage
     */
    public DDSFile(final File file, final DDSImage ddsImage) {
        super(file);
        initFromDDSImage(ddsImage);
    }

    /**
     * @param file
     * @param bufferedImage
     * @param pixelFormat
     * @param hasMipMaps
     */
    public DDSFile(final File file,
                   final BufferedImage bufferedImage,
                   final int pixelFormat,
                   final boolean hasMipMaps) {
        super(file);
        this.width = bufferedImage.getWidth();
        this.height = bufferedImage.getHeight();
        this.pixelFormat = pixelFormat;
        this.hasMipMaps = hasMipMaps;
        if (hasMipMaps) {
            this.numMipMaps = MipMapsUtil.calculateMaxNumberOfMipMaps(width, height);
        } else {
            this.numMipMaps = 1;
        }
        this.mipMaps = new MipMaps(this.numMipMaps);
        this.mipMaps.setMipMap(0, bufferedImage);
    }

    /**
     * @param ddsimage
     */
    protected void initFromDDSImage(final DDSImage ddsimage) {
        this.ddsImage = ddsimage;
        this.width = ddsimage.getWidth();
        this.height = ddsimage.getHeight();
        this.depth = ddsimage.getDepth();
        this.pixelFormat = ddsimage.getPixelFormat();
        this.textureType = getTextureType(ddsimage);
        this.numMipMaps = ddsimage.getNumMipMaps();
        this.mipMaps = new MipMaps(this.numMipMaps);
        this.hasMipMaps = (ddsimage.getNumMipMaps() > 1); // there is always at least the topmost MipMap
    }

    /**
     * Load the ImageData for the specified MipMap from original {@link DDSImage}.
     *
     * @param mipmap
     */
    public void loadImageData(int mipmap) throws UnsupportedCompressionFormatException {
        if (mipmap <= this.numMipMaps) {
            int width = MipMaps.getMipMapSizeAtIndex(mipmap, ddsImage.getWidth());
            int height = MipMaps.getMipMapSizeAtIndex(mipmap, ddsImage.getHeight());
            ByteBuffer data = ddsImage.getMipMap(mipmap).getData();

            BufferDecompressor bufferDecompressor = getBufferDecompressor(width, height, data);
            this.mipMaps.addMipMap(bufferDecompressor.getImage());
        }
    }

    private BufferDecompressor getBufferDecompressor(int width, int height, ByteBuffer data) throws UnsupportedCompressionFormatException {
        BufferDecompressor bufferDecompressor;
        if (isCompressed()) {
            CompressionType compressionType =
                    PixelFormats.getSquishCompressionFormat(ddsImage.getPixelFormat());
            bufferDecompressor = new DXTBufferDecompressor(
                    data,
                    width,
                    height,
                    compressionType);
        } else {
            bufferDecompressor = new ARGBBufferDecompressor(
                    data,
                    width,
                    height,
                    this.pixelFormat);
        }
        return bufferDecompressor;
    }

    @Override
    public void loadImageData() throws UnsupportedCompressionFormatException {
        for (int i = 0; i < this.numMipMaps; i++) {
            loadImageData(i);
        }
    }

    @Override
    public String toString() {
        return this.file.getAbsolutePath() + PixelFormats.verbosePixelformat(this.pixelFormat);
    }

    @Override
    public boolean equals(Object second) {
        if (second instanceof DDSFile secondFile) {
            return (this.getFile().getAbsoluteFile().equals(secondFile.getFile().getAbsoluteFile()) &&
                    this.hasMipMaps() == secondFile.hasMipMaps() &&
                    this.getPixelFormat() == secondFile.getPixelFormat() &&
                    this.getHeight() == secondFile.getHeight() &&
                    this.getWidth() == secondFile.getWidth());
        }
        return false;
    }

//	public ByteBuffer[] getMipMapData() {
//		ByteBuffer[] buffer = new ByteBuffer[ddsimage.getNumMipMaps()+1];
//		for (int i = 0; i < buffer.length; i++) {
//			buffer[i] = ddsimage.getMipMap(i).getData();
//		}
//		return buffer;
//	}

    /**
     * The DDS-Image can have different texture types.
     * Regular Texture, Volume-Texture and CubeMap
     *
     * @return TextureType Type of Texture
     */
    public TextureType getTextureType() {
        return this.textureType;
    }

    /**
     * The DDS-Image can have different texture types.
     * Regular Texture, Volume-Texture and CubeMap
     * This returns the textureType from a {@link DDSImage}
     *
     * @param ddsimage
     * @return
     */
    public static TextureType getTextureType(final DDSImage ddsimage) {
        if (ddsimage.isCubemap()) {
            return TextureType.CUBEMAP;
        } else if (ddsimage.isVolume()) {
            return TextureType.VOLUME;
        } else {
            return TextureType.TEXTURE;
        }
    }



    /**
     * Checks if the {@link File} is a valid DDS-Image
     *
     * @param file
     * @return
     * @throws IOException
     */
    public static boolean isValidDDSImage(final File file) throws IOException {
        InputStream inputStream = new FileInputStream(file);
        boolean isDDSImage = DDSImage.isDDSImage(inputStream);
        inputStream.close();
        return isDDSImage;
    }

    /**
     * Returns the stored MipMaps as a {@link BufferedImage}-Array
     *
     * @return
     */
    public BufferedImage[] getAllMipMapsBI() {
        return mipMaps.getAllMipMapsArray();
    }

    /**
     * returns the stored MipMaps as {@link ByteBuffer}-Array
     *
     * @return
     */
    public List<BufferedImage> generateAllMipMaps() {
        MipMaps mipMaps = new MipMaps();
        mipMaps.generateMipMaps(getTopMipMap());
        return mipMaps.getAllMipMaps();
    }

    public BufferedImage getMipMap(int index) {
        return this.mipMaps.getMipMap(index);
    }

}
