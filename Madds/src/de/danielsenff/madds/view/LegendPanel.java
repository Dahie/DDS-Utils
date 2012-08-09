package de.danielsenff.madds.view;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LegendPanel extends JPanel {

	public LegendPanel() {
		setPreferredSize(new Dimension(100, 120));
		
		JLabel labelDiffuse = new JLabel("Diffuse map");
		labelDiffuse.setBackground(ColorPalette.colorDiffuse);
		labelDiffuse.setOpaque(true);
		add(labelDiffuse);
		
		JLabel labelSpecular = new JLabel("Specular map");
		labelSpecular.setBackground(ColorPalette.colorSpecular);
		labelSpecular.setOpaque(true);
		add(labelSpecular);
		
		JLabel labelNormal = new JLabel("Normal map");
		labelNormal.setBackground(ColorPalette.colorNormal);
		labelNormal.setOpaque(true);
		add(labelNormal);
		
		JLabel labelAnimation = new JLabel("Animation");
		labelAnimation.setBackground(ColorPalette.colorAnimation);
		labelAnimation.setOpaque(true);
		add(labelAnimation);
		
		JLabel labelMisc = new JLabel("Misc");
		labelMisc.setOpaque(true);
		labelMisc.setBackground(ColorPalette.colorOther);
		add(labelMisc);
	}
	
}
