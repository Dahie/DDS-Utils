package de.danielsenff.dropps;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.jdesktop.application.Application;

import util.FileUtil;
import util.ImageIOUtils;
import ddsutil.DDSUtil;
import ddsutil.MipMapsUtil;
import de.danielsenff.dropps.models.ExportOptions;
import de.danielsenff.dropps.models.IConvertListener;
import de.danielsenff.dropps.models.IProgressListener;
import de.danielsenff.dropps.models.IProgressObserverable;
import de.danielsenff.dropps.models.ProgressStatus;
import de.danielsenff.dropps.models.TextureImageFormatLoaderTGA;

/**
 * Controller for the conversion process. This actually does the work.
 * @author dahie
 *
 */
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

	private final ExportOptions options;
	
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
		for (final File file : files) {
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
	private void convertFile(final File file,
			final int i, 
			final int filesCount) {
		System.out.println("Process: "+file);

		notifyConversionBegin(file);
		notifyProgressListeners(new ProgressStatus(i, filesCount, "progress"));

		BufferedImage imageToConvert = null;
		final DefaultListModel dlm = (DefaultListModel) ((DroppsView)((Dropps)Application.getInstance()).getMainView()).getDropPanel().getModel();
		try {
			System.out.println("Read BufferedImage ...");
			
			//check if is folder
			if(file.isDirectory()) {
				dlm.removeElement(file);
				notifyError(new ProgressStatus(i, filesCount, "unsupportedFolder_"+file.getName(), true));
				return;
			}
			
			// check supported fileformats
			if(!isFiletypeSupported(file)) {
				dlm.removeElement(file);
				notifyError(new ProgressStatus(i, filesCount, "unsupportedFileformat_"+file.getName(), true));
				return;
			}
			
			InputStream fis = new FileInputStream(file);
			if(ImageIOUtils.isImageIOSupported(file)) {
				imageToConvert = ImageIO.read(fis);
				fis.close();
				System.out.println(imageToConvert.getType());
			} else if (FileUtil.getFileSuffix(file).contains("tex")) {
				imageToConvert = DDSUtil.decompressTexture(file);
			} else if (FileUtil.getFileSuffix(file).contains("tga")) {
				try {
					final TextureImageFormatLoaderTGA loader = new TextureImageFormatLoaderTGA();

					final BufferedInputStream in = new BufferedInputStream(fis);
					imageToConvert = loader.loadTextureImage(in , true, false);
					fis.close();
				} catch (final Exception ex) {
					ex.printStackTrace();
					JOptionPane.showMessageDialog(Dropps.getApplication().getMainFrame(), 
							"<html>Error: The TGA-file could not be loaded. Only 32bit TGA are supported." +
							"<br>The operation is aborted. </html>",	"Error", 
							JOptionPane.ERROR_MESSAGE);
					fis.close();
				}
			}
			
			if(imageToConvert != null) {
				
				final String convertedFile = file.getAbsolutePath().substring(0,file.getAbsolutePath().lastIndexOf('.'));
				final File newFile = new File(convertedFile+".dds");
				
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
			}
		} catch (final IOException e) {
			e.printStackTrace();
			//			showErrorDialog(e.getMessage());
		} catch (final OutOfMemoryError e) {
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
	
	private boolean isFiletypeSupported(final File file) {
		if(ImageIOUtils.isImageIOSupported(file)
				|| FileUtil.getFileSuffix(file).contains("tex")
				|| FileUtil.getFileSuffix(file).contains("tga")
				)
			return true;
		
		return false;
	}

	protected void notifyProgressListeners(final ProgressStatus status) {
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.update(status);
		}
	}

	protected void notifyConversionBegin(final File originalFile) {
		for (final IConvertListener iConvertListener : convertListeners) {
			iConvertListener.convertBegin(originalFile);
		}
	}

	protected void notifyConversionEnd(final File originalFile) {
		for (final IConvertListener iConvertListener : convertListeners) {
			iConvertListener.convertEnd(originalFile);
		}
	}

	protected void notifyError(final ProgressStatus status) {
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.error(status);
		}
	}


	protected boolean checkConvertFileExists(final File originalFile) {
		if (!originalFile.exists()) {
			notifyError(new ProgressStatus(0, 0, ERROR_ORIGINAL_FILE_NOT_EXISTS, true));
			logger.log(Level.SEVERE, ERROR_ORIGINAL_FILE_NOT_EXISTS);
			return false;
		}
		return true;
	}

	public void addListener(final IConvertListener listener) {
		convertListeners.add(listener);
	}

	public void removeListener(final IConvertListener listener) {
		convertListeners.remove(listener);
	}

	@Override
	public void addListener(final IProgressListener listener) {
		progressListeners.add(listener);
	}

	@Override
	public void removeListener(final IProgressListener listener) {
		progressListeners.remove(listener);
	}
}
