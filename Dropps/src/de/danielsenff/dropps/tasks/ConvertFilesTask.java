package de.danielsenff.dropps.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import de.danielsenff.dropps.ConvertController;
import de.danielsenff.dropps.Dropps;
import de.danielsenff.dropps.models.ExportOptions;
import de.danielsenff.dropps.models.IConvertListener;
import de.danielsenff.dropps.models.IProgressListener;
import de.danielsenff.dropps.models.IProgressObserverable;
import de.danielsenff.dropps.models.ProgressStatus;

/**
 * Performs the conversion of several image files.
 * @author danielsenff
 *
 */
public class ConvertFilesTask extends Task<ProgressStatus, Void> {

	private final Collection<File> fileList;
	private ProgressStatus status;
	private ExportOptions options;
	private ConvertController converter;
	
	/**
	 * @param app
	 * @param fileList
	 * @param options
	 */
	public ConvertFilesTask(final Application app, 
			final Collection<File> fileList, 
			final ExportOptions options) {
		super(app, "convertFiles");
		this.fileList = fileList;
		this.options = options;
	}

	@Override protected ProgressStatus doInBackground() {
		
		((Dropps)getApplication()).getMainView().getFrame().setEnabled(false);
		
		if (!fileList.isEmpty()) {
//			final ConvertionController converter = new ConvertionController(fileList,	options, deleteOriginal, overwriteFiles);
			
			
			final IProgressListener listener = new IProgressListener() {

				@Override
				public void update(final ProgressStatus newStatus) {
					message("startMessage", newStatus.getProcessed(), newStatus.getProcessable());
					//initial progress
					setProgress(newStatus.getProcessed(), 0, newStatus.getProcessable());
					// attention, this changes also the message
					status = newStatus;
				}

				@Override
				public void error(final ProgressStatus errorStatus) {
					//localize message
					if (errorStatus.getMessage() != null) {
						
						StringTokenizer tokenizer = new StringTokenizer(errorStatus.getMessage(), "_");
						String bundleKey = tokenizer.nextToken();
						String fileName = null;
						if (tokenizer.hasMoreTokens()) {
							fileName = tokenizer.nextToken();
						}
						errorStatus.setMessage(getResourceMap().getString(bundleKey, fileName));
					}
					status = errorStatus;
				}
			};
			final IConvertListener convListener = new IConvertListener() {

				@Override
				public void convertBegin(File originalFile) {
					setMessage("Conversion of " + originalFile.getName());
				}

				@Override
				public void convertEnd(File originalFile) {
//					setMessage("End Convertion of " + originalFile.getName());
				}
				
			};
			
			final List<IProgressListener> listeners = new ArrayList<IProgressListener>();
			listeners.add(listener);
			
			final List<IConvertListener> convListeners = new ArrayList<IConvertListener>();
			convListeners.add(convListener);
			
			ConvertController converter = new ConvertController(options);
			if (converter instanceof IProgressObserverable) {
				for (IProgressListener iProgressListener : listeners) {
					converter.addListener(iProgressListener);
				}
				
				for (IConvertListener iConvertListener : convListeners) {
					converter.addListener(iConvertListener);
				}
			}
			
//			setMessage("Handle files");
			converter.convertFiles(fileList);
		}
		((Dropps)getApplication()).getMainView().getFrame().setEnabled(true);
		return status;
	}

	/**
	 * @param result
	 */
	@Override
	protected void succeeded(final ProgressStatus result) {
		if (result != null) {
			if (!result.isError()) {
				message("finishedMessage", getExecutionDuration(TimeUnit.SECONDS), getExecutionDuration(TimeUnit.SECONDS) / 60);
			} else {
				showErrorDialog(result.getMessage());
			}
		}
		super.succeeded(result);
	}
	
	@Override
	protected void cancelled() {
		
		super.cancelled();
	}

	private void showErrorDialog(final String message) {
		JOptionPane.showMessageDialog(null, message, "Error", JOptionPane.ERROR_MESSAGE);
	}
	
}
