package de.danielsenff.radds.view.canvas;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeListener;

import model.TextureImage;
import net.miginfocom.swing.MigLayout;

import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;
import org.jdesktop.application.TaskService;

import ddsutil.BIUtil;
import ddsutil.ImageOperations;
import de.danielsenff.radds.Radds;
import de.danielsenff.radds.RaddsView;
import de.danielsenff.radds.models.ColorChannel;
import de.danielsenff.radds.tasks.LoadImageTask;
import de.danielsenff.radds.util.FileDrop;

/**
 * Control-Panel for the {@link BICanvas}.
 * This contains controls and components to manipulate the canvas. 
 * @author Daniel Senff
 *
 */
public class CanvasControlsPanel extends JPanel {
	private static final long serialVersionUID = -3204480115770320549L;
	private final BICanvas canvas;
	private final JComboBox<Integer> zoomCombo;
	private final JSlider zoomSlider;
	private final RaddsView view;
	private final JComboBox<Integer> mipMapCombo;
	private final JScrollPane scrollViewPane;
	private boolean fitSize;

	/**
	 * @param view
	 */
	public CanvasControlsPanel(final RaddsView view) {
		this.view = view;

		setLayout(new BorderLayout());
		canvas = newBICanvas();
		scrollViewPane = newScrollCanvas();
		mipMapCombo = newMipmapCombo();
		zoomSlider = newZoomSlider();
		zoomCombo = newZoomCombo();

		this.add(scrollViewPane, BorderLayout.CENTER);
		this.add(newNavigationPanel(), BorderLayout.SOUTH);
	}

	private FileDrop newFileDrop(JComponent component) {
		return new FileDrop(component, files -> openImage(files[0]));
	}

	private PropertyChangeListener newListenerForZoomLevel() {
		return pChangeEvent -> {
			if(!pChangeEvent.getPropertyName().equals("zoomFactor")){
				return;
			}
			float newValue = (Float) pChangeEvent.getNewValue();
			Integer percentage = Integer.valueOf((int) (newValue * 100));
			zoomSlider.setValue(percentage);
			zoomCombo.setSelectedItem("" +percentage);
		};
	}

	private void openImage(File file) {
		LoadImageTask convertTask = new LoadImageTask(file);
		TaskService ts = Radds.getApplication().getContext().getTaskService();
		ts.execute(convertTask);
	}

	/**
	 * Initialize components in the navigation panel for this BICanvas. 
	 * @return
	 */
	private JPanel newNavigationPanel() {
		MigLayout miglayout = new MigLayout("nogrid, fillx, wrap, gap 0, ins 0");
		final JPanel panel = new JPanel();
		panel.setLayout(miglayout);
		panel.add(new JButton(view.getAction("copy")), "sg");
		panel.add(new JButton(view.getAction("export")), "sg");
		panel.add(newTransparencyColorButton(), "sg");

		// Color Channel
		
		final JLabel lblChannelCombo = new JLabel(getResourceMap().getString("Channels")+":");
		panel.add(lblChannelCombo, "gap push");
		panel.add(newColorChannelCombo(), "sg");

		// MipMaps
		
		JLabel lblMipMap = new JLabel(getResourceMap().getString("MipMap")+":");
		panel.add(lblMipMap, "gap push");
		panel.add(mipMapCombo, "sg, wrap");

		// ZO_om
		
		panel.add(new JLabel(getResourceMap().getString("Zoom")+":"), "");
		panel.add(newZoomCombo(), "");
		panel.add(zoomSlider, "");
		panel.add(newFitZoomCheckbox(), "");

		return panel;
	}

	private JCheckBox newFitZoomCheckbox() {
		JCheckBox chkZoomFitSize = new JCheckBox(getResourceString("Zoom_to_fit"));
		chkZoomFitSize.addChangeListener(newChangeListenerForResizeFit());
		return chkZoomFitSize;
	}

	private ChangeListener newChangeListenerForResizeFit() {
		return e -> {
			fitSize = ((JCheckBox)e.getSource()).isSelected();
			resizeCanvasToFit(fitSize);
		};
	}

	private JButton newTransparencyColorButton() {
		final JButton bgColorButton = new JButton(getResourceString("Background"));
		bgColorButton.setToolTipText(getResourceString("set_background_color"));
		bgColorButton.setIcon(initColorImageIcon(canvas.getTransparencyColor()));
		bgColorButton.addActionListener(newActionListenerSettingBgColor(bgColorButton));
		return bgColorButton;
	}

