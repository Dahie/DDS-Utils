package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.io.*;


public class ConversionResultBuilder {

    private String _error = null;
    private File _targetFile = null;
    private TextureImage _textureImage = null;

    public ConversionResultBuilder() { }

    public ConversionResult buildConversionResult() {
        return new ConversionResult(_error, _textureImage, _targetFile);
    }

    public ConversionResultBuilder error(final String _error) {
        this._error = _error;
        return this;
    }

    public ConversionResultBuilder textureImage(final TextureImage _image) {
        this._textureImage = _image;
        return this;
    }

    public ConversionResultBuilder targetFile(final File _targetFile) {
        this._targetFile = _targetFile;
        return this;
    }
}

