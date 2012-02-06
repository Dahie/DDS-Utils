package util;

/*
 * Copyright 2009, Morten Nobel-Joergensen
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */


import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;


/**
 * @author Heinz Doerr
 */
public class ImageUtils {




	/**
	 * @param img
	 * @return
	 */
	static public String imageTypeName(final BufferedImage img) {
		switch (img.getType()) {
		case BufferedImage.TYPE_3BYTE_BGR: return "TYPE_3BYTE_BGR";
		case BufferedImage.TYPE_4BYTE_ABGR: return "TYPE_4BYTE_ABGR";
		case BufferedImage.TYPE_4BYTE_ABGR_PRE: return "TYPE_4BYTE_ABGR_PRE";
		case BufferedImage.TYPE_BYTE_BINARY: return "TYPE_BYTE_BINARY";
		case BufferedImage.TYPE_BYTE_GRAY: return "TYPE_BYTE_GRAY";
		case BufferedImage.TYPE_BYTE_INDEXED: return "TYPE_BYTE_INDEXED";
		case BufferedImage.TYPE_CUSTOM: return "TYPE_CUSTOM";
		case BufferedImage.TYPE_INT_ARGB: return "TYPE_INT_ARGB";
		case BufferedImage.TYPE_INT_ARGB_PRE: return "TYPE_INT_ARGB_PRE";
		case BufferedImage.TYPE_INT_BGR: return "TYPE_INT_BGR";
		case BufferedImage.TYPE_INT_RGB: return "TYPE_INT_RGB";
		case BufferedImage.TYPE_USHORT_555_RGB: return "TYPE_USHORT_555_RGB";
		case BufferedImage.TYPE_USHORT_565_RGB: return "TYPE_USHORT_565_RGB";
		case BufferedImage.TYPE_USHORT_GRAY: return "TYPE_USHORT_GRAY";
		}
		return "unknown image type #" + img.getType();
	}


	/**
	 * @param img
	 * @return
	 */
	static public int nrChannels(final BufferedImage img) {
		switch (img.getType()) {
		case BufferedImage.TYPE_3BYTE_BGR: return 3;
		case BufferedImage.TYPE_4BYTE_ABGR: return 4;
		case BufferedImage.TYPE_BYTE_GRAY: return 1;
		case BufferedImage.TYPE_INT_BGR: return 3;
		case BufferedImage.TYPE_INT_ARGB: return 4;
		case BufferedImage.TYPE_INT_RGB: return 3;
		case BufferedImage.TYPE_CUSTOM: return 4;
		case BufferedImage.TYPE_4BYTE_ABGR_PRE: return 4;
		case BufferedImage.TYPE_INT_ARGB_PRE: return 4;
		case BufferedImage.TYPE_USHORT_555_RGB: return 3;
		case BufferedImage.TYPE_USHORT_565_RGB: return 3;
		case BufferedImage.TYPE_USHORT_GRAY: return 1;
		}
		return 0;
	}






	/**
	 *
	 * returns one row (height == 1) of byte packed image data in BGR or AGBR form
	 *
	 * @param img
	 * @param y
	 * @param w
	 * @param array
	 * @param temp must be either null or a array with length of w*h
	 * @return
	 */
	public static byte[] getPixelsBGR(final BufferedImage img, 
			final int y, final int w, final byte[] array, final int[] temp) {
		final int x= 0;
		final int h= 1;


		assert array.length == temp.length * nrChannels(img);
		assert (temp.length == w);


		int imageType= img.getType();
		Raster raster;
		switch (imageType) {
		case BufferedImage.TYPE_3BYTE_BGR:
		case BufferedImage.TYPE_4BYTE_ABGR:
		case BufferedImage.TYPE_4BYTE_ABGR_PRE:
		case BufferedImage.TYPE_BYTE_GRAY:
			raster= img.getRaster();
			//int ttype= raster.getTransferType();
			raster.getDataElements(x, y, w, h, array);
			break;
		case BufferedImage.TYPE_INT_BGR:
			raster= img.getRaster();
			raster.getDataElements(x, y, w, h, temp);
			ints2bytes(temp, array, 0, 1, 2);  // bgr -->  bgr
			break;
		case BufferedImage.TYPE_INT_RGB:
			raster= img.getRaster();
			raster.getDataElements(x, y, w, h, temp);
			ints2bytes(temp, array, 2, 1, 0);  // rgb -->  bgr
			break;
		case BufferedImage.TYPE_INT_ARGB:
		case BufferedImage.TYPE_INT_ARGB_PRE:
			raster= img.getRaster();
			raster.getDataElements(x, y, w, h, temp);
			ints2bytes(temp, array, 2, 1, 0, 3);  // argb -->  abgr
			break;
		case BufferedImage.TYPE_CUSTOM: // TODO: works for my icon image loader, but else ???
			img.getRGB(x, y, w, h, temp, 0, w);
			ints2bytes(temp, array, 2, 1, 0, 3);  // argb -->  abgr
			break;
		default:
			img.getRGB(x, y, w, h, temp, 0, w);
			ints2bytes(temp, array, 2, 1, 0);  // rgb -->  bgr
			break;
		}


		return array;
	}


