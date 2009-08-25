/**
 * 
 */
package de.danielsenff.badds.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * @author danielsenff
 *
 */
public class FileHelper {

	private FileHelper() {}
	
	/**
	 * Creates a new directory if necessary, creates a duplicate of the original file and 
	 * copies all contents into the new file.
	 * @param file
	 * @param targetDirectory
	 */
	public static void createBackups(final File file, final String targetDirectory) {
		
		//get working directory
		File workdir = new File(file.getAbsoluteFile().getParent());
		
		// create backupfolder
		File backupDir = new File(workdir.getAbsolutePath()+targetDirectory);
		backupDir.mkdir();
		
		String backupFileName = backupDir.getAbsoluteFile()+File.separator+file.getName();
		File backupFile = new File(backupFileName);
		try {
			backupFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// copy contents
		try {
			copyFiles(file, backupFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	
	}
	
	
	/**
	 * Copies the contents of the input file to the output file
	 * @param fromFile
	 * @param toFile
	 * @throws IOException
	 */
	public static void copyFiles(final File fromFile, final File toFile) throws IOException {
		    FileInputStream inFile = new FileInputStream(fromFile);
		    FileOutputStream outFile = new FileOutputStream(toFile);
		    FileChannel inChannel = inFile.getChannel();
		    FileChannel outChannel = outFile.getChannel();
		    int bytesWritten = 0;
		    long byteCount = inChannel.size();
		    while (bytesWritten < byteCount) {
		      bytesWritten += inChannel.transferTo(bytesWritten, byteCount - bytesWritten, outChannel);
		    }
		    inFile.close();
		    outFile.close();
	}
	
}
