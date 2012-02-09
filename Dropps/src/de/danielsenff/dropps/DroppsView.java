package de.danielsenff.dropps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Vector;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import jogl.DDSImage;

import org.jdesktop.application.Action;
import org.jdesktop.application.Application;
import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.FrameView;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.Task;
import org.jdesktop.application.TaskMonitor;
import org.jdesktop.application.TaskService;
import org.jdesktop.application.Task.BlockingScope;

import de.danielsenff.dropps.models.ExportOptions;
import de.danielsenff.dropps.models.ProgressStatus;
import de.danielsenff.dropps.tasks.ConvertFilesTask;
import de.danielsenff.dropps.util.FileDrop;


/**
 * View and Mainframe
 * @author Daniel Senff
 *
 */
public class DroppsView extends FrameView {

    private javax.swing.JPanel mainPanel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusAnimationLabel;
    private javax.swing.JLabel statusMessageLabel;
    private javax.swing.JPanel statusPanel;

    private final Timer messageTimer;
    private final Timer busyIconTimer;
    private final Icon idleIcon;
    private final Icon[] busyIcons = new Icon[15];
    private int busyIconIndex = 0;

    private static final Logger logger = Logger.getLogger(DroppsView.class.getName());
	
    private final ExportOptions options;
    private SettingsPanel settingsPanel;
	private JList dropPanel;
	
