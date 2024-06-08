/**
 * 
 */
package org.dds_utils.radds.models;

import org.ddsutil.ImageOperations;


/**
 * @author danielsenff
 *
 */
public class ColorChannel {

	ImageOperations.ChannelMode channel;
	String titel;
	
	/**
	 * 
	 */
	public ColorChannel(ImageOperations.ChannelMode channel, String titel) {
		this.channel = channel;
		this.titel = titel;
	}

	@Override
	public String toString() {
		return this.titel;
	}
	
	/**
	 * Returns the titel of the Channel
	 * @return
	 */
	public String getTitel() {
		return this.titel;
	}
	
	/**
	 * Return the channel
	 * @return
	 */
	public ImageOperations.ChannelMode getChannel() {
		return this.channel;
	}
	
}
