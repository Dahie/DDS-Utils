/**
 * 
 */
package de.danielsenff.badds.operations;

import util.Debug;
import DDSUtil.ImageOperations;

/**
 * @author danielsenff
 *
 */
public class ChannelBrightness extends ManipulateChannel {

	private float brightness;
	private int channel;


	/**
	 * @param rgbChannel
	 * @param value
	 */
	public ChannelBrightness(int rgbChannel, float value) {
		this.channel = rgbChannel;
		this.brightness = value;
	}

	/**
	 * @param rgbChannel
	 * @param value
	 */
	public ChannelBrightness(int rgbChannel, double value) {
		this.channel = rgbChannel;
		this.brightness = (float) value;
	}
	
	public int[] filterRGB(int x, int y, int[] rgba) {
		int oldValue = rgba[this.channel];
		int newValue = (int) (oldValue  * (brightness*255));
		newValue = ImageOperations.checkValueLimits(newValue, 0, 255);
		rgba[this.channel] = newValue;
		return rgba;
	}


}
