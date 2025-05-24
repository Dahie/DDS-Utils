package org.dds_utils.dropps;

import org.dds_utils.dropps.io.ImageReader;
import org.dds_utils.dropps.io.ImageWriter;
import org.dds_utils.dropps.io.ImageReaderFactory;
import org.dds_utils.dropps.io.ImageWriterFactory;
import org.dds_utils.dropps.models.*;
import org.ddsutil.MipMapsUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
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

	protected final List<IProgressListener> progressListeners = new ArrayList<IProgressListener>(1);

	public static final String ERROR_ORIGINAL_FILE_NOT_EXISTS = "errorOriginalFileNotExists";

	private final ExportConfiguration options;
	
	/**
	 * @param options
	 */
	public ConvertController(final ExportConfiguration options) {
		this.options = options;
	}

	/**
	 * Process a single file
	 * @param sourceFile
	 */
	public void convertFile(final File sourceFile) {
		try {
			notifyConversionBegin(sourceFile);

			if(sourceFile.isDirectory()) {
				notifyConversionEnd(sourceFile);
				return;
			}

			final File targetFile = getTargetFileFromSourceFile(sourceFile, "dds");
			ImageReader reader = ImageReaderFactory.build(sourceFile);
			ImageWriter writer = ImageWriterFactory.build(targetFile, options);

			if(reader == null) {
				notifyConversionEnd(sourceFile);
				return;
			}

			final ConversionResult result = convert(reader, writer);

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

	private ConversionResult convert(ImageReader reader, ImageWriter writer) throws IOException {
		BufferedImage imageToConvert = reader.read();

		if(imageToConvert == null) {
			return new ConversionResultBuilder().error("Image not loadable").buildConversionResult();
		}

		if (!MipMapsUtil.isPowerOfTwo(imageToConvert.getWidth())
				&& !MipMapsUtil.isPowerOfTwo(imageToConvert.getHeight())
				&& options.hasGeneratedMipMaps()) {
			return new ConversionResultBuilder().error("Mipmaps generation failed").buildConversionResult(); // todo split to different messages
		}

		return writer.write(imageToConvert);
	}

	private File getTargetFileFromSourceFile(File sourceFile, String targetExtension) {
		final String targetPath = sourceFile.getAbsolutePath().substring(0, sourceFile.getAbsolutePath().lastIndexOf('.'));
		return new File(targetPath + "." + targetExtension);
	}


	protected void updateProgressListeners(final ProgressStatus status) {
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.update(status);
		}
	}

	protected void notifyConversionBegin(final File file) {
		ProgressStatus status = new ProgressStatus(file, 0, 0, "Started");
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.begin(status);
		}
	}

	protected void notifyConversionEnd(final File file) {
		ProgressStatus status = new ProgressStatus(file, 0, 0, "Ended");
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.end(status);
		}
	}

	protected void notifyError(final ProgressStatus status) {
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.error(status);
		}
	}

	@Override
	public void addListener(final IProgressListener listener) {
		progressListeners.add(listener);
	}

	@Override
	public void removeListener(final IProgressListener listener) {
		progressListeners.remove(listener);
	}

	public ExportConfiguration getOptions() {
		return this.options;
	}
}
