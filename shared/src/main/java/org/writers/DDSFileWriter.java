package org.writers;

import org.jogl.DDSImage;
import org.model.DDSFile;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public class DDSFileWriter {

    DDSFile ddsFile;

    public DDSFileWriter(DDSFile ddsFile) {
        this.ddsFile = ddsFile;
    }

    public void write(final File targetFile) throws IOException {
        ByteBuffer[] mipmaps = new ByteBuffer[ddsFile.getNumMipMaps()];
        for (int i = 0; i < mipmaps.length; i++) {
            mipmaps[i] = DDSImage.read(targetFile).getMipMap(i).getData();
        }

        DDSImage outputDDS = DDSImage.createFromData(ddsFile.getPixelFormat(), ddsFile.getWidth(), ddsFile.getHeight(), mipmaps);
        outputDDS.write(targetFile);
        outputDDS.close();
    }
}
