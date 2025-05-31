package org.dds_utils.dropps;

import org.io.ImageReader;
import org.io.ImageWriter;
import org.io.ImageReaderFactory;
import org.io.ImageWriterFactory;
import org.dds_utils.dropps.models.*;
import org.ddsutil.MipMapsUtil;
import org.model.ExportConfiguration;
import org.model.TextureImage;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
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
	 * @param conversionExecution
	 */
	public void convertFile(final ConversionExecution conversionExecution) {
		File sourceFile = conversionExecution.getSourceFile();
		try {
			notifyConversionBegin(sourceFile);

			if(sourceFile.isDirectory()) {
				notifyConversionEnd(sourceFile);
				return;
			}

			ImageReader reader = ImageReaderFactory.build(sourceFile);
			ImageWriter writer = ImageWriterFactory.build(sourceFile, options);

			if(reader == null) {
				notifyConversionEnd(sourceFile);
				return;
			}

			final ConversionResult result = convert(sourceFile, reader, writer);

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

	private ConversionResult convert(File sourceFile, ImageReader reader, ImageWriter writer) throws IOException {
		TextureImage textureImage = reader.read();

		System.out.println("read");
		//notifyImageLoaded(sourceFile, textureImage);

		if(textureImage == null) {
			return newImageNotLoadableException("Image not loadable");
		}

		if (options.hasGeneratedMipMaps() && !MipMapsUtil.isPowerOfTwo(textureImage.getWidth())
				&& !MipMapsUtil.isPowerOfTwo(textureImage.getHeight())) {
			return newImageNotLoadableException("Mipmaps generation failed, width or height not power of two");
		}

		System.out.println("write");
		System.out.println(writer);
		writer.write(textureImage);
		System.out.println("written" + writer.getTargetFile());
		return newConversionResult(writer.getTargetFile(), textureImage);
	}

	private static ConversionResult newImageNotLoadableException(String message) {
		return new ConversionResultBuilder().error(message).buildConversionResult();
	}


	private ConversionResult newConversionResult(File targetFile, TextureImage textureImage) {
		return new ConversionResultBuilder().textureImage(textureImage).targetFile(targetFile).buildConversionResult();
	}


	private void notifyImageLoaded(File sourceFile, TextureImage imageToConvert) {
		ProgressStatus status = new ProgressStatus(sourceFile, imageToConvert, 0, 0, "Loaded");
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.imageLoaded(status);
		}
	}


	protected void updateProgressListeners(final ProgressStatus status) {
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.update(status);
		}
	}

	protected void notifyConversionBegin(final File file) {
		ProgressStatus status = new ProgressStatus(file, null, 0, 0, "Started");
		for (final IProgressListener iProgressListener : progressListeners) {
			iProgressListener.begin(status);
		}
	}

	protected void notifyConversionEnd(final File file) {
		ProgressStatus status = new ProgressStatus(file, null, 0, 0, "Ended");
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
