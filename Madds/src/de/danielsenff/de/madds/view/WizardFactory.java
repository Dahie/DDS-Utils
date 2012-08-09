package de.danielsenff.de.madds.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

import net.bouthier.treemapSwing.TMView;

import org.ciscavate.cjwizard.PageFactory;
import org.ciscavate.cjwizard.WizardPage;
import org.ciscavate.cjwizard.WizardSettings;

import de.danielsenff.de.madds.Madds;
import de.danielsenff.de.madds.models.Inventorizer;

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
		final WizardPage wp = new WizardPage("Memory visualization", "Visualize DDS in directories") {
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
		final WizardPage wp = new WizardPage("Select directory", "Select directory") {
			
			public final String startFolder = System.getProperty("user.home");

			{
				setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
				
				JPanel intro = new JPanel();
				intro.setLayout(new BorderLayout());
				intro.setBackground(Color.WHITE);
				
				try {
					InputStream is = Madds.class.getResourceAsStream("/res/intro.jpg");
					ImageIcon imageIcon = new ImageIcon(ImageIO.read(is)); 
					intro.add(new JLabel(imageIcon), BorderLayout.LINE_START);
				} catch (IOException ex) {}
				
				JTextPane tp = new MoreWebbableView("res/guide.html");
				JScrollPane js = new JScrollPane(tp);
				js.setBorder(null);
				js.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
				
				intro.add(js);
				add(intro);
				
				
				
				
				add(new JSeparator());
				
				JPanel dirPanel = new JPanel();
				dirPanel.setLayout(new BorderLayout());
				JLabel label2 = new JLabel("<html><p>Select your Mod's Vehicles-folder.<p>");
				label2.setAlignmentX(LEFT_ALIGNMENT);
				dirPanel.add(label2, BorderLayout.LINE_START);
				
				JPanel panel = createDirectorySelectPanel(settings);
				panel.setAlignmentX(LEFT_ALIGNMENT);
				panel.setMaximumSize(new Dimension(800, 300));
				dirPanel.add(panel, BorderLayout.LINE_END);
				
				JLabel label3 = new JLabel("<html><p>This tool analyzes only DDS files. TGA, BMP or any other formats are ignored.");
				label3.setAlignmentX(LEFT_ALIGNMENT);
				dirPanel.add(label3, BorderLayout.PAGE_END);
				
				add(dirPanel);
				add(new JSeparator());
				
				
				
			}
			
			private JPanel createDirectorySelectPanel(
					final WizardSettings settings) {
				
				JPanel panel = new JPanel();
				panel.setOpaque(false);
				panel.setLayout(new BorderLayout());
				final JTextField textfield = new JTextField(startFolder);
				textfield.setEnabled(false);
				textfield.setPreferredSize(new Dimension(400, 20));
				textfield.setName("rootDirectory");

				JButton fileDialogButton = new JButton("Choose");
				fileDialogButton.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						File file = Madds.openFile(startFolder);
						if(file != null && file.exists()) {
							textfield.setText(file.getAbsolutePath());
							settings.put("rootDirectory", file.getAbsolutePath());
						}
						setNextEnabled(true);
					}
				});

				panel.add(textfield, BorderLayout.CENTER);
				panel.add(fileDialogButton, BorderLayout.LINE_END);
				return panel;
			}
			
			public void rendering(List<WizardPage> path, WizardSettings settings) {
				super.rendering(path, settings);
				setFinishEnabled(false);
				setNextEnabled(false);
			} 
			
			@Override
			public void updateSettings(WizardSettings settings) {
				super.updateSettings(settings);
				String rootDirectory = (String)settings.get("rootDirectory");
				File rootFile = new File(rootDirectory);
				if(rootDirectory.isEmpty()) {
					System.err.println("root directory empty string");
				} else if(rootDirectory.equals(startFolder)) {
					System.err.println("root directory same as startfolder");
				} else if (!rootFile.exists()){
					System.err.println("root directory not found");
				} else {
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
