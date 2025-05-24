package org.dds_utils.dropps.models;

import java.io.IOException;

public interface IConverter {

    public ConversionResult perform() throws IOException;
    
}
