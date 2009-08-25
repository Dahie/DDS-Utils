/*
  An easy way to load TGA files.
 
  Copyright 2001, Robert Allan Zeh (razeh@yahoo.com)
  7346 Lake Street #3W
  River Forest, IL 60305
 
  This library is free software; you can redistribute it and/or modify
  it under the terms of the GNU Lesser General Public License as
  published by the Free Software Foundation; either version 2 of the
  License, or (at your option) any later version.

  This library is distributed in the hope that it will be useful, but
  WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
  Lesser General Public License for more details.

  You should have received a copy of the GNU Lesser General Public
  License along with this library; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
  USA

*/
package de.danielsenff.radds.models;

import java.awt.Canvas;
import java.awt.Dimension;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/** Supports the reading and writing of simple TGA files.
 *
 * @author Robert Allan Zeh (razeh@yahoo.com)
 *
 * @version 0.4
 */


public class TGAFile {
  /** Construct a TGAFile object from a OpenGL object and a canvas. 
   * @param gl the OpenGL context to use for our readPixels call.
   * @param canvas the canvas we use to create our TGA object from.
   */
  /*public TGAFile(GL gl, Canvas canvas) {
    setSize(new Dimension(canvas.getSize().width, canvas.getSize().height));
    data = (byte[]) gl.readPixels(0, 0, getSize().width, getSize().height, 
				  GLConstants.RGBA, GLConstants.GL_UNSIGNED_BYTE);
  }*/

  /** Write out a TGA file. 
   * @param filename the name of the file we will open and write to. 
   */
  public void write(String filename) throws java.io.IOException {
    BufferedOutputStream output = new BufferedOutputStream(new DataOutputStream(new FileOutputStream(filename)), 10240);

    /* We handle the 18 TGA byte header here. */
    output.write(0); // Field 1: The size of field 6.
    output.write(0); // Field 2: 0 indicates that there is no color map.
    output.write(2); // Field 3: 2 indicates a true color image.
    output.write(0); // Field 4.1: No color map, so we write out zero.
    output.write(0); // Field 4.1: No color map, so we write out zero.
    output.write(0); // Field 4.2: No color map, so we write out zero.
    output.write(0); // Field 4.2: No color map, so we write out zero.
    output.write(0); // Field 4.3: No color map, so we write out zero.
    output.write(0); // Field 5.1: Two byte X-origin of zero.
    output.write(0); // Field 5.1: Two byte X-origin of zero.
    output.write(0); // Field 5.2: Two byte Y-origin of zero.
    output.write(0); // Field 5.2: Two byte Y-origin of zero.
    output.write((byte)(getSize().width & 0xff)); // Field 5.3: LSB of the image width.
    output.write((byte)(getSize().width >> 8));   // Field 5.3: MSB of the image width.
    output.write((byte)(getSize().height & 0xff)); // Field 5.3: LSB of the image height.
    output.write((byte)(getSize().height >> 8));   // Field 5.3: MSB of the image height.
    output.write(24); // Field 5.5: The number of bits per pixel.
    output.write(0); // Field 5.6: 0 bits of alpha per pixel, with the image starting
                     // in the lower left.

    // Write out the data buffer.
    int index = 0;
    for(int j = 0; j < getSize().height; j++) {
      for(int i = 0; i < getSize().width; i++) {
	output.write(data[index+2]);
	output.write(data[index+1]);
	output.write(data[index+0]);
	// No alpha, so no ouput write(data[index+3]);
	index += 4;
      }
    }
    output.close();
  }

  /** Read in a TGAFile from a named file.  We are not very careful
   * about the image formats that we read in.
   * @param filename the name of the file that we construct our image from  
   */
  public TGAFile(String filename){
    byte headerInformation[] = new byte[18];
    int MSWidthByte, LSWidthByte, MSHeightByte, LSHeightByte;
    byte imageType;
    int w, h;
    BufferedInputStream in = null;
    try {
      in = new BufferedInputStream(new FileInputStream(new File(filename)), 10240);
      for(int i = 0; i < 18; i++) {
	headerInformation[i] = (byte) in.read();
      }

      MSWidthByte = headerInformation[13];
      LSWidthByte = headerInformation[12];
      if (MSWidthByte < 0) MSWidthByte += 256;
      if (LSWidthByte < 0) LSWidthByte += 256;
      w = (256*MSWidthByte) + LSWidthByte;

      MSHeightByte = headerInformation[15];
      LSHeightByte = headerInformation[14];
      if (MSHeightByte < 0) MSHeightByte += 256;
      if (LSHeightByte < 0) LSHeightByte += 256;
      h = (256*MSHeightByte) + LSHeightByte;

      setSize(new Dimension(w, h));
      data = new byte[w*h*4];
      int index = 0;
      for (int j = 0; j < h; j++) {
	for (int i = 0; i < w; i++) {
	  data[index+2] = (byte) in.read();
	  data[index+1] = (byte) in.read();
	  data[index+0] = (byte) in.read();
	  data[index+3] = (byte) 255;
	  index += 4;
	}
      }
      in.close();
    }
    catch(IOException e){
      e.printStackTrace();
    }
  }

  /** Returns the raw TGA data, in RGBA format.
   * @return an array of bytes describing our image.
   */
  public byte[] getData() { return data;}
  /** Returns the size of our image.
   * @return the size of our image. 
   */
  public Dimension getSize() { return size; }
  /** Sets the size of our image.  This does not change our data array, so
   * really bad things can happen if you set our size to something that
   * doesn't match our data array.
   * @param newSize the size of our image after the call. 
   */
  public void setSize(Dimension newSize) { size = newSize; }
  private Dimension size;
  private byte[] data;
};
