/**
 * 
 */
package de.danielsenff.dropps;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;

import javax.swing.ComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import jogl.DDSImage;

import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;

import de.danielsenff.dropps.models.ExportOptions;
import de.danielsenff.dropps.models.Preset;
import de.danielsenff.dropps.models.PresetsComboModel;
import de.danielsenff.dropps.models.PresetsFactory;


/**
 * @author danielsenff
 *
 */
public class SettingsPanel extends JPanel implements PropertyChangeListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected JFormattedTextField fldNewHeight;
	protected JFormattedTextField fldNewWidth;
	protected JComboBox comboPixelformat;
	private JCheckBox chkMipMaps;
	private ExportOptions exportOptions;
	protected ResourceMap resourceMap;
	private PresetsComboModel presetsModel;
	
	/**
	 * Panel with the saving settings for the dds files
	 * @param controller
	 */
	public SettingsPanel(ExportOptions exportoptions) {
		this.exportOptions = exportoptions;
		
		this.resourceMap = Application.getInstance().getContext().getResourceMap(getClass(), SettingsPanel.class);
		
		init();
	}
	

	private void init() {
		setLayout(new GridBagLayout());
		final GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(4,4,4,4);  //top padding
		c.anchor = GridBagConstraints.LINE_START;
		
		final JLabel lblPresets = new JLabel(resourceMap.getString("Presets"));
		c.gridx = 0;
		c.ipady = 0;      //make this component tall
		c.gridwidth = 1;
		c.gridheight = 1;
		c.gridy = 0;
//		add(lblPresets,c);
		
		final NumberFormat numberformat = NumberFormat.getNumberInstance();
		numberformat.setGroupingUsed(false);
		this.fldNewWidth = new JFormattedTextField(numberformat);
		this.fldNewWidth.setColumns(5);
		this.fldNewWidth.addPropertyChangeListener(this);
		setNewWidth(exportOptions.getNewWidth());
		
		this.fldNewHeight = new JFormattedTextField(numberformat);
		this.fldNewHeight.setColumns(5);
		this.fldNewHeight.addPropertyChangeListener(this);
		setNewWidth(exportOptions.getNewHeight());
	
		this.presetsModel = PresetsFactory.getInstanceFromDefaultLocalFile();
//		final PresetsComboModel presetsModel = controller.getPresets();	
		final JComboBox comboPresets = new JComboBox(presetsModel);
		comboPresets.addActionListener(new ActionSetPresets());
		c.gridx = 0;
		c.gridwidth = 2;
		c.gridy = 1;
//		add(comboPresets,c);
		
		
		
		final JLabel lblNewWidth = new JLabel(resourceMap.getString("new_width"));
		c.gridx = 0;
		c.gridwidth = 1;
		c.gridy = 2;
//		add(lblNewWidth,c);
		
		
		c.gridx = 1;
		c.gridwidth = 1;
		c.gridy = 2;
//		add(fldNewWidth,c);
		
		final JLabel lblNewHeight = new JLabel(resourceMap.getString("new_height"));
		c.gridx = 0;
		c.gridy = 3;
//		add(lblNewHeight,c);
		
		
		c.gridx = 1;
		c.gridy = 3;
//		add(fldNewHeight,c);
		
		
		
		final JCheckBox chkWhiteAlpha = new JCheckBox(resourceMap.getString("White_alpha_channel_(recommended)"));
		chkWhiteAlpha.setToolTipText("If you have a DDSImage with a 8bit alpha and reduce it to 1bit \n" +
				"it'll make the pixel either white or black with value 128 as threshold. In most cases if you want \n" +
				"a DXT1 file, you want a clean white alpha. This function paints the alpha channel white.");
		chkWhiteAlpha.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				final boolean selected = ((JCheckBox)arg0.getSource()).isSelected();
				exportOptions.setPaintWhiteAlpha(selected);
			}
		});
		chkWhiteAlpha.setSelected(false);
		chkWhiteAlpha.setEnabled(false);
		
		
		final JCheckBox chkKeepOriginal = new JCheckBox(resourceMap.getString("keep_old_files"));
		chkKeepOriginal.setSelected(true);
		chkKeepOriginal.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
//				controller.getExportOptions().setKeepOriginal(((JCheckBox)arg0.getSource()).isSelected());
			}
		});
		
		
		final JCheckBox chkBackup = new JCheckBox(resourceMap.getString("backup_old_files"));
		chkBackup.setSelected(true);
		chkBackup.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				boolean selected = ((JCheckBox)arg0.getSource()).isSelected();
