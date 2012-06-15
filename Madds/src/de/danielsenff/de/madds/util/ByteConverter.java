package de.danielsenff.de.madds.util;

public class ByteConverter {

	public static float bit2KibiByte(int vale) {
		return (vale / 8) / 1024;
	}
	
	public static float bit2MibiByte(int vale) {
		return (vale / 8) / (1024*1024);
	}
}
