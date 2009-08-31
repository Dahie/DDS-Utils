package de.danielsenff.dropps.tasks;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;

import org.jdesktop.application.Application;
import org.jdesktop.application.Task;

import de.danielsenff.dropps.Converter;
import de.danielsenff.dropps.Dropps;
import de.danielsenff.dropps.DroppsView;
import de.danielsenff.dropps.models.ExportOptions;
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
	private Converter converter;
	
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

				public void update(final ProgressStatus newStatus) {
					message("startMessage", newStatus.getProcessed(), newStatus.getProcessable());
					setProgress(newStatus.getProcessed(), 0, newStatus.getProcessable());
					status = newStatus;
				}

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
			final List<IProgressListener> listeners = new ArrayList<IProgressListener>();
			listeners.add(listener);
//			converter.convert(listener);
			
			converter = new Converter(options);
			if (converter instanceof IProgressObserverable) {
				for (IProgressListener iProgressListener : listeners) {
					converter.addListener(iProgressListener);
				}
			}
			message("message", "Handle files");
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