//				controller.getExportOptions().setMakeBackup(selected);
				chkKeepOriginal.setEnabled(selected);
			}
		});
		
		c.gridx = 0;
		c.gridy = 4;
		add(new JSeparator(),c);
		
		
		// Pixelformat
		
		final String[] presetsCompression = {
//				resourceMap.getString("Keep_original_pixelformat"), 
				"DXT5 compression (8bit alpha)", 
				"DXT3 compression", 
				"DXT1 compression (1bit or no alpha)",
				"A8R8G8B8 uncompressed",
				"R8G8B8 uncompressed",
				"X8R8G8B8 uncompressed"};
		this.comboPixelformat = new JComboBox(presetsCompression);
		this.comboPixelformat.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent e) {
				final int newPixelformat = convertIndexPixelFormat(((JComboBox)e.getSource()).getSelectedIndex());
				exportOptions.setNewPixelformat(newPixelformat);
				if(newPixelformat == DDSImage.D3DFMT_DXT1) {
					chkWhiteAlpha.setEnabled(true);
					chkWhiteAlpha.setSelected(true);
					exportOptions.setPaintWhiteAlpha(true);
				} else {
					chkWhiteAlpha.setEnabled(false);
					exportOptions.setPaintWhiteAlpha(false);
				}
			}
		});
		setNewPixelformatIndex(0);
		c.gridx = 0;
		c.gridwidth=2;
		c.gridy = 5;
		add(comboPixelformat,c);
	

		c.gridx = 0;
		c.gridy = 6;
		add(chkWhiteAlpha,c);
		
		
		
		chkMipMaps = new JCheckBox(resourceMap.getString("Generate_MipMaps_(recommended)"));
		chkMipMaps.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(final ActionEvent arg0) {
				boolean selected = ((JCheckBox)arg0.getSource()).isSelected();
				exportOptions.setGenerateMipMaps(selected);
			}	
		});
		setGeneratedMipMaps(exportOptions.hasGeneratedMipMaps());
		c.gridx = 0;
		c.gridy = 7;
		add(chkMipMaps,c);
		
		c.gridx = 0;
		c.gridy = 8;
//		add(chkBackup,c);
		
		c.gridx = 0;
		c.gridy = 9;
//		add(chkKeepOriginal,c);
		
//		comboPresets.setSelectedIndex(0); // set to default texture size
		
		String text = "<html>This programm automatically converts <br>" +
				"supported image files to DirectDrawSurface format.<br>" +
				"Just drop the files into the white area and the <br>" +
				"conversion will start immidiately.</html>";
		JLabel description = new JLabel(text);
		c.gridx = 0;
		c.gridy = 7;
		add(description);
	}


	private void setGeneratedMipMaps(boolean value) {
		chkMipMaps.setSelected(value);
		exportOptions.setGenerateMipMaps(value);
	}
	
	class ActionSetPresets implements ActionListener {

		/* (non-Javadoc)
		 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
		 */
		@Override
		public void actionPerformed(final ActionEvent e) {
			final JComboBox combo = (JComboBox) e.getSource();
			final ComboBoxModel model = combo.getModel();
			
			final Preset preset = (Preset) model.getSelectedItem();
			
			fldNewWidth.setValue(preset.getWidth());
			fldNewHeight.setValue(preset.getHeight());
			chkMipMaps.setSelected(preset.isMipmaps());
			
//			comboPixelformat.setSelectedIndex(3);
			//TODO clean up ... no hardcode
			if(preset.getPixelformat() == DDSImage.D3DFMT_DXT1) {
				comboPixelformat.setSelectedIndex(3);
			} else if (preset.getPixelformat() == DDSImage.D3DFMT_DXT5) {
				comboPixelformat.setSelectedIndex(1);
			} else {
				comboPixelformat.setSelectedIndex(1);
			}
			
		}
		
	}
	
	/**
	 * Set the width in the JTextfield
	 * @param newHeight
	 */
	public void setNewHeight(final int newHeight) {
		exportOptions.setNewHeight(newHeight);
		this.fldNewHeight.setValue(new Integer(newHeight));
	}

	/**
	 * Set the width in the JTextfield
	 * @param newWidth
	 */
	public void setNewWidth(final int newWidth) {
		this.exportOptions.setNewWidth(newWidth);
		this.fldNewWidth.setValue(new Integer(newWidth));
	}
	
	
	/** 
	 * Called when a field's "value" property changes. 
	 */
    @Override
	public void propertyChange(final PropertyChangeEvent e) {
        final Object source = e.getSource();
		if (source == this.fldNewWidth) {
            final int width = ((Number)fldNewWidth.getValue()).intValue();
			this.exportOptions.setNewWidth(width);
        } else if (source == this.fldNewHeight) {
        	final int height = ((Number)fldNewHeight.getValue()).intValue();
			this.exportOptions.setNewHeight(height);
        }

    }
    

	/**
	 * Get the selected Pixelformat
	 * @return
	 */
	public int getNewPixelformatIndex() {
		return this.comboPixelformat.getSelectedIndex();
	}
	
	public void setNewPixelformatIndex(int newPixelformatIndex) {
		exportOptions.setNewPixelformat(convertIndexPixelFormat(newPixelformatIndex));
		this.comboPixelformat.setSelectedIndex(newPixelformatIndex);
	}

	private int convertIndexPixelFormat(final int index) {
		switch(index) {
			default: // keep
//			case 0:
//				return 0;
			case 0:	
				return DDSImage.D3DFMT_DXT5; 
			case 1:	
				return DDSImage.D3DFMT_DXT3;
			case 2:	
				return DDSImage.D3DFMT_DXT1;
			case 3:	
				return DDSImage.D3DFMT_A8R8G8B8;
			case 4:	
				return DDSImage.D3DFMT_R8G8B8;
			case 5:	
				return DDSImage.D3DFMT_X8R8G8B8;
		}
		
	}
}