	private ActionListener newActionListenerSettingBgColor(JButton bgColorButton) {
		return e -> {
			BICanvas canvas = getCanvas();
			Color returnValue = null;
			try {
				returnValue = JColorChooser.showDialog(canvas,
						getResourceString("set_background_color"),
						canvas.getTransparencyColor());
				if(returnValue != null) {
					canvas.setTransparencyColor(returnValue);
					canvas.repaint();
					bgColorButton.setIcon(initColorImageIcon(returnValue));
					bgColorButton.repaint();
				}
			} catch (Exception ex) {

			}
		};
	}

	private JComboBox<ColorChannel> newColorChannelCombo() {
		final JComboBox<ColorChannel> channelCombo = new JComboBox<>(composeColorChannelModel());
		channelCombo.addActionListener(newActionListenerForChannelChoice());
		return channelCombo;
	}

	private ActionListener newActionListenerForChannelChoice() {
		return event -> {
			final JComboBox<ColorChannel> channelCombo = (JComboBox<ColorChannel>) event.getSource();
			final ColorChannel channel = (ColorChannel) channelCombo.getSelectedItem();
			canvas.setChannelMode(channel.getChannel());
		};
	}

	private ImageIcon initColorImageIcon(Color color) {
		BufferedImage colorImage = new BufferedImage(16, 16, BufferedImage.TYPE_4BYTE_ABGR);
		for (int y = 0; y < colorImage.getHeight(); y++) {
			for (int x = 0; x < colorImage.getWidth(); x++) {
				colorImage.setRGB(x, y, color.getRGB());
			}
		}

		return new ImageIcon(colorImage);
	}

	private JSlider newZoomSlider() {
		JSlider zoomSlider = new JSlider(0, 500, 100);
		Hashtable<Integer, JComponent> labels = new Hashtable<>();
		labels.put(10, new JLabel("0.1x"));
		labels.put(100, new JLabel("1x"));
		labels.put(250, new JLabel("2.5x"));
		labels.put(500, new JLabel("5x"));
		zoomSlider.setLabelTable(labels);
		zoomSlider.setMajorTickSpacing(100);
		zoomSlider.setMinorTickSpacing(25);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.setPreferredSize(new Dimension(250, 14));
		zoomSlider.addChangeListener(e -> {
			final int zoomValue = ((JSlider)e.getSource()).getValue();
			canvas.setZoomFactor( (Float.valueOf(zoomValue)) * 0.01f);
			canvas.revalidate();
		});
		return zoomSlider;
	}

	private JComboBox<Integer> newMipmapCombo() {
		JComboBox<Integer> mipMapCombo = new JComboBox<>();
		updateNumMipMaps();
		mipMapCombo.addActionListener(createActionListenerForMipMapLevel());
		return mipMapCombo;
	}

	private ActionListener createActionListenerForMipMapLevel() {
		return e -> {
			if(mipMapCombo.getItemCount() > 0) {
				int index = (Integer) ((JComboBox<Integer>)e.getSource()).getSelectedItem();
				TextureImage textureImage = view.getTextureImage();
				if(index < textureImage.getNumMipMaps()) {
					BufferedImage bi = textureImage.getMipMap(index-1);
					canvas.setSourceBI(bi);
					canvas.revalidate();
				}
			}
		};
	}

	private JComboBox newZoomCombo() {
		final String[] defaultZooms = { "25", "50", "100", "150", "200", "400"};
		JComboBox zoomCombo = new JComboBox(defaultZooms);
		zoomCombo.setSelectedIndex(2);
		zoomCombo.setEditable(true);
		zoomCombo.addActionListener(newZoomComboListener());
		zoomCombo.addKeyListener(newZoomComboKeyListener());
		return zoomCombo;
	}

	private ActionListener newZoomComboListener() {
		return e -> {
			float zoomValue = (Float.valueOf((String) zoomCombo.getSelectedItem())) / 100;
			canvas.setZoomFactor( zoomValue);
			canvas.repaint();
		};
	}

	private KeyAdapter newZoomComboKeyListener() {
		return new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					float zoomValue = (Float.valueOf((String) zoomCombo.getSelectedItem())) / 100;
					canvas.setZoomFactor( zoomValue);
					canvas.repaint();
				}
			}
		};
	}

	private void resizeCanvasToFit(boolean fitSize) {
		float zoomValue = (Float.valueOf((String) zoomCombo.getSelectedItem())) * 0.01f;
		if (fitSize) {
			zoomValue = calculateZoomfactorFitWidth();
		}
		canvas.setZoomFactor(zoomValue);
		canvas.repaint();
	}

	public float calculateZoomfactorFitWidth() {
		Insets insets = scrollViewPane.getBorder().getBorderInsets(canvas);
		float factorWidth = (float) ( (scrollViewPane.getSize().getWidth()-insets.left-insets.right) / canvas.getSource().getWidth());
		float factorHeight = (float) ( (scrollViewPane.getSize().getHeight()-insets.bottom-insets.top) / canvas.getSource().getHeight());
		
		return Math.min(factorWidth, factorHeight);
	}

	private String getResourceString(final String text) {
		return getResourceMap().getString(text);
	}

	/**
	 * Number of MipMaps of the currently displayed texture.
	 */
	public void updateNumMipMaps() {
		TextureImage textureImage = view.getTextureImage();
		if(textureImage != null) {
			mipMapCombo.removeAllItems();
			for (int i = 0; i < textureImage.getNumMipMaps(); i++) {
				mipMapCombo.addItem(i+1);
			}
		}
	}

	/**
	 * Init ComboBox for selecting different Color Channels of an Image.
	 * @return
	 */
	private DefaultComboBoxModel<ColorChannel> composeColorChannelModel() {
		final DefaultComboBoxModel<ColorChannel> combo = new DefaultComboBoxModel<ColorChannel>();
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RGB, getResourceString("rgb_channels")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RED, getResourceString("r_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.GREEN, getResourceString("g_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.BLUE, getResourceString("b_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.ALPHA, getResourceString("a_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RGBA, getResourceString("rgba_transparent")));
		return combo;
	}

	private ResourceMap getResourceMap() {
		final Radds instance = Radds.getInstance(Radds.class);
		final ApplicationContext context = instance.getContext();
		return context.getResourceMap(RaddsView.class);
	}

	private ImageIcon getResourceIcon(String file){
		return new ImageIcon(Class.class.getResource(file));
	}

	private BICanvas newBICanvas() {
		final ImageIcon defaultImage = (ImageIcon)getResourceMap().getIcon("Canvas.default_image");
		BICanvas canvas = new BICanvas(BIUtil.convertImageToBufferedImage(defaultImage.getImage(),
				BufferedImage.TYPE_4BYTE_ABGR));
		canvas.addPropertyChangeListener(newListenerForZoomLevel());
		return canvas;
	}

	private JScrollPane newScrollCanvas() {
		final JScrollPane scrollViewPane = newCanvasScrollPane(canvas);
		
		final ScrollCanvasListener scrollCanvasListener = new ScrollCanvasListener(scrollViewPane);
		canvas.addMouseMotionListener(scrollCanvasListener);
		//		canvas.addMouseWheelListener(scrollCanvasListener);
		canvas.addKeyListener(scrollCanvasListener);
		canvas.setFocusable(true);
		canvas.requestFocus();
		canvas.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				canvas.requestFocus();
			}
		});
		canvas.addRenderedChangeListener(e -> resizeCanvasToFit(fitSize));

		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		canvas.addAncestorListener(newCanvasAncestorListener());

		newFileDrop(scrollViewPane);

		return scrollViewPane;
	}

	private JScrollPane newCanvasScrollPane(JPanel panel) {
		JScrollPane scrollPane = new JScrollPane(panel);
		scrollPane.setPreferredSize(new Dimension(700,300));
		scrollPane.addComponentListener(new ComponentAdapter() {

			@Override
			public void componentResized(ComponentEvent event) {
				resizeCanvasToFit(fitSize);
			}
		});
		return scrollPane;
	}

	private AncestorListener newCanvasAncestorListener() {
		return new AncestorListener() {
			@Override
			public void ancestorAdded(final AncestorEvent arg0) {}
			@Override
			public void ancestorMoved(final AncestorEvent arg0) {
				scrollViewPane.repaint();
			}
			@Override
			public void ancestorRemoved(final AncestorEvent arg0) {	}
		};
	}

	/**
	 * Returns the BufferedImage drawing canvas
	 * @return
	 */
	public BICanvas getCanvas() {
		return this.canvas;
	}

}
