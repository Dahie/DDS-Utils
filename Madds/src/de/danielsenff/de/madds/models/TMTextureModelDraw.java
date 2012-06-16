package de.danielsenff.de.madds.models;

import java.awt.Color;
import java.awt.Paint;

import net.bouthier.treemapSwing.fileViewer.TMFileModelDraw;
import de.danielsenff.de.madds.util.ByteConverter;

public class TMTextureModelDraw extends TMFileModelDraw {

	@Override
	public Paint getFillingOfObject(Object node) {
		
		TextureFile textureNode = TextureHashMap.getTextureHashMap().get(node);
		if(textureNode instanceof TextureFile) {
			switch (textureNode.getMaterial()) {
			case Normal:	return Color.CYAN;
			case Diffuse:	return Color.RED;
			case Specular:	return Color.BLUE;
			case Other:	return Color.ORANGE;
			default:
				break;
			}
		}
		
		return Color.BLACK; 
	}
	
	@Override
	public String getTooltipOfObject(Object node) {
		String tooltip = super.getTooltipOfObject(node);
		
		TextureFile textureNode = TextureHashMap.getTextureHashMap().get(node);
		if(textureNode instanceof TextureFile) {
			long size = textureNode.getSize();
			tooltip += "<p>"+ByteConverter.bit2MibiByte(size) + " MByte";
		}
		
		return tooltip;
	}
	
}
