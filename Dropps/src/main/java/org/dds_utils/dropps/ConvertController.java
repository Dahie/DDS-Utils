package org.dds_utils.dropps;

import org.dds_utils.dropps.models.*;
import org.ddsutil.MipMapsUtil;
import org.util.FileUtil;
import org.util.ImageIOUtils;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller for the conversion process. This actually does the work.
 * @author dahie
 *
 */
public class ConvertController implements IProgressObserverable {

	private static final Logger logger = Logger.getLogger(ConvertController.class.getName());

	protected final List<IConvertListener> convertListeners = new ArrayList<IConvertListener>(1);
	protected final List<IProgressListener> progressListeners = new ArrayList<IProgressListener>(1);

	public static final String ERROR_ORIGINAL_FILE_NOT_EXISTS = "errorOriginalFileNotExists";

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
	 */
	public void convertFiles(final Collection<File> files) {
		int i = 0;
		for (final File sourceFile : files) {
			convertFile(sourceFile);
			i++;
		}
	}

	/**
	 * Process a single file
	 * @param sourceFile
	 */
	public void convertFile(final File sourceFile) {
		try {
			notifyConversionBegin(sourceFile);

			if(sourceFile.isDirectory() || !isFiletypeSupported(sourceFile)) {
				notifyConversionEnd(sourceFile);
				return;
			}

			// if input is DDS

			final File targetFile = getTargetFileFromSourceFile(sourceFile, "dds");

			final ConversionResult result = new DDSConverter(sourceFile, targetFile, options).perform();

		} catch (final IOException e) {
			e.printStackTrace();
		} catch (final OutOfMemoryError e) {
			e.printStackTrace();
			final long mem0 = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
			JOptionPane.showMessageDialog(null,
					"<html>Error: Out of memory: " + mem0 +
					"<br>The operation is aborted. </html>",	"Error",
					JOptionPane.ERROR_MESSAGE);
		}

		notifyConversionEnd(sourceFile);
	}

	private File getTargetFileFromSourceFile(File sourceFile, String targetExtension) {
		final String targetPath = sourceFile.getAbsolutePath().substring(0, sourceFile.getAbsolutePath().lastIndexOf('.'));
		return new File(targetPath + "." + targetExtension);
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

	protected boolean checkConvertFileExists(final File originalFile) { //unused
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

	public ExportOptions getOptions() {
		return this.options;
	}
}
