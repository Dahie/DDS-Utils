package de.danielsenff.dropps.models;

/**
* Copyright (c) 2007-2009, JAGaToo Project Group all rights reserved.
* 
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* 
* Redistributions of source code must retain the above copyright notice,
* this list of conditions and the following disclaimer.
* 
* Redistributions in binary form must reproduce the above copyright notice,
* this list of conditions and the following disclaimer in the documentation
* and/or other materials provided with the distribution.
* 
* Neither the name of the 'Xith3D Project Group' nor the names of its
* contributors may be used to endorse or promote products derived from this
* software without specific prior written permission.
* 
* THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
* ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
* LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
* CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
* SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
* INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
* CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) A
* RISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
* POSSIBILITY OF SUCH DAMAGE
*/

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.BufferedInputStream;
import java.io.IOException;

//import org.jagatoo.util.image.ImageUtility;

/**
* Reads TGA files from an InputStream.
* 
* @author Marvin Froehlich (aka Qudus)
*/
public class TextureImageFormatLoaderTGA
{
   private static final int HEADER_SIZE = 18;

   private static final int HEADER_INVALID = 0;
   private static final int HEADER_UNCOMPRESSED = 1;
   private static final int HEADER_COMPRESSED = 2;

   private static final short getUnsignedByte( byte[] bytes, int byteIndex )
   {
       return ( (short)( bytes[byteIndex] & 0xFF ) );
   }

   private static final int getUnsignedShort( byte[] bytes, int byteIndex )
   {
       return ( ( getUnsignedByte( bytes, byteIndex + 1 ) << 8 ) + getUnsignedByte( bytes, byteIndex + 0 ) );
   }

   private static void readBuffer( BufferedInputStream in, byte[] buffer ) throws IOException
   {
       int bytesRead = 0;
       int bytesToRead = buffer.length;
       while ( bytesToRead > 0 )
       {
           int read = in.read( buffer, bytesRead, bytesToRead );
           bytesRead += read;
           bytesToRead -= read;
       }
   }

   private static final void skipBytes( BufferedInputStream in, long toSkip ) throws IOException
   {
       while ( toSkip > 0L )
       {
           long skipped = in.skip( toSkip );

           if ( skipped > 0 )
               toSkip -= skipped;
           else if ( skipped < 0 )
               toSkip = 0;
       }
   }

