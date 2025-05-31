/**
 * 
 */
package org.dds_utils.dropps.view;

import org.model.ExportConfiguration;
import org.jogl.DDSImage;

import javax.swing.*;
import java.awt.*;


/**
 *
 * Panel with the export settings for the dds files

 * @author danielsenff
 *
 */
public class SettingsPanel extends JPanel {

	private JComboBox comboPixelformat;
	private JCheckBox chkMipMaps;
	private ExportConfiguration exportOptions;
	private JCheckBox chkWhiteAlpha;

	public SettingsPanel() {
		super();

		init();
	}

	public void setExportConfiguration(ExportConfiguration exportOptions) {
		this.exportOptions = exportOptions;
	}

	private void init() {
		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4,4,4,4);  //top padding
		c.anchor = GridBagConstraints.LINE_START;

		
		final JCheckBox chkKeepOriginal = new JCheckBox("keep old files");
		chkKeepOriginal.setSelected(true);
		chkKeepOriginal.addActionListener(arg0 -> exportOptions.setKeepOriginal(((JCheckBox)arg0.getSource()).isSelected()));
		
		
		final JCheckBox chkBackup = new JCheckBox("backup_old_files");
		chkBackup.setSelected(true);
		chkBackup.addActionListener(event -> {
			boolean selected = ((JCheckBox)event.getSource()).isSelected();
			exportOptions.setMakeBackup(selected);
			chkKeepOriginal.setEnabled(selected);
		});
		
		c.gridx = 0;
		c.gridy = 4;
		add(new JSeparator(),c);
		


		chkWhiteAlpha = setupChkWhiteAlpha();
		chkMipMaps = setupChkMipMaps();
		comboPixelformat = setupSelectPixelformat();


		c.gridx = 0;
		c.gridwidth=2;
		c.gridy = 5;
		add(comboPixelformat,c);
	

		c.gridx = 0;
		c.gridy = 6;
		add(chkWhiteAlpha, c);

		c.gridx = 0;
		c.gridy = 7;
		add(chkMipMaps,c);
		
		c.gridx = 0;
		c.gridy = 8;
//		add(chkBackup,c);
		
		c.gridx = 0;
		c.gridy = 9;
//		add(chkKeepOriginal,c);


	}

	private JCheckBox setupChkWhiteAlpha() {
		JCheckBox chkWhiteAlpha = new JCheckBox("white_alpha_channel_recommended");
		chkWhiteAlpha.setToolTipText("If you have a DDSImage with a 8bit alpha and reduce its alpha to 1bit \n" +
				"it'll make the pixel either white or black with value 128 as threshold. In most cases, if you want \n" +
				"a DXT1 file, you want a clean white alpha. This function paints the alpha channel white.");
		chkWhiteAlpha.addActionListener(arg0 -> {
			final boolean selected = ((JCheckBox)arg0.getSource()).isSelected();
			exportOptions.setPaintWhiteAlpha(selected);
		});
		chkWhiteAlpha.setSelected(false);
		chkWhiteAlpha.setEnabled(false);
		return chkWhiteAlpha;
	}

	private JComboBox setupSelectPixelformat() {
		String[] compression_options = {
			"DXT5 compression (8bit alpha)",
			"DXT3 compression",
			"DXT1 compression (1bit or no alpha)",
			"A8R8G8B8 uncompressed",
			"R8G8B8 uncompressed",
			"X8R8G8B8 uncompressed" };
		JComboBox comboPixelformat = new JComboBox(compression_options);
		comboPixelformat.addActionListener(event -> {
			final int newPixelformatIndex = ((JComboBox)event.getSource()).getSelectedIndex();
			final int newPixelformat = convertIndexPixelFormat(newPixelformatIndex);
			exportOptions.setNewPixelformat(newPixelformat);
			if(newPixelformat == DDSImage.D3DFMT_DXT1) {
				chkWhiteAlpha.setEnabled(true);
				chkWhiteAlpha.setSelected(true);
				exportOptions.setPaintWhiteAlpha(true);
			} else {
				chkWhiteAlpha.setEnabled(false);
				exportOptions.setPaintWhiteAlpha(false);
			}
		});

		comboPixelformat.setSelectedIndex(0);
		return comboPixelformat;
	}

	private JCheckBox setupChkMipMaps() {
		JCheckBox chkMipMaps = new JCheckBox("Generate MipMaps (recommended)");
		chkMipMaps.addActionListener(event -> {
			boolean selected = ((JCheckBox)event.getSource()).isSelected();
			exportOptions.setGenerateMipMaps(selected);
		});
		chkMipMaps.setSelected(exportOptions.hasGeneratedMipMaps());

		return chkMipMaps;
	}

	private int convertIndexPixelFormat(final int index) {
		return switch (index) { // keep
			default -> DDSImage.D3DFMT_DXT5;
			case 1 -> DDSImage.D3DFMT_DXT3;
			case 2 -> DDSImage.D3DFMT_DXT1;
			case 3 -> DDSImage.D3DFMT_A8R8G8B8;
			case 4 -> DDSImage.D3DFMT_R8G8B8;
			case 5 -> DDSImage.D3DFMT_X8R8G8B8;
		};
		
	}
}