	/**
	 * @param app
	 */
	public DroppsView(final Application app) {
		super(app);

        this.options = new ExportOptions(2048, 2048, DDSImage.D3DFMT_DXT5, true);
		
		// generated GUI builder code
        initComponents();

        
        // status bar initialization - message timeout, idle icon and busy animation, etc
//        final ResourceMap resourceMap = getResourceMap();
        final Dropps instance = Application.getInstance(Dropps.class);
		final ApplicationContext context = instance.getContext();
		final org.jdesktop.application.ResourceMap resourceMap = context.getResourceMap(DroppsView.class);
        
        
		final int messageTimeout = resourceMap.getInteger("StatusBar.messageTimeout");
        messageTimer = new Timer(messageTimeout, new ActionListener() {
        	@Override
            public void actionPerformed(final ActionEvent e) {
//                statusMessageLabel.setText("");
            }
        });
        messageTimer.setRepeats(false);
        final int busyAnimationRate = resourceMap.getInteger("StatusBar.busyAnimationRate");
        for (int i = 0; i < busyIcons.length; i++) {
            busyIcons[i] = resourceMap.getIcon("StatusBar.busyIcons[" + i + "]");
        }
        busyIconTimer = new Timer(busyAnimationRate, new ActionListener() {
        	@Override
            public void actionPerformed(final ActionEvent e) {
                busyIconIndex = (busyIconIndex + 1) % busyIcons.length;
                statusAnimationLabel.setIcon(busyIcons[busyIconIndex]);
            }
        });
        idleIcon = resourceMap.getIcon("StatusBar.idleIcon");
        statusAnimationLabel.setIcon(idleIcon);
        progressBar.setVisible(true);

        // connect action tasks to status bar via TaskMonitor
        final TaskMonitor taskMonitor = new TaskMonitor(getApplication().getContext());
        taskMonitor.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
        	@Override
            public void propertyChange(final java.beans.PropertyChangeEvent evt) {
                final String propertyName = evt.getPropertyName();
                System.out.println("PropertyName: "+propertyName);
                if ("started".equals(propertyName)) {
                    if (!busyIconTimer.isRunning()) {
                        statusAnimationLabel.setIcon(busyIcons[0]);
                        busyIconIndex = 0;
                        busyIconTimer.start();
                    }
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                } else if ("done".equals(propertyName)) {
                    busyIconTimer.stop();
                    statusAnimationLabel.setIcon(idleIcon);
                    progressBar.setVisible(false);
                    progressBar.setValue(0);
//                    statusMessageLabel.setText(resourceMap.getString("statusMessageLabel.text"));
                    statusMessageLabel.setText(resourceMap.getString("StatusBar.conversion_finished"));
                } else if ("message".equals(propertyName)) {
                    final String text = (String)(evt.getNewValue());
                    if(text != null)
                    	statusMessageLabel.setText(text); // this way we don't overwrite if message is empty
                    messageTimer.restart();
                } else if ("progress".equals(propertyName)) {
                    final int value = (Integer)(evt.getNewValue());
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(false);
                    progressBar.setValue(value);
//                    statusMessageLabel.setText(evt.get)
                }
            }
        });
	}


	

	/**
	 * GUI definiton
	 * 
	 */
	
	private void initComponents() {

		this.getFrame().setResizable(false);
		
        mainPanel = new javax.swing.JPanel();
        mainPanel.setName("mainPanel"); // NOI18N
        mainPanel.setLayout(new BorderLayout());
        
        statusPanel = new javax.swing.JPanel();
        statusPanel.setName("statusPanel"); // NOI18N
        
        
        statusMessageLabel = new javax.swing.JLabel();
        statusMessageLabel.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        statusMessageLabel.setName("statusMessageLabel");
        
        statusAnimationLabel = new javax.swing.JLabel();
        statusAnimationLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        statusAnimationLabel.setName("statusAnimationLabel"); // NOI18N        
        
        progressBar = new javax.swing.JProgressBar();
        progressBar.setName("progressBar"); // NOI18N
        
        statusPanel.setLayout(new BorderLayout());
        statusPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        statusPanel.add(statusAnimationLabel, BorderLayout.LINE_START);
        statusPanel.add(statusMessageLabel, BorderLayout.CENTER);
        statusPanel.add(progressBar, BorderLayout.LINE_END);


		final ResourceMap resourceMap = getResourceMap();
        resourceMap.injectComponents(mainPanel);
        resourceMap.injectComponents(statusPanel);
        
        this.settingsPanel = new SettingsPanel(this.options);
        this.settingsPanel.setName("settingsPanel");
        settingsPanel.setBorder(
        		BorderFactory.createTitledBorder(resourceMap.getString("SettingsPanel.title")));
        resourceMap.injectComponents(settingsPanel);
        mainPanel.add(settingsPanel, BorderLayout.LINE_START);

        
        final DefaultListModel dlm = new DefaultListModel();
        dropPanel = new JList(dlm) {
        	@Override
        	public void paintComponent(Graphics g) {
        		super.paintComponent(g);
        		if(this.getModel().getSize() == 0) {
        			final int fontsize = 24;
        			final Font font = g.getFont();
        			final Font newFont = new Font(font.getFamily(), Font.BOLD, fontsize);
        			g.setFont(newFont);
        			g.setColor(new Color(128, 128, 128));
        			// and if even now to small, then cut
        			g.drawString(resourceMap.getString("DropsPanel.title"), 15, 25);
        			g.setFont(font);
        			g.drawString(resourceMap.getString("DropsPanel.text"), 15, 45);
        		}
        	}
        };
        JScrollPane scrollpane = new JScrollPane(dropPanel);
        scrollpane.setPreferredSize(new Dimension(350, 190));
        mainPanel.add(scrollpane, BorderLayout.CENTER);
        
        new FileDrop( null, dropPanel, /*dragBorder,*/ new FileDrop.Listener()
        {   
        	@Override
			public void filesDropped( final java.io.File[] files )
            {   
				
				ArrayList<File> filesArrayList = new ArrayList<File>();
				for( int i = 0; i < files.length; i++ )
                {   
					File file = files[i];
					dlm.addElement(file);
					filesArrayList.add(file);
        			
                }   // end for: through each dropped file
				
//				System.out.println("Width: " + options.getNewWidth() + " Height: " + options.getNewHeight());
//				System.out.println("Pixelformat: " + DDSFile.verbosePixelformat(options.getNewPixelformat()) + " with MipMaps " + options.hasGeneratedMipMaps());
				
				Task convertTask = new ConvertFilesTask(getApplication(), filesArrayList, options);
				TaskService ts = Dropps.getApplication().getContext().getTaskService();
				ts.execute(convertTask);
            }   // end filesDropped
        }); // end FileDrop.Listener
        
        setComponent(mainPanel);
        setStatusBar(statusPanel);
	}
	
	
	
	/**
	 * @return the dropPanel
	 */
	public JList getDropPanel() {
		return dropPanel;
	}
	
	/*
	 * Action definitions
	 * 
	 */

	/**
	 * @return
	 */
	@Action(block = BlockingScope.APPLICATION)
	public Task<ProgressStatus, Void> convertFiles() {
		final Vector<File> filesAwaitingProgression = null;
		return new ConvertFilesTask(getApplication(), filesAwaitingProgression, options);
	}

	
}
