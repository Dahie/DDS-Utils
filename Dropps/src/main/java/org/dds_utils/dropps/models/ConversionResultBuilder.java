package org.dds_utils.dropps.models;

import org.model.TextureImage;

import java.io.*;


public class ConversionResultBuilder {

    private String _error = null;
    private ConversionExecution _conversionExecution = null;
    private TextureImage _textureImage = null;

    public ConversionResultBuilder() { }

    public ConversionResult buildConversionResult() {
        return new ConversionResult(_error, _textureImage, _conversionExecution);
    }

    public ConversionResultBuilder error(final String _error) {
        this._error = _error;
        return this;
    }

    public ConversionResultBuilder textureImage(final TextureImage _image) {
        this._textureImage = _image;
        return this;
    }

    public ConversionResultBuilder conversionExecution(final ConversionExecution _conversionExecution) {
        this._conversionExecution = _conversionExecution;
        return this;
    }
}