   private static final int compareFormatHeader( BufferedInputStream in, byte[] header ) throws IOException
   {
       readBuffer( in, header );

       boolean hasPalette = false;
       int result = HEADER_INVALID;

       /*
        * 0: Length of Image-ID (usually 0)
        */
       int imgIDSize = getUnsignedByte( header, 0 );

       /*
        * 1: boolean for palette
        * 
        * possible values:
        * 0 = no palette
        * 1 = has palette
        */
       if ( ( header[1] != (byte)0 ) && ( header[1] != (byte)1 ) )
           return ( HEADER_INVALID );

       /*
        * 2: image type
        * 
        * possible values:
        * 0  = no image data
        * 1  = indexed (palette), uncompressed
        * 2  = RGB(A) (no palette), uncompressed
        * 3  = monochrome (no palette), uncompressed
        * 9  = indexed (palette), compressed (RLE)
        * 10 = RGB(A) (no palette), compressed (RLE)
        * 11 = monochrome, compressed (RLE)
        */
       switch ( getUnsignedByte( header, 2 ) )
       {
           case 0:
               result = HEADER_UNCOMPRESSED;
               break;
           case 1:
               hasPalette = true;
               result = HEADER_UNCOMPRESSED;
               System.err.println( "Indexed TGA is not yet supported!" );
               return ( HEADER_INVALID );
           case 2:
               result = HEADER_UNCOMPRESSED;
               break;
           case 3:
               result = HEADER_UNCOMPRESSED;
               break;
           case 9:
               hasPalette = true;
               result = HEADER_COMPRESSED;
               System.err.println( "Indexed TGA is not yet supported!" );
               return ( HEADER_INVALID );
           case 10:
               result = HEADER_COMPRESSED;
               break;
           case 11:
               result = HEADER_COMPRESSED;
               break;
           default:
               return ( HEADER_INVALID );
       }

       /*
        * 3/4: Begin of palette data (default: 0)
        */
       if ( !hasPalette )
       {
           if ( getUnsignedShort( header, 3 ) != 0 )
           {
               // No palette data, but palette offset specified!
               return ( HEADER_INVALID );
           }
       }

       /*
        * 5/6: Number of colors in the palette
        */
       if ( !hasPalette )
       {
           if ( getUnsignedShort( header, 5 ) != 0 )
           {
               // No palette data, but palette size specified!
               return ( HEADER_INVALID );
           }
       }

       /*
        * 7: Size of one palette entry in bits
        * 
        * possible values:
        * 0: this value is expected, if no palette is being used.
        * 15, 16, 24, 32
        */
       short paletteEntrySize = getUnsignedByte( header, 7 );
       if ( !hasPalette )
       {
           if ( paletteEntrySize != 0 )
               // No palette, but non-zero palette-entry-size!
               return ( HEADER_INVALID );
       }
       else
       {
           if ( ( paletteEntrySize != 15 ) && ( paletteEntrySize != 16 ) && ( paletteEntrySize != 24 ) && ( paletteEntrySize != 32 ) )
               // Invalid palette-entry-size!
               return ( HEADER_INVALID );
       }

       /*
        * 8/9: X-coordinate of the image origin
        * 
        * Should always be 0
        */
       if ( getUnsignedShort( header, 8 ) != 0 )
       {
           return ( HEADER_INVALID );
       }

       /*
        * 10/11: Y-coordinate of the image origin
        * 
        * Should always be 0
        */
       if ( getUnsignedShort( header, 10 ) != 0 )
       {
           return ( HEADER_INVALID );
       }

       /*
        * 12/13: Image width
        */

       /*
        * 14/15: Image height
        */

       /*
        * 16: its per pixel (BPP)
        */
       switch ( getUnsignedByte( header, 16 ) )
       {
           case 1:
           case 8:
           case 15:
           case 16:
               System.err.println( "TGAs with non RGB or RGBA pixels are not yet supported." );
               return ( HEADER_INVALID );
           case 24:
           case 32:
               break;
           default:
               return ( HEADER_INVALID );
       }

       /*
        * 17: attributes
        */

       /*
        * If 'imgIDSize' is non-zero, we need to read the image-ID.
        */
       if ( imgIDSize != 0 )
       {
           // We don't need the image-ID. So we simply skip it.
           skipBytes( in, imgIDSize );
       }

       return ( result );
   }

   private static final void writePixel( final byte red, final byte green, final byte blue, final byte alpha, final boolean hasAlpha, byte[] buffer, final int offset )
   {
       if ( hasAlpha )
       {
           buffer[offset + 0] = alpha;
           buffer[offset + 1] = blue;
           buffer[offset + 2] = green;
           buffer[offset + 3] = red;
       }
       else
       {
           buffer[offset + 0] = blue;
           buffer[offset + 1] = green;
           buffer[offset + 2] = red;
       }
   }

   private static void readBuffer( BufferedInputStream in, int width, int height, int srcBytesPerPixel, boolean acceptAlpha, boolean flipVertically, byte[] bb ) throws IOException
   {
       byte[] buffer = new byte[ srcBytesPerPixel ];
       final boolean copyAlpha = ( srcBytesPerPixel == 4 ) && acceptAlpha;
       final int dstBytesPerPixel = acceptAlpha ? srcBytesPerPixel : 3;
       final int trgLineSize = width * dstBytesPerPixel;

       int dstByteOffset = 0;

       for ( int y = 0; y < height; y++ )
       {
           for ( int x = 0; x < width; x++ )
           {
               int read = in.read( buffer, 0, srcBytesPerPixel );

               if ( read < srcBytesPerPixel )
                   return;

               int actualByteOffset = dstByteOffset;
               if ( !flipVertically )
                   actualByteOffset = ( ( height - y - 1 ) * trgLineSize ) + ( x * dstBytesPerPixel );

               if ( copyAlpha ) // has alpha?
                   writePixel( buffer[2], buffer[1], buffer[0], buffer[3], true, bb, actualByteOffset );
               else
                   writePixel( buffer[2], buffer[1], buffer[0], (byte)0, false, bb, actualByteOffset );

               dstByteOffset += dstBytesPerPixel;
           }
       }
   }