	/**
	 * converts and copies byte packed  BGR or ABGR into the img buffer,
	 *              the img type may vary (e.g. RGB or BGR, int or byte packed)
	 *              but the number of components (w/o alpha, w alpha, gray) must match
	 *
	 * does not unmange the image for all (A)RGN and (A)BGR and gray imaged
	 * @param bgrPixels 
	 * @param img 
	 * @param x 
	 * @param y 
	 * @param w 
	 * @param h 
	 *
	 */
	public static void setBGRPixels(final byte[] bgrPixels, 
			final BufferedImage img, final int x, final int y, final int w, final int h) {
		int imageType= img.getType();
		WritableRaster raster= img.getRaster();
		//int ttype= raster.getTransferType();
		if (imageType == BufferedImage.TYPE_3BYTE_BGR ||
				imageType == BufferedImage.TYPE_4BYTE_ABGR ||
				imageType == BufferedImage.TYPE_4BYTE_ABGR_PRE ||
				imageType == BufferedImage.TYPE_BYTE_GRAY) {
			raster.setDataElements(x, y, w, h, bgrPixels);
		} else {
			int[] pixels;
			if (imageType == BufferedImage.TYPE_INT_BGR) {
				pixels= bytes2int(bgrPixels, 2, 1, 0);  // bgr -->  bgr
			} else if (imageType == BufferedImage.TYPE_INT_ARGB ||
					imageType == BufferedImage.TYPE_INT_ARGB_PRE) {
				pixels= bytes2int(bgrPixels, 3, 0, 1, 2);  // abgr -->  argb
			} else {
				pixels= bytes2int(bgrPixels, 0, 1, 2);  // bgr -->  rgb
			}
			if (w == 0 || h == 0) {
				return;
			} else if (pixels.length < w * h) {
				throw new IllegalArgumentException("pixels array must have a length" + " >= w*h");
			}
			if (imageType == BufferedImage.TYPE_INT_ARGB ||
					imageType == BufferedImage.TYPE_INT_RGB ||
					imageType == BufferedImage.TYPE_INT_ARGB_PRE ||
					imageType == BufferedImage.TYPE_INT_BGR) {
				raster.setDataElements(x, y, w, h, pixels);
			} else {
				// Unmanages the image
				img.setRGB(x, y, w, h, pixels, 0, w);
			}
		}
	}




	/**
	 * @param in
	 * @param out
	 * @param index1
	 * @param index2
	 * @param index3
	 */
	public static void ints2bytes(final int[] in, 
			final byte[] out, final int index1, 
			final int index2, final int index3) {
		for (int i= 0; i < in.length; i++) {
			int index= i * 3;
			int value= in[i];
			out[index + index1]= (byte)value;
			value= value >> 8;
		out[index + index2]= (byte)value;
		value= value >> 8;
		out[index + index3]= (byte)value;
		}
	}


	/**
	 * @param in
	 * @param out
	 * @param index1
	 * @param index2
	 * @param index3
	 * @param index4
	 */
	public static void ints2bytes(final int[] in, final byte[] out, 
			final int index1, final int index2, 
			final int index3, final int index4) {
		for (int i= 0; i < in.length; i++) {
			int index= i * 4;
			int value= in[i];
			out[index + index1]= (byte)value;
			value= value >> 8;
		out[index + index2]= (byte)value;
		value= value >> 8;
		out[index + index3]= (byte)value;
		value= value >> 8;
		out[index + index4]= (byte)value;
		}
	}


	/**
	 * @param in
	 * @param index1
	 * @param index2
	 * @param index3
	 * @return
	 */
	public static int[] bytes2int(final byte[] in, final int index1, 
			final int index2, final int index3) {
		int[] out= new int[in.length / 3];
		for (int i= 0; i < out.length; i++) {
			int index= i * 3;
			int b1= (in[index +index1] & 0xff) << 16;
			int b2= (in[index + index2] & 0xff) << 8;
			int b3= in[index + index3] & 0xff;
			out[i]= b1 | b2 | b3;
		}
		return out;
	}


	/**
	 * @param in
	 * @param index1
	 * @param index2
	 * @param index3
	 * @param index4
	 * @return
	 */
	public static int[] bytes2int(final byte[] in, final int index1, 
			final int index2, final int index3, final int index4) {
		int[] out= new int[in.length / 4];
		for (int i= 0; i < out.length; i++) {
			int index= i * 4;
			int b1= (in[index +index1] & 0xff) << 24;
			int b2= (in[index +index2] & 0xff) << 16;
			int b3= (in[index + index3] & 0xff) << 8;
			int b4= in[index + index4] & 0xff;
			out[i]= b1 | b2 | b3 | b4;
		}
		return out;
	}


	/**
	 * Converts the {@link BufferedImage} type.
	 * @param srcImage
	 * @param destImgType
	 * @return
	 */
	public static BufferedImage convert(final BufferedImage srcImage, final int destImgType) {
		BufferedImage img= new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), destImgType);
		Graphics2D g2d= img.createGraphics();
		g2d.drawImage(srcImage, 0, 0, null);
		g2d.dispose();
		return img;
	}




}