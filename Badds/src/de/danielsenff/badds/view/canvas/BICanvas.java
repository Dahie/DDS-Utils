package de.danielsenff.badds.view.canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

import javax.swing.Scrollable;

import de.danielsenff.badds.controller.Application;
import de.danielsenff.badds.view.GUI.JCPanel;

import DDSUtil.BIUtil;
import DDSUtil.ImageOperations;


/**
 * JComponent for drawing {@link BufferedImage}s.
 * @author danielsenff
 *
 */
public class BICanvas extends JCPanel implements Scrollable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	private BufferedImage biRendered;
	private BufferedImage biSource;
	private ImageOperations.ChannelMode channelMode;
	private float zoomFactor = 1.0f;
	
	/**
	 * Displays a {@link BufferedImage} in RGB-Mode 
	 * without multiplied Alpha-channel.
	 * @param biRendered BufferedImage to display
	 */
	public BICanvas(final Application controller, BufferedImage image) {
		this(controller, image, ImageOperations.ChannelMode.RGB);
	}
	
	
	/**
	 * Displays a {@link BufferedImage} in the channel-mode specified.
	 * @param biRendered BufferedImage to display
	 * @param channel Channel of the BufferedImage to display 
	 */
	public BICanvas(final Application controller, final BufferedImage image, final ImageOperations.ChannelMode channel) {
		super(controller);
		this.controller = controller;
		this.channelMode = channel;
		this.biRendered = image;
		this.biSource = image;
		changeChannelBi(channel, biRendered);
		
		this.setPreferredSize(new Dimension(biRendered.getWidth(), biRendered.getHeight()));
	}

	private void changeChannelBi(ImageOperations.ChannelMode channel, BufferedImage currentImage) {
		switch(channel){
			default:
			case RGBA:
				this.biRendered = currentImage;
				break;
			case RGB:
				this.biRendered = BIUtil.getRGBChannel(currentImage);
				break;
			case ALPHA:
				this.biRendered = BIUtil.getChannel(currentImage, ImageOperations.ChannelMode.ALPHA);
				break;
			case RED:
				this.biRendered = BIUtil.getChannel(currentImage, ImageOperations.ChannelMode.RED);
				break;
			case GREEN:
				this.biRendered = BIUtil.getChannel(currentImage, ImageOperations.ChannelMode.GREEN);
				break;
			case BLUE:
				this.biRendered = BIUtil.getChannel(currentImage, ImageOperations.ChannelMode.BLUE);
				break;
		}
		super.repaint();
		revalidate();
	}
	
	/**
	 * Changes the color-channel of the displayed BufferedImage
	 * @param channel new channel to display
	 */
	public void setChannelMode(final ImageOperations.ChannelMode channel) {
		changeChannelBi(channel, biSource);
		this.channelMode = channel;
	}
	
	/**
	 * Current channel displayed on the canvas
	 * @return
	 */
	public ImageOperations.ChannelMode getChannelMode() {
		return this.channelMode;
	}
	
	
	
	/**
	 * Returns the {@link BufferedImage} currently displayed on the canvas.
	 * @return
	 */
	public BufferedImage getCanvas() {
		return this.biRendered;
	}
	
	public BufferedImage getSource() {
		return this.biSource;
	}
	
	/**
	 * Overwrite the current source-BufferedImage.
	 * This will also update the displayed canvas and the window-size.
	 * @param bi
	 */
	public void setSourceBI(final BufferedImage bi) {
		this.biRendered = bi;
		this.biSource = bi;
		this.setPreferredSize(new Dimension(bi.getWidth(), bi.getHeight()));
		changeChannelBi(channelMode, bi);
		invalidate();
	}
		
	
	/**
	 * Sets the factor the original-image dimensions are multiplied with
	 * @param zoom
	 */
	public void setZoomFactor(final float zoom) {
		this.zoomFactor = zoom;
		int newW = (int) (biRendered.getWidth() * zoom);
		int newH = (int) (biRendered.getHeight() * zoom);
		this.setPreferredSize(new Dimension(newW, newH));
		this.revalidate();
	}
	
	/**
	 * Returns the factor the original-image dimensions are multiplied with
	 * @return
	 */
	public float getZoomFactor() {
		return this.zoomFactor;
	}
	
	@Override public void paint(Graphics g) {
		
		int width =  biRendered.getWidth(), height =  biRendered.getHeight();
		int newW = (int) (zoomFactor * width); 
		int newH = (int) (zoomFactor * height);
		int offsetX = (int) ((0.5*g.getClipBounds().getWidth()) - (0.5*newW)); // offset im viewport
		int offsetY = (int) ((0.5*g.getClipBounds().getHeight())- (0.5*newH)); // offset im viewport
		int moveX = 0; //offset on bi
		int moveY = 0; //offset on bi
		offsetX=0;
		offsetY=0;
		
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		//g.drawImage(this.displayBi, 0, 0, this);
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);   
        g.drawImage(biRendered, offsetX, offsetY, newW+offsetX, newH+offsetY, moveX, moveY, biRendered.getWidth(), biRendered.getHeight(), null);
  	
	}




	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getPreferredScrollableViewportSize()
	 */
	public Dimension getPreferredScrollableViewportSize() {
		return null;
	}


	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableBlockIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableBlockIncrement(Rectangle arg0, int arg1, int arg2) {
		return 50;
	}


	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportHeight()
	 */
	public boolean getScrollableTracksViewportHeight() {
		return false;
	}


	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableTracksViewportWidth()
	 */
	public boolean getScrollableTracksViewportWidth() {
		return false;
	}

	/**
	 * The dimensions of the stored {@link BufferedImage} multiplied by the zoom-factor.
	 * @return dimension Dimension of the stored {@link BufferedImage}
	 */
	public Dimension getViewDimension() {
		return new Dimension((int) (zoomFactor * biRendered.getWidth()), (int) (zoomFactor * biRendered.getWidth()));
	}

	/* (non-Javadoc)
	 * @see javax.swing.Scrollable#getScrollableUnitIncrement(java.awt.Rectangle, int, int)
	 */
	public int getScrollableUnitIncrement(Rectangle arg0, int arg1, int arg2) {
		return 15; // pixel
	}
}
