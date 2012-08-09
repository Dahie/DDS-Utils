package de.danielsenff.madds.util;

public class ByteConverter {

	public static float bit2KibiByte(long vale) {
		return (vale / 8) / 1024;
	}
	
	public static float bit2MibiByte(long vale) {
		return (vale / 8) / (1024*1024);
	}
}
