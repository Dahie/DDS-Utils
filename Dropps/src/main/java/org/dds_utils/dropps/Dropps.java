package org.dds_utils.dropps;


import com.formdev.flatlaf.FlatLightLaf;
import org.dds_utils.dropps.models.ExportConfigurationFactory;
import org.dds_utils.dropps.view.DroppsView;
import org.model.ExportConfiguration;

import javax.swing.*;

/**
 * @author danielsenff
 *
 */
public class Dropps {
	public static void main(final String[] args) {
		FlatLightLaf.setup();

		ExportConfiguration options = ExportConfigurationFactory.buildDefault();

		ConvertController convertController = new ConvertController(options);

		SwingUtilities.invokeLater(new DroppsView(convertController));
	}

}
