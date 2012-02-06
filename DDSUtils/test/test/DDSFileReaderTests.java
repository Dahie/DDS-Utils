package test;

import jogl.DDSImage;

import org.junit.Test;

public class DDSFileReaderTests extends DDSTestCase{

	
	@Test
	public void openrrfmIconDDS() {
		DDSImage image = loadDDSImage(this.textureDDSrfmIcon);
		image.debugPrint();
		System.out.println(image.getPixelFormat());
	}
	
	@Test
	public void openrrfmSMIconDDS() {
		DDSImage image = loadDDSImage(this.textureDDSrfmSMIcon);
		image.debugPrint();
		System.out.println(image.getPixelFormat());
	}
}
