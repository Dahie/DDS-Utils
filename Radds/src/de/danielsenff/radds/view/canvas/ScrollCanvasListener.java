/**
 * 
 */
package de.danielsenff.radds.view.canvas;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

import javax.swing.JComboBox;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JViewport;


/**
 * @author danielsenff
 *
 */
public class ScrollCanvasListener implements MouseMotionListener, MouseWheelListener {

	
	private JScrollPane scrollViewPane;
	private JComboBox zoomCombo;
	private JSlider zoomSlider;
	
	/**
	 * @param pane 
	 * @param zoomCombo 
	 * 
	 */
	public ScrollCanvasListener(JScrollPane pane, JComboBox zoomCombo) {
		this(pane, zoomCombo, null);
	}
	
	public ScrollCanvasListener(JScrollPane pane, JComboBox zoomCombo, JSlider zoomSlider) {
		this.scrollViewPane = pane;
		this.zoomCombo = zoomCombo;
		this.zoomSlider = zoomSlider;
	}
	
	public ScrollCanvasListener(JScrollPane pane, JSlider zoomSlider) {
		this(pane, null, zoomSlider);
	}

	int xOld = 0;
	int yOld = 0;
	
	public void mouseDragged(MouseEvent event) {
		drag(event);
	}

	/**
	 * 
	 */
	private void drag(MouseEvent event) {
		
		JViewport viewport = scrollViewPane.getViewport();
		Point position = viewport.getViewPosition();
		Rectangle rectViewport = viewport.getViewRect();
		
	
		// get new click coordinates
		int xNew = event.getX();
		int yNew = event.getY();
		
		// translation aka difference
		int deltaX = xOld - xNew;
		int deltaY = yOld - yNew;
		
		int x = position.x + deltaX;
		int y = position.y + deltaY;

		// boundaries
		int canvasWidth = (int) viewport.getViewSize().getWidth();
		int canvasHeight = (int) viewport.getViewSize().getHeight();
		
		if(rectViewport.width < canvasWidth) 
		{
			// left and top edge
			if (x <= 0) x = 0;
			
			// right edge
			int maxX = canvasWidth - rectViewport.width;
			if (x > maxX)	x = maxX;
			position.x = x;
		}
		if(rectViewport.height < canvasHeight) {
			if (y <= 0) y = 0;
			int maxY = canvasHeight - rectViewport.height;
			// bottom edge
			if (y > maxY)	y = maxY;
			position.y = y;
		}
		
		viewport.setViewPosition(position);
	}

	/* (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	public void mouseMoved(MouseEvent event) {
		xOld = event.getX();
		yOld = event.getY();
	}

	
	
	/**
	 * Maximum factor for zooming
	 */
	public static final float UPPER_ZOOM_LIMIT = 20.0f;

	/**
	 * Minimum factor for zooming
	 */
	public static final float LOWER_ZOOM_LIMIT = 0.1f;
	
	/* (non-Javadoc)
	 * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
	 */
	public void mouseWheelMoved(MouseWheelEvent wheelEvent) {
		BICanvas canvas = ((BICanvas) wheelEvent.getSource());
		
		
		
		float originalZoomFactor = canvas.getZoomFactor();
		float zoomFactor = 1;
		
		// zoom direction
		if(wheelEvent.getWheelRotation() < 0) {
			// increase zoom
			zoomFactor = originalZoomFactor + 0.05f;
			
			if (originalZoomFactor > UPPER_ZOOM_LIMIT) {
				zoomFactor = UPPER_ZOOM_LIMIT;
			}
			canvas.setZoomFactor(zoomFactor);
		} else if(wheelEvent.getWheelRotation() > 0 ) {
			// decrease zoom
			zoomFactor = originalZoomFactor - 0.05f;
			if(zoomFactor < LOWER_ZOOM_LIMIT) {
				zoomFactor = LOWER_ZOOM_LIMIT;
			}
			canvas.setZoomFactor(zoomFactor);
		}
		int f = (int) (zoomFactor*100);
		if(zoomCombo != null) 
			zoomCombo.setSelectedItem(f+"");
		if(zoomSlider != null)
			zoomSlider.setValue(f);
		
		// mouse position offset
		
		JViewport viewport = scrollViewPane.getViewport();
		Point position = viewport.getViewPosition();
		Rectangle viewRect = viewport.getViewRect();
		Dimension canvasDimension = canvas.getViewDimension();
		
		
		// get new scroll coordinates
		int xNew = wheelEvent.getX();
		int yNew = wheelEvent.getY();
		
		// translation aka centerin
		int deltaX = position.x - xNew;
		int deltaY = position.y - yNew;
		
		int xView = (int) ((canvasDimension.getWidth() - viewRect.width) * 0.5);
		int yView = (int) ((canvasDimension.getHeight() - viewRect.height) * 0.5);
//		int xView = deltaX
		
		// write coordinates back
		position.x = xView;
		position.y = yView;
		
		viewport.setViewPosition(position);
	}

}