   /**
    * Loads an uncompressed TGA (note, much of this code is based on NeHe's)
    * 
    * @param header
    * @param in
    * @param acceptAlpha
    * @param flipVertically
    * @param allowStreching
    * @param texFactory
    * 
    * @return the TextureImage or null.
    * 
    * @throws IOException
    */
   private BufferedImage loadUncompressedTGA( byte[] header, BufferedInputStream in, boolean acceptAlpha, boolean flipVertically ) throws IOException
   {
       // TGA Loading code nehe.gamedev.net)

       // Determine The TGA width (highbyte * 256 + lowbyte)
       int orgWidth = getUnsignedShort( header, 12 );
       // Determine The TGA height (highbyte * 256 + lowbyte)
       int orgHeight = getUnsignedShort( header, 14 );
       // Determine the bits per pixel
       int bpp = getUnsignedByte( header, 16 );

       //boolean isOriginLeft = ( header[17] & 0x10 ) == 0;
       boolean isOriginBottom = ( header[17] & 0x20 ) == 0;
       //boolean isOriginLowerLeft =  isOriginLeft && isOriginBottom;

       if ( !isOriginBottom )
       {
           flipVertically = !flipVertically;
       }

       // Make sure all information is valid
       if ( ( orgWidth <= 0 ) || ( orgHeight <= 0 ) || ( ( bpp != 24 ) && ( bpp != 32 ) ) )
       {
           throw new IOException( "Invalid texture information" );
       }

       // Compute the number of BYTES per pixel
       int bytesPerPixel = ( bpp / 8 );
       // Compute the total amout ofmemory needed to store data
       //int imageSize = orgWidth * orgHeight * bytesPerPixel;

       int width = orgWidth;
       int height = orgHeight;

       final int dstBytesPerPixel = ( acceptAlpha && ( bytesPerPixel == 4 ) ? 4 : 3 );

       BufferedImage image = new BufferedImage( width, height, dstBytesPerPixel == 4 ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR );
       byte[] imageData = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

       readBuffer( in, orgWidth, orgHeight, bytesPerPixel, acceptAlpha, flipVertically, imageData );
       
       
       return ( image );
   }

