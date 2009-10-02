/**
 * 
 */
package de.danielsenff.badds.view.worker;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Vector;

import javax.swing.JOptionPane;

import de.danielsenff.badds.model.ExportOptions;
import de.danielsenff.badds.operations.ChannelBrightness;
import de.danielsenff.badds.operations.Operation;
import de.danielsenff.badds.operations.ScaleOperation;
import de.danielsenff.badds.util.FileHelper;
import de.danielsenff.badds.view.View;
import de.danielsenff.badds.view.GUI.PreviewFrame;

import DDSUtil.DDSUtil;
import DDSUtil.NonCubicDimensionException;
import JOGL.DDSImage;
import Model.DDSFile;
import Model.DDSImageFile;

/**
 * @author danielsenff
 *
 */
public class SaveOperationWorker extends OperationWorker {

	int newWidth;
	int newHeight;
	int pixelformat;
	boolean hasGeneratedMipMaps;
	private Vector<DDSFile> files;
	private boolean makeBackup;
	private Collection<Operation> operations;
	private boolean keepOriginal;

	/**
	 * @param view
	 * @param openFiles
	 * @param defaultListModel 
	 * @param exportOptions 
	 * @param operations 
	 */
	public SaveOperationWorker(View view, 
			Vector<DDSFile> openFiles, 
			ExportOptions exportOptions, 
			final Collection<Operation> operations) {
		super(view, openFiles.size());
		
		this.files = openFiles; 
		this.operations = operations;
		initExportValues(exportOptions);
	}


	/**
	 * @param exportOptions 
	 * 
	 */
	private void initExportValues(ExportOptions exportOptions) {
		this.newWidth = exportOptions.getNewWidth();
		this.newHeight = exportOptions.getNewHeight();
		this.pixelformat = exportOptions.getNewPixelformat();
		this.hasGeneratedMipMaps = exportOptions.hasGeneratedMipMaps();
		this.makeBackup = exportOptions.isMakeBackup();
		this.keepOriginal = exportOptions.isKeepOriginal();
		
//		operations.add(new ScaleOperation(newWidth, newHeight));
		if(pixelformat == DDSImage.D3DFMT_DXT1 && exportOptions.isPaintWhiteAlpha()) {
			operations.add(new ChannelBrightness(3, 1.0f));
		}
		
		System.out.println("Width: " + newWidth + " Height: " + newHeight);
		System.out.println("Pixelformat: " + DDSFile.verbosePixelformat(pixelformat) + " with MipMaps " + hasGeneratedMipMaps);
		
	}


	@Override
	@SuppressWarnings("finally")
	public Object construct() {
		int i = 0;;
		for (DDSFile file : files) {
			try {
				workThroughFile(file, pixelformat, hasGeneratedMipMaps);

				files.setElementAt(new DDSFile(file.getAbsolutePath()), i);

				view.getOpenFilesTable().invalidate();
				setProgressValue(i+1);	
			} catch (OutOfMemoryError ex) {
				ex.printStackTrace();
				JOptionPane.showMessageDialog(view, 
						"<html>Error: Out of memory" +
						"<br>The operation is aborted. </html>",	"Error", 
						JOptionPane.ERROR_MESSAGE);
				
				break;
			}
			i++;
		}
		view.validate();
//		startNotification();
		return "All done";
	}	
	

	private void workThroughFile(DDSFile sourceDDS, 
			int pixelformat,
			boolean hasGeneratedMipMaps) 
	throws OutOfMemoryError {

		((FileProgressDialog)dialog).setFilename(sourceDDS.getFile().getName());
		DDSImageFile imagefile;

		try {
			if(hasGeneratedMipMaps 
					&& !DDSImageFile.isPowerOfTwo(sourceDDS.getWidth()) 
					&& !DDSImageFile.isPowerOfTwo(sourceDDS.getHeight())) 
				throw new NonCubicDimensionException();

			imagefile = new DDSImageFile(sourceDDS.getFile());
			BufferedImage bufferedImage = imagefile.getData();
			
			
			((FileProgressDialog)dialog).setPreview(
					bufferedImage.getScaledInstance(150, 150, Image.SCALE_AREA_AVERAGING));
//			new PreviewFrame(null,imagefile.getFile().getName(),  bufferedImage).setVisible(true);

			File targetFile = sourceDDS.getFile();
			
			// create backups
			if (makeBackup) {
				((FileProgressDialog)dialog).setStatus("Making backup ...");
				FileHelper.createBackups(sourceDDS.getFile(), File.separator + "Backup");
				
				if(keepOriginal) {
					// keep original and create file in subfolder
					File workdir = new File(sourceDDS.getFile().getAbsoluteFile().getParent());
					File subDir = new File(workdir.getAbsolutePath()+ File.separator + "Scaled_DDS");
					subDir.mkdir();
					targetFile = new File(subDir.getAbsolutePath() + File.separator + sourceDDS.getFile().getName());
				}
			}
			
			if (pixelformat == 0) {		// keep original format
				pixelformat = imagefile.getPixelformat();
			}
			
			
			((FileProgressDialog)dialog).setStatus("Pixel operations ...");
//			long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
//			System.out.println(mem0);
			
			BufferedImage bidata = runOperations(bufferedImage, operations);
//			BufferedImage bidata = bufferedImage;

			((FileProgressDialog)dialog).setStatus("Compressing and saving ...");
			new PreviewFrame(null,imagefile.getFile().getName() + " scaled",  bidata).setVisible(true);
			DDSUtil.write(targetFile, bidata, pixelformat, hasGeneratedMipMaps);

		} catch (IOException ey) {
			ey.printStackTrace();
			JOptionPane.showMessageDialog(view, 
					"<html>Error: " + ey.getMessage() +
					"<br>"+sourceDDS.getFile().getName()+" will be skipped.</html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
		} catch (IllegalArgumentException ex) {
			JOptionPane.showMessageDialog(view, 
					"<html>Error: " + ex.getMessage() +
					"<br>"+sourceDDS.getFile().getName()+" will be skipped.</html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private BufferedImage runOperations(BufferedImage srcbi, Collection<Operation> operations) {
		if(!operations.isEmpty()) {
			BufferedImage newbi = null;
			for (Operation op : operations) {
				System.out.println(op);
				newbi = op.run(srcbi);
				srcbi = newbi;
			}

			return newbi;
		}
		return srcbi;
	}
	/*
	private void startNotification() {
		Notification.SetFactory("glguerin.notification.imp.mac.ten.TenNotification");
		Notification note = Notification.MakeOne();
		note.setMarker(true);
		note.setIcon(0);
		note.setSound(0);
		note.setAlert(null);
		note.post();
	}*/
	
}
