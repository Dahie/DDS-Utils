package de.danielsenff.madds.models;

import java.io.File;
import java.util.HashMap;

public class TextureHashMap extends HashMap<File, TextureFile>{

	private static TextureHashMap tm = new TextureHashMap();
	
	private TextureHashMap() {
		super();
	}
	
	public static TextureHashMap getTextureHashMap() {
		return tm;
	}
	
}