   /**
    * Loads COMPRESSED TGAs
    * 
    * @param header
    * @param in
    * @param acceptAlpha
    * @param flipVertically
    * @param allowStreching
    * @param texFactory
    * 
    * @return the TextureImage or null.
    * 
    * @throws IOException
    */
   private BufferedImage loadCompressedTGA( byte[] header, BufferedInputStream in, boolean acceptAlpha, boolean flipVertically ) throws IOException
   {
       // Determine The TGA width (highbyte * 256 + lowbyte)
       int orgWidth = getUnsignedShort( header, 12 );
       // Determine The TGA height (highbyte * 256 + lowbyte)
       int orgHeight = getUnsignedShort( header, 14 );
       // Determine the bits per pixel
       int bpp = getUnsignedByte( header, 16 );

       //boolean isOriginLeft = ( header[17] & 0x10 ) == 0;
       boolean isOriginBottom = ( header[17] & 0x20 ) == 0;
       //boolean isOriginLowerLeft =  isOriginLeft && isOriginBottom;

       if ( !isOriginBottom )
       {
           flipVertically = !flipVertically;
       }

       // Make sure all information is valid
       if ( ( orgWidth <= 0 ) || ( orgHeight <= 0 ) || ( ( bpp != 24 ) && ( bpp != 32 ) ) )
       {
           throw new IOException( "Invalid texture information" );
       }

       // Compute the number of BYTES per pixel
       int bytesPerPixel = ( bpp / 8 );
       // Compute the total amout ofmemory needed to store data
       //int imageSize = ( bytesPerPixel * imageWidth * imageHeight );
       // Allocate that much memory
       //byte imageData[] = new byte[ imageSize ];
       // Number of pixels in the image
       int pixelCount = orgHeight * orgWidth;
       // Current byte
       // Current pixel being read
       int currentPixel = 0;
       // Storage for 1 pixel
       byte[] colorBuffer = new byte[ bytesPerPixel ];

       int width = orgWidth;
       int height = orgHeight;

       final int dstBytesPerPixel = ( acceptAlpha && ( bytesPerPixel == 4 ) ? 4 : 3 );
       final int trgLineSize = orgWidth * dstBytesPerPixel;

       BufferedImage image = new BufferedImage( width, height, dstBytesPerPixel == 4 ? BufferedImage.TYPE_4BYTE_ABGR : BufferedImage.TYPE_3BYTE_BGR );
       byte[] imageData = ((DataBufferByte)image.getRaster().getDataBuffer()).getData();

       int dstByteOffset = 0;

       do
       {
           // Storage for "chunk" header
           int chunkHeader = 0;
           try
           {
               chunkHeader = (byte)in.read() & 0xFF;
           }
           catch ( IOException e )
           {
               throw new IOException( "Could not read RLE header" );
           }

           boolean repeatColor;

           /*
            * If the header is < 128, it means, the that is the number of RAW
            * color packets minus 1.
            */
           if ( chunkHeader < 128 )
           {
               // add 1 to get number of following color values
               chunkHeader++;

               repeatColor = false;
           }
           // chunkheader > 128 RLE data, next color repeated chunkheader - 127 times
           else
           {
               // Subtract 127 to get rid of the ID bit
               chunkHeader -= 127;

               readBuffer( in, colorBuffer );

               repeatColor = true;
           }

           for ( int counter = 0; counter < chunkHeader; counter++ )
           {
               if ( !repeatColor )
               {
                   readBuffer( in, colorBuffer );
               }

               // write to memory

               int x = currentPixel % orgWidth;
               int y = currentPixel / orgWidth;

               int actualByteOffset = dstByteOffset;
               if ( !flipVertically )
                   actualByteOffset = ( ( height - y - 1 ) * trgLineSize ) + ( x * dstBytesPerPixel );

               // Swap R and B, because TGA stores them swapped.

               if ( dstBytesPerPixel == 4 ) // has alpha?
                   writePixel( colorBuffer[2], colorBuffer[1], colorBuffer[0], colorBuffer[3], true, imageData, actualByteOffset );
               else
                   writePixel( colorBuffer[2], colorBuffer[1], colorBuffer[0], (byte)0, false, imageData, actualByteOffset );

               dstByteOffset += dstBytesPerPixel;

               // Increase current pixel by 1
               currentPixel++;

               // Make sure we havent read too many pixels
               if ( currentPixel > pixelCount )
               {
                   // if there is too many... Display an error!
                   throw new IOException( "Too many pixels read" );
               }
           }
       }
       while ( currentPixel < pixelCount ); // Loop while there are still pixels left...

       return ( image );
   }

   /**
    * can return null for invalid tga files
    * {@inheritDoc}
    */
   public BufferedImage loadTextureImage( BufferedInputStream in, boolean acceptAlpha, boolean flipVertically ) throws IOException
   {
       if ( in.available() < HEADER_SIZE )
       {
           return ( null );
       }

       byte[] header = new byte[ HEADER_SIZE ];

       final int headerType = compareFormatHeader( in, header );

       if ( headerType == HEADER_INVALID )
    	   throw new IOException("headers are invalid");
           //return ( null );

       BufferedImage image = null;

       if ( headerType == HEADER_UNCOMPRESSED )
       {
           // If so, jump to uncompressed TGA loading code
           image = loadUncompressedTGA( header, in, acceptAlpha, flipVertically );
       }
       else if ( headerType == HEADER_COMPRESSED )
       {
           // If so, jump to compressed TGA loading code
           image = loadCompressedTGA( header, in, acceptAlpha, flipVertically );
       }
       // If header matches neither type
       else
       {
           throw new IOException("TGA file be type 2 or type 10 ");
//           return ( null );
       }

       return ( image );
   }

}