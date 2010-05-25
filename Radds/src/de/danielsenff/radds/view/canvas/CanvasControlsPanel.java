/**
 * 
 */
package de.danielsenff.radds.view.canvas;

import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Hashtable;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.jdesktop.application.ApplicationContext;
import org.jdesktop.application.ResourceMap;

import com.sun.j3d.utils.behaviors.vp.WandViewBehavior.ResetViewListener;

import ddsutil.BIUtil;
import ddsutil.ImageOperations;
import de.danielsenff.radds.Radds;
import de.danielsenff.radds.RaddsView;
import de.danielsenff.radds.controller.Application;
import de.danielsenff.radds.models.ColorChannel;
import de.danielsenff.radds.util.FileDrop;

/**
 * Control-Panel for the {@link BICanvas}. 
 * @author Daniel Senff
 *
 */
public class CanvasControlsPanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3204480115770320549L;
	private BICanvas canvas;
	private JComboBox zoomCombo;
	private JSlider zoomSlider;
	private RaddsView view;

	/**
	 * @param controller
	 */
	public CanvasControlsPanel(final RaddsView view) {
		this.view = view;

		setLayout(new BorderLayout());
		final JPanel navigateCanvas = initNavigationPanel();

		final JScrollPane scrollViewPane = initScrollCanvas();

		getCanvas().addPropertyChangeListener(new PropertyChangeListener() {
			public void propertyChange(PropertyChangeEvent pChangeEvent) {
				if(pChangeEvent.getPropertyName().equals("zoomFactor")){
					float newValue = (Float) pChangeEvent.getNewValue();
					Integer percentage = Integer.valueOf((int) (newValue * 100));
					zoomSlider.setValue(percentage);
					zoomCombo.setSelectedItem("" +percentage);
				}
			}
		});

		new FileDrop( scrollViewPane, new FileDrop.Listener(){   
			public void filesDropped( java.io.File[] files ) {   
				// handle file drop

				for (int i = 0; i < files.length; i++) {
					File file = files[i];
					// TODO FileDrop 
//					controller.setImage(file);
				}

			}   // end filesDropped
		}); // end FileDrop.Listener
		
		this.add(scrollViewPane, BorderLayout.CENTER);
		this.add(navigateCanvas, BorderLayout.SOUTH);
	}

	private JPanel initNavigationPanel() {
		final JPanel panel = new JPanel();
//		TODO COPY BUTTON
		final JButton copyButton = new JButton(view.getAction("copy"));
		panel.add(copyButton);
		
		final JComboBox channelCombo = new JComboBox(composeColorChannelModel());
		channelCombo.addActionListener(new ActionListener() {

			public void actionPerformed(final ActionEvent event) {
				final JComboBox channelCombo = (JComboBox) event.getSource();
				final ColorChannel channel = (ColorChannel) channelCombo.getSelectedItem();
				canvas.setChannelMode(channel.getChannel());
			}

		});

		

		final JLabel lblChannelCombo = new JLabel(getResourceMap().getString("Channels")+":");

		panel.add(lblChannelCombo);
		panel.add(channelCombo);

		final String[] defaultZooms = { "25", "50", "100", "150", "200", "400"};
		zoomCombo = new JComboBox(defaultZooms);
		zoomCombo.setSelectedIndex(2);
		zoomCombo.setEditable(true);
		zoomCombo.addActionListener(new ActionListener() {
			public void actionPerformed(final ActionEvent e) {
				float zoomValue = (Float.valueOf((String) zoomCombo.getSelectedItem())) / 100;
				canvas.setZoomFactor( zoomValue);
				canvas.repaint();
			}
		});
		zoomCombo.addKeyListener(new KeyListener() {

			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ENTER) {
					float zoomValue = (Float.valueOf((String) zoomCombo.getSelectedItem())) / 100;
					canvas.setZoomFactor( zoomValue);
					canvas.repaint();
				}
			}

			public void keyReleased(KeyEvent e) {}

			public void keyTyped(KeyEvent e) {}
			
		});
		

		final JLabel lblZoom = new JLabel(getResourceMap().getString("Zoom")+":");

		zoomSlider = new JSlider(0, 500, 100);
		Hashtable<Integer, JComponent> labels = new Hashtable<Integer, JComponent>();
		labels.put(10, new JLabel("0.1x"));
		labels.put(100, new JLabel("1x"));
		labels.put(250, new JLabel("2.5x"));
		labels.put(500, new JLabel("5x"));
		zoomSlider.setLabelTable(labels);
		zoomSlider.setMajorTickSpacing(100);
		zoomSlider.setMinorTickSpacing(25);
		zoomSlider.setPaintTicks(true);
		zoomSlider.setPaintLabels(true);
		zoomSlider.addChangeListener(new ChangeListener() {

			public void stateChanged(final ChangeEvent e) {
				final int zoomValue = ((JSlider)e.getSource()).getValue();
				canvas.setZoomFactor( (Float.valueOf(zoomValue)) / 100);
				canvas.repaint();
			}

		});

		panel.add(lblZoom);
		panel.add(zoomCombo);
		panel.add(zoomSlider);

		//TODO fit to width/optimal



		return panel;
	}


	/**
	 * Init ComboBox for selecting different Color Channels of an Image.
	 * @return
	 */
	private DefaultComboBoxModel composeColorChannelModel() {
		final DefaultComboBoxModel combo = new DefaultComboBoxModel();
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RGB, getResourceMap().getString("rgb_channels")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.RED, getResourceMap().getString("r_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.GREEN, getResourceMap().getString("g_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.BLUE, getResourceMap().getString("b_channel")));
		combo.addElement(new ColorChannel(ImageOperations.ChannelMode.ALPHA, getResourceMap().getString("a_channel")));
		return combo;
	}

	private ResourceMap getResourceMap() {
		final Radds instance = Application.getInstance(Radds.class);
		final ApplicationContext context = instance.getContext();
		final org.jdesktop.application.ResourceMap resourceMap = context.getResourceMap(RaddsView.class);
		return resourceMap;
	}

	private ImageIcon getResourceIcon(String file){
		return new ImageIcon(Class.class.getResource(file));
	}
	
	private JScrollPane initScrollCanvas() {

		final ImageIcon defaultImage = getResourceIcon("/de/danielsenff/radds/resources/defaultimage.png");

		canvas = new BICanvas(BIUtil.convertImageToBufferedImage(defaultImage.getImage(), 
						BufferedImage.TYPE_4BYTE_ABGR));
		final JScrollPane scrollViewPane = new JScrollPane(canvas);
		scrollViewPane.setPreferredSize(new Dimension(700,300));
		final ScrollCanvasListener scrollCanvasListener = new ScrollCanvasListener(scrollViewPane);
		canvas.addMouseMotionListener(scrollCanvasListener);
//		canvas.addMouseWheelListener(scrollCanvasListener);
		canvas.addKeyListener(scrollCanvasListener);
		canvas.setFocusable(true);
		canvas.requestFocus();

		canvas.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				canvas.requestFocus();
			}
			public void mouseEntered(MouseEvent arg0) {}
			public void mouseExited(MouseEvent arg0) {}
			public void mousePressed(MouseEvent arg0) {}
			public void mouseReleased(MouseEvent arg0) {}
		});
		
		canvas.setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
		canvas.addAncestorListener(new AncestorListener() {
			public void ancestorAdded(final AncestorEvent arg0) {}
			public void ancestorMoved(final AncestorEvent arg0) {
				scrollViewPane.repaint();
			}
			public void ancestorRemoved(final AncestorEvent arg0) {	}
		});

		return scrollViewPane;
	}

	/**
	 * Returns the BufferedImage drawing canvas
	 * @return
	 */
	public BICanvas getCanvas() {
		return this.canvas;
	}

}
