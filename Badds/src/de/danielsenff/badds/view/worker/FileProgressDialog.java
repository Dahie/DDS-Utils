/**
 * 
 */
package de.danielsenff.badds.view.worker;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JPanel;

import util.Stopwatch;

import de.danielsenff.badds.view.View;
import de.danielsenff.badds.view.GridBagConstraints.LabelConstraints;
import de.danielsenff.badds.view.GridBagConstraints.RemainderConstraintsNoFill;



/**
 * @author danielsenff
 *
 */
public class FileProgressDialog extends ProgressDialog {

	protected String status;
	protected String filename;
	private JLabel lblStatus;
	private JLabel lblFilename;
	
	private Stopwatch stopwatch;
	private JLabel lblRemainingTime;
	private JLabel lblElapsedTime;
	private int maxFileCount;
	private int currentFileCount;
	private JLabel lblFileCount;
	Image previewImage;
	private JPanel preview;
	
	public FileProgressDialog(View view, int maxValue) {
		this(view, maxValue, "", "");
	}
	
	public FileProgressDialog(View view, int maxValue, String status, String filename) {
		super(view, maxValue);
		setSize(new Dimension(300, 100));
		
		this.status = status;
		this.filename = filename;
		this.stopwatch = new Stopwatch();
		
		JPanel infoPanel = new JPanel();
		setLayout(new BorderLayout());
		infoPanel.setLayout(new GridBagLayout());
		
		preview = new JPanel() {
			
			@Override
			public void paint(Graphics g) {
				super.paint(g);
				/*g.setColor(Color.red);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());*/
//				g.drawImage(previewImage, 0, 0, 0, 0, previewImage.getWidth(null), previewImage.getWidth(null), 0, 0, null);
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, this.getWidth(), this.getHeight());
				if(previewImage != null) {
					g.drawImage(previewImage, 0, 0, previewImage.getWidth(null), previewImage.getHeight(null), 0, 0, previewImage.getWidth(null), previewImage.getHeight(null), null);
				} else 
					System.out.println("no image");
			}
			
		};
		preview.setPreferredSize(new Dimension(150,150));
//		add(preview, BorderLayout.LINE_START);
		
		// line for file information
		maxFileCount = maxValue;
		currentFileCount = 0;
		
		lblFileCount = new JLabel(currentFileCount + " / "+ maxFileCount);
		infoPanel.add(lblFileCount, new LabelConstraints());
		infoPanel.add(new JLabel("File:"), new LabelConstraints());
		lblFilename = new JLabel(filename);
		infoPanel.add(lblFilename, new RemainderConstraintsNoFill());
		lblStatus = new JLabel(status);
		infoPanel.add(lblStatus, new RemainderConstraintsNoFill());
		
		this.add(infoPanel, BorderLayout.CENTER);
		
		JPanel timePanel = new JPanel();
		timePanel.setLayout(new GridBagLayout());
		timePanel.add(new JLabel("Elapsed time:"), new LabelConstraints());
		lblRemainingTime = new JLabel();
		lblElapsedTime = new JLabel();
		timePanel.add(lblElapsedTime , new RemainderConstraintsNoFill());
		timePanel.add(new JLabel("Remaining time:"), new LabelConstraints());
		timePanel.add(lblRemainingTime, new RemainderConstraintsNoFill());
//		timePanel.add(new JButton(new ActionCancelSaveAll(null)), new RemainderConstraintsNoFill());
		
		
		
		
//		this.add(timePanel, BorderLayout.PAGE_END);
		
		this.add(progressbar, BorderLayout.PAGE_END);
		
		
//		this.pack();
		setResizable(false);
		setVisible(true);
		stopwatch.start();
	}

	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.lblStatus.setText(status);
		this.lblStatus.invalidate();
	}

	/**
	 * @return
	 */
	public String getFilename() {
		return this.filename;
	}

	/**
	 * @param filename
	 */
	public void setFilename(final String filename) {
		this.lblFilename.setText(filename);
		this.currentFileCount++;
		this.lblFileCount.setText(this.currentFileCount + " / " + this.maxFileCount);
		this.lblFilename.invalidate();
	}
	
	public static void main(final String[] args) {
		new FileProgressDialog(null, 10);
	}

	/**
	 * @param scaledInstance
	 */
	public synchronized void setPreview(final Image scaledInstance) {
		this.previewImage = scaledInstance;
		this.invalidate();
	}
	
}
