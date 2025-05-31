package org.dds_utils.dropps.models;

import org.jogl.DDSImage;
import org.model.ExportConfiguration;

public class ExportConfigurationFactory {
    public static ExportConfiguration buildDefault() {
        return new ExportConfiguration(2048, 2048, DDSImage.D3DFMT_DXT5, true);
    }
}
