package de.danielsenff.de.madds.models;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;

import jogl.DDSImage;

public class TextureFile implements Sizable{

	private File file;
	private DDSImage image;
	private Dimension dimension;
	private int bitrate;
	private long sizeInMemory;
	private Material material;
	private int compression;
	
	public TextureFile(File file) {
		this.file = file;
	}
	
	private void setup() throws IOException {
		this.image = DDSImage.read(this.file);
		this.dimension = new Dimension(image.getWidth(), image.getHeight());
		this.compression = image.getCompressionFormat();
		
		if(image.getDepth() > 0)
			this.bitrate = image.getDepth();
		else if (image.getPixelFormat() == DDSImage.D3DFMT_DXT5) {
			this.bitrate = 32;
		}else if (image.getPixelFormat() == DDSImage.D3DFMT_DXT1) {
			this.bitrate = 32;
		}else if (image.getPixelFormat() == DDSImage.D3DFMT_DXT3) {
			this.bitrate = 32;
		}
		
		if(matchesFileName("(.+)(_[rdbns][0-9]+)(\\..+)")) {
			// animations
			this.material = Material.Animation;
		} else 	if(containsFileName("_s.")) {
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
		this.sizeInMemory = 0;
		for (int i = 0; i < image.getNumMipMaps(); i++) {
			this.sizeInMemory += image.mipMapSizeInBytes(i)*8;
		}
	}

	private boolean containsFileName(String string) {
		return getFileName().toLowerCase().contains(string);
	}
	
	private boolean matchesFileName(String string) {
		return getFileName().toLowerCase().matches(string);
	}
	
	public Dimension getDimension() {
		return dimension;
	}
	
	public int getCompression() {
		return compression;
	}
	
	public int getBitrate() {
		return bitrate;
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
	public long getSize() {
		return this.sizeInMemory;
	}

	@Override
	public File getFile() {
		return this.file;
	}

	@Override
	public void setSize(long size) {
		this.sizeInMemory = size;
	}

	@Override
	public void addSize(long diff) {
		this.sizeInMemory += diff;
	}

	public Material getMaterial() {
		return this.material;
	}
	
}
