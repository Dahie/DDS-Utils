package de.danielsenff.de.madds.models;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import jogl.DDSImage;

public class TextureFile implements SizableNode{

	private File file;
	private DDSImage image;
	private Dimension dimension;
	private int bitrate;
	private int sizeInMemory;
	private Material material;
	
	public TextureFile(File file) {
		this.file = file;
	}
	
	private void setup() throws IOException {
		this.image = DDSImage.read(this.file);
		this.dimension = new Dimension(image.getWidth(), image.getHeight());
		
		this.bitrate = image.getDepth();
		
		if(containsFileName("_s.")) {
			// specular map
			this.material = Material.Specular;
		} else if(containsFileName("_b.")) {
			// normal map
			this.material = Material.Normal;
		} else if(containsFileName("_r.")) {
			// diffuse map
			this.material = Material.Diffuse;
		} else {
			this.material = Material.Other;
		}
		
		// size in memory without mipmaps
		this.sizeInMemory = image.getDepth()*image.getWidth()*image.getHeight();
		// size in memory with mipmaps
		// TODO
	}

	private boolean containsFileName(String string) {
		return getFileName().toLowerCase().contains(string);
	}
	
	public String getFileName() {
		return this.file.getName();
	}
	
	public static TextureFile read(File file2) throws IOException {
		TextureFile tf = new TextureFile(file2);
		tf.setup();
		return tf;
	}

	/**
	 * size in memory without mipmaps
	 */
	@Override
	public int getSize() {
		return this.sizeInMemory;
	}
	
}
