package org.dds_utils.dropps.models;

import java.awt.image.BufferedImage;
import java.io.*;


public class ConversionResultBuilder {

    private String _error = null;
    private File _targetFile = null;
    private BufferedImage _image = null;

    public ConversionResultBuilder() { }

    public ConversionResult buildConversionResult() {
        return new ConversionResult(_error, _image, _targetFile);
    }

    public ConversionResultBuilder error(final String _error) {
        this._error = _error;
        return this;
    }

    public ConversionResultBuilder image(final BufferedImage _image) {
        this._image = _image;
        return this;
    }

    public ConversionResultBuilder targetFile(final File _targetFile) {
        this._targetFile = _targetFile;
        return this;
    }
}

