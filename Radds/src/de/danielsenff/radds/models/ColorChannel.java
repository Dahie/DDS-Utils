/**
 * 
 */
package de.danielsenff.radds.models;

import ddsutil.ImageOperations;


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
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
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
