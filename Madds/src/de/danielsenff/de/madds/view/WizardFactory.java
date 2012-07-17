package de.danielsenff.de.madds.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import net.bouthier.treemapSwing.TMView;
import net.bouthier.treemapSwing.TreeMap;
import net.bouthier.treemapSwing.fileViewer.TMFileModelSize;

import org.ciscavate.cjwizard.PageFactory;
import org.ciscavate.cjwizard.WizardPage;
import org.ciscavate.cjwizard.WizardSettings;

import de.danielsenff.de.madds.Madds;
import de.danielsenff.de.madds.models.Inventorizer;
import de.danielsenff.de.madds.models.TMTextureModelDraw;
import de.danielsenff.de.madds.models.TMTextureNode;

public class WizardFactory implements PageFactory {

	@Override
	public WizardPage createPage(List<WizardPage> previewPages, WizardSettings settings) {
		return buildPage(previewPages.size(), settings);
	}
	
	private WizardPage buildPage(int pageCount, final WizardSettings settings) {
		switch (pageCount) {
		case 0:
			return createDirectorySelectPage(settings);
		case 1:
			return createTreeMapPage(settings);
		}
		return null;
	}

	
	private WizardPage createTreeMapPage(final WizardSettings settings) {
		final WizardPage wp = new WizardPage("Memory visualization", "hey") {
			{
				Inventorizer inventorizer = (Inventorizer)settings.get("inventorizer");
				TMView view = (TMView) settings.get("view");
				setLayout(new BorderLayout());
				add(view, BorderLayout.CENTER);
				
				JPanel statisticsPanel = new StatisticsPanel(inventorizer);
				
				add(statisticsPanel, BorderLayout.SOUTH);
			}
			
			public void rendering(List<WizardPage> path, WizardSettings settings) {
				super.rendering(path, settings);
				setFinishEnabled(true);
				setNextEnabled(false);
			} 
			
		};
		return wp;
	}

	private WizardPage createDirectorySelectPage(final WizardSettings settings) {
		final WizardPage wp = new WizardPage("Select directory", "hey") {
			{
				String text = "<html><b>Welcome to Madds to test Memory Access of DDS</b>"
						+"<p>This utility helps you to get a quick overview on the " +
						"texture memory usage of your Mod/Game." +
						"<p>Select the root-folder of your project to analyze all containing DDS-files.";
				add(new JLabel(text));
				String text2 = "Select your Mod's Vehicles-folder.";
				add(new JLabel(text2));
				JPanel panel = createDirectorySelectPanel(settings);
				add(panel);
			}

			private JPanel createDirectorySelectPanel(
					final WizardSettings settings) {
				JPanel panel = new JPanel();
				panel.setOpaque(false);
				panel.setLayout(new BorderLayout());
				final JTextField textfield = new JTextField();
				textfield.setEnabled(false);
				textfield.setPreferredSize(new Dimension(400, 20));
				textfield.setName("rootDirectory");

				JButton fileDialogButton = new JButton("Choose");
				fileDialogButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						File file = Madds.openFile();
						if(file != null && file.exists()) {
							textfield.setText(file.getAbsolutePath());
							settings.put("rootDirectory", file.getAbsolutePath());
						}
					}
				});

				panel.add(textfield, BorderLayout.CENTER);
				panel.add(fileDialogButton, BorderLayout.LINE_END);
				return panel;
			}
			
			public void rendering(List<WizardPage> path, WizardSettings settings) {
				super.rendering(path, settings);
				setFinishEnabled(false);
				setNextEnabled(true);
			} 
			
			@Override
			public void updateSettings(WizardSettings settings) {
				super.updateSettings(settings);
				String rootDirectory = (String)settings.get("rootDirectory");
				File rootFile = new File(rootDirectory);
				if(!rootDirectory.isEmpty() && rootFile.exists()) {
					Inventorizer inventorizer = new Inventorizer(".dds");
					inventorizer.startInventoring(rootFile);
					settings.put("inventorizer", inventorizer);

					// Now setup the treeview
					TMView showTMApp;
					try {
						showTMApp = Madds.showTMApp(inventorizer, rootFile);
						settings.put("view", showTMApp);
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		};
		return wp; 
	}

}
