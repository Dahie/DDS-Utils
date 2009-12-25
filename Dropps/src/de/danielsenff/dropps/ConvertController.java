package de.danielsenff.dropps;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import DDSUtil.DDSUtil;
import DDSUtil.MipMapsUtil;
import de.danielsenff.dropps.models.ExportOptions;
import de.danielsenff.dropps.models.IConvertListener;
import de.danielsenff.dropps.models.IProgressListener;
import de.danielsenff.dropps.models.IProgressObserverable;
import de.danielsenff.dropps.models.ProgressStatus;

public class ConvertController implements IProgressObserverable {

	private static final Logger logger = Logger.getLogger(ConvertController.class.getName());

	protected final List<IConvertListener> convertListeners = new ArrayList<IConvertListener>(1);
	protected final List<IProgressListener> progressListeners = new ArrayList<IProgressListener>(1);

	//constants for messages regarding the conversion process
    public static final String ERROR_FILE_IO = "errorFileIOMessage";
    public static final String ERROR_THREAD_INTERUPTION = "errorGeneralMessage";
    public static final String ERROR_ORIGINAL_FILE_PATH_NULL = "errorOriginalFilePathNull";
    public static final String ERROR_ORIGINAL_FILE_NOT_EXISTS = "errorOriginalFileNotExists";
    public static final String ERROR_CANT_CREATE_CONVERTED_FILE = "errorCantCreateConvertedFile";

	private ExportOptions options;
	
	/**
	 * @param options
	 */
	public ConvertController(final ExportOptions options) {
		this.options = options;
	}
	
	/**
	 * Convert a bunch of files
	 * @param files
	 * @param options
	 */
	public void convertFiles(final Collection<File> files) {
		int i = 0;
		for (File file : files) {
			convertFile(file, i, files.size());
			i++;
		}
	}



	/**
	 * Process a single file
	 * @param file
	 * @param options
	 * @param i
	 * @param filesCount
	 */
	private void convertFile(File file,
			int i, 
			int filesCount) {
		System.out.println("Process: "+file);

		notifyConversionBegin(file);
		notifyProgressListeners(new ProgressStatus(i, filesCount, "progress"));

		BufferedImage imageToConvert;
		DefaultListModel dlm = (DefaultListModel) ((DroppsView)((Dropps)Dropps.getInstance()).getMainView()).getDropPanel().getModel();
		try {
			System.out.println("Read BufferedImage ...");
			
			// check supported fileformats
			if(!isFiletypeSupported(file)) {
				dlm.removeElement(file);
				notifyError(new ProgressStatus(i, filesCount, "unsupportedFileformat_"+file.getName(), true));
				return;
			}
			
			imageToConvert = ImageIO.read(file);
			String convertedFile = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf('.'));
			File newFile = new File(convertedFile+".dds");

			// check image dimensions
			
			if (!MipMapsUtil.isPowerOfTwo(imageToConvert.getWidth())
				&& !MipMapsUtil.isPowerOfTwo(imageToConvert.getHeight())
				&& options.hasGeneratedMipMaps()) {
				dlm.removeElement(file);
				notifyError(new ProgressStatus(i, filesCount, "notPowerOf2_"+file.getName(), true));
				return;
			}
			
			System.out.println("Begin conversion ...");
			// exclude this into an Converter with appropriate listeners
			DDSUtil.write(newFile, imageToConvert, options.getNewPixelformat(), options.hasGeneratedMipMaps());
		} catch (IOException e) {
			e.printStackTrace();
			//			showErrorDialog(e.getMessage());
		} catch (OutOfMemoryError e) {
			e.printStackTrace();
			final long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			JOptionPane.showMessageDialog(null, 
					"<html>Error: Out of memory: " + mem0 +
					"<br>The operation is aborted. </html>",	"Error", 
					JOptionPane.ERROR_MESSAGE);
			
		}
		dlm.removeElement(file);
		
		notifyConversionEnd(file);
	}
	
	
	
	private boolean isFiletypeSupported(File file) {
		String[] supportedMIMETypes = ImageIO.getReaderFormatNames();
		for (int j = 0; j < supportedMIMETypes.length; j++) {
			if(file.getName().toLowerCase().contains(supportedMIMETypes[j]))
				return true;
		}
		return false;
	}

	protected void notifyProgressListeners(ProgressStatus status) {
		for (IProgressListener iProgressListener : progressListeners) {
			iProgressListener.update(status);
		}
	}

	protected void notifyConversionBegin(File originalFile) {
		for (IConvertListener iConvertListener : convertListeners) {
			iConvertListener.convertBegin(originalFile);
		}
	}

	protected void notifyConversionEnd(File originalFile) {
		for (IConvertListener iConvertListener : convertListeners) {
			iConvertListener.convertEnd(originalFile);
		}
	}

	protected void notifyError(ProgressStatus status) {
		for (IProgressListener iProgressListener : progressListeners) {
			iProgressListener.error(status);
		}
	}


	protected boolean checkConvertFileExists(File originalFile) {
		if (!originalFile.exists()) {
			notifyError(new ProgressStatus(0, 0, ERROR_ORIGINAL_FILE_NOT_EXISTS, true));
			logger.log(Level.SEVERE, ERROR_ORIGINAL_FILE_NOT_EXISTS);
			return false;
		}
		return true;
	}

	public void addListener(IConvertListener listener) {
		convertListeners.add(listener);
	}

	public void removeListener(IConvertListener listener) {
		convertListeners.remove(listener);
	}

	public void addListener(IProgressListener listener) {
		progressListeners.add(listener);
	}

	public void removeListener(IProgressListener listener) {
		progressListeners.remove(listener);
	}
}
