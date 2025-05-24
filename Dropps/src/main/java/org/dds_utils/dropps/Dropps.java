package org.dds_utils.dropps;


import com.formdev.flatlaf.FlatLightLaf;
import org.dds_utils.dropps.models.ExportConfiguration;
import org.jogl.DDSImage;

import javax.swing.*;

/**
 * @author danielsenff
 *
 */
public class Dropps {
	public static void main(final String[] args) {
		FlatLightLaf.setup();

		ExportConfiguration options = new ExportConfiguration(2048, 2048, DDSImage.D3DFMT_DXT5, true);

		ConvertController convertController = new ConvertController(options);

		SwingUtilities.invokeLater(new DroppsView(convertController));
	}

}
