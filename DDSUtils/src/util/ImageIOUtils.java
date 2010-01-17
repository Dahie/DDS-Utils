package util;

import java.io.File;

import javax.imageio.ImageIO;

public class ImageIOUtils {

	public static boolean isImageIOSupported(File file) {
		String[] supportedMIMETypes = ImageIO.getReaderFormatNames();
		for (int j = 0; j < supportedMIMETypes.length; j++) {
			if(FileUtil.getFileSuffix(file).contains(supportedMIMETypes[j]))
				return true;
		}
		return false;
	}
	
}
