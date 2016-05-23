// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.imagereader.xpm;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Arrays;
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageReader;
import net.multiphasicapps.imagereader.ImageType;

/**
 * This class is able to read XPM images.
 *
 * If the XPM is invalid then the read image data will not be correct.
 *
 * There are also limitations to the reader, only the last color key will be
 * used and it will be treated as a RGB hexadecimal color. Also the pixels
 * per character has a limit of 2 characters, any pixels with characters
 * codes beyond 2 character will only use the first 2.
 *
 * @since 2016/05/08
 */
public class XPMImageReader
	implements ImageReader
{
	/**
	 * Initializes the XPM image reader.
	 *
	 * @since 2016/05/08
	 */
	public XPMImageReader()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/10
	 */
	@Override
	public boolean canRead(String __m)
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__m)
		{
				// Is valid
			case "xpm":
			case "XPM":
			case "image/x-xpixmap":
			case "image/xpm":
			case "image/x-xpm":
			case "application/x-xpm":
				return true;
			
				// Not valid
			default:
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public ImageData readImage(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Create character stripper
		__CharStripper__ cs = new __CharStripper__(new InputStreamReader(__is,
			"utf-8"));
		
		// Read the XPM header
		int[] header = __readHeader(cs);
		
		// Get dimensional data
		int width = Math.max(header[0], 1);
		int height = Math.max(header[1], 1);
		int numcolors = Math.max(header[2], 1);
		int pxchars = Math.max(header[3], 1);
		int hotx = header[4];
		int hoty = header[5];
		
		// Read the color table
		int[] codes = new int[numcolors];
		int[] palette = new int[numcolors];
		__readColorTable(cs, codes, palette, numcolors, pxchars);
		
		// Target array
		int area = width * height;
		int[] data = new int[area];
		
		// Read pixels
		__readPixels(cs, width, height, data, pxchars, codes, palette);
		
		
		throw new Error("TODO");
	}
	
	/**
	 * Decodes a color key value.
	 *
	 * @param __cs The input key value characters.
	 * @return The decoded color value.
	 * @since 2016/05/22
	 */
	private int __decodeColor(CharSequence __cs)
	{
		// Too short?
		int n = __cs.length();
		if (n <= 0)
			return 0x00_000000;
		
		// Must start with '#'
		if (__cs.charAt(0) != '#')
			return 0x00_000000;
		
		// Decode the first 8 digits
		int[] dig = new int[8];
		for (int i = 0, j = 1; i < 8 && j < n; i++, j++)
			dig[i] = Math.max(0, Character.digit(__cs.charAt(j), 16));
		
		// #rgb
		if (n == 4)
			return 0xFF_000000 |
				(dig[0] << 20) |
				(dig[0] << 16) |
				(dig[1] << 12) |
				(dig[1] << 8) |
				(dig[2] << 4) |
				(dig[2]);
		
		// #argb
		else if (n == 5)
			return (dig[0] << 28) |
				(dig[0] << 24) |
				(dig[1] << 20) |
				(dig[1] << 16) |
				(dig[2] << 12) |
				(dig[2] << 8) |
				(dig[3] << 4) |
				(dig[3]);
		
		// #rrggbb
		else if (n == 7)
			return 0xFF_000000 |
				(dig[0] << 20) |
				(dig[1] << 16) |
				(dig[2] << 12) |
				(dig[3] << 8) |
				(dig[4] << 4) |
				(dig[5]);
		
		// #aarrggbb
		else if (n == 9)
			return (dig[0] << 28) |
				(dig[1] << 24) |
				(dig[2] << 20) |
				(dig[3] << 16) |
				(dig[4] << 12) |
				(dig[5] << 8) |
				(dig[6] << 4) |
				(dig[7]);
		
		// Unknown
		else
			return 0x00_000000;
	}
	
	/**
	 * Locates a color for a given color.
	 *
	 * @param __c The code to find the color for.
	 * @param __codes The array of codes.
	 * @param __pal The color palette.
	 * @return The color code used.
	 * @since 2016/05/22
	 */
	private int __locateCode(int __c, int[] __codes, int[] __pal)
	{
		int at = Arrays.binarySearch(__codes, __c);
		if (at >= 0)
			return __pal[at];
		return 0;
	}
	
	/**
	 * Reads the color table of the XPM.
	 *
	 * @param __cs The source characters.
	 * @param __codes The output color codes.
	 * @param __palette The output color palette.
	 * @param __numcolors The number of colors used.
	 * @param __pxchars The number of characters per pixel.
	 * @throws IOException On read errors.
	 * @since 2016/05/22
	 */
	private void __readColorTable(Reader __cs, int[] __codes, int[] __palette,
		int __numcolors, int __pxchars)
		throws IOException
	{
		// Decode the color palette
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < __numcolors; i++)
		{
			// Read new input string
			sb.setLength(0);
			__readLine(__cs, sb);
			
			// Ignore really short lines
			int n = sb.length();
			if (n < __pxchars)
				continue;
			
			// Set code to the given sequence
			int cx = 0;
			if (__pxchars >= 1)
				cx |= (int)sb.charAt(0);
			if (__pxchars >= 2)
				cx |= ((int)sb.charAt(1)) << 16;
			
			// Find the last color key value
			int s, e = n - 1;
			while (e >= __pxchars && sb.charAt(e) <= ' ')
			{
				e--;
				continue;
			}
			
			// Find the start of the color key
			s = e -1;
			while (s >= __pxchars && sb.charAt(s) > ' ')
			{
				s--;
				continue;
			}
			
			// Decode the color
			int col = __decodeColor(sb.subSequence(s + 1, e + 1));
			
			// Find the position to place the code at
			int at = Arrays.binarySearch(__codes, 0, i, cx);
			if (at < 0)
				at = -(at + 1);
			
			// Move all values up
			for (int j = i; j > at; j--)
			{
				__codes[j] = __codes[j - 1];
				__palette[j] = __palette[j - 1];
			}
			
			// Set the value
			__codes[at] = cx;
			__palette[at] = col;
		}
	}
	
	/**
	 * Reads the XPM image heder.
	 *
	 * @param __r The source characters.
	 * @retun The header values.
	 * @throws On read errors.
	 * @since 2016/05/22
	 */
	private int[] __readHeader(Reader __r)
		throws IOException
	{
		// Read XPM header
		int[] header = new int[7];
		for (int i = 0;; i++)
			if (__readInt(__r, header, Math.min(6, i)))
				break;
		
		// Return it
		return header;
	}
	
	/**
	 * Reads a single integer value from the input.
	 *
	 * @param __r The stream to read an integer from.
	 * @param __v The read value.
	 * @param __o The offset in the array index.
	 * @return {@code true} if the line or stream has ended.
	 * @throws IOException On read errors.
	 * @since 2016/05/22
	 */
	private boolean __readInt(Reader __r, int[] __v, int __o)
		throws IOException
	{
		// Setup
		int val = 0;
		boolean neg = false;
		
		// Read character
		for (boolean first = true, startwhite = true;; first = false)
		{
			// Read
			int c = __r.read();
			
			// Ignore starting whitespace
			if (c == ' ' || c == '\t' || c == '\r')
				if (startwhite)
					continue;
			
			// No more whitespace to ignore
			startwhite = false;
			
			// EOF or EOL?
			if (c < 0)
			{
				__v[__o] = (neg ? -val : val);
				return true;
			}
			
			// Negative?
			if (first && c == '-')
			{
				neg = true;
				continue;
			}
			
			// As a digit
			int dig = Character.digit((char)c, 10);
			
			// If not a digit, stop
			if (dig < 0)
			{
				__v[__o] = (neg ? -val : val);
				return false;
			}
			
			// Shift up and add
			val *= 10;
			val += dig;
		}
	}
	
	/**
	 * Reads a single line into the given string builder.
	 *
	 * @param __r The stream to source characters from.
	 * @param __sb The buffer to store the temporary string data.
	 * @throws IOException On read errors.
	 * @since 2015/06/22
	 */
	private void __readLine(Reader __r, StringBuilder __sb)
		throws IOException
	{
		// Read until the end
		for (;;)
		{
			// Read character
			int c = __r.read();
			
			// End of stream or line?
			if (c < 0)
				return;
			
			// Append
			__sb.append((char)c);
		}
	}
	
	/**
	 * Reads the pixels from the XPM image.
	 *
	 * @param __cs The character source.
	 * @param __width The image width.
	 * @param __height The image height.
	 * @param __data The output data.
	 * @param __pxchars The characters per pixel.
	 * @param __codes The character codes.
	 * @param __palette The color palette.
	 * @throws IOException On read errors.
	 * @since 2016/05/22
	 */
	private void __readPixels(Reader __cs, int __width, int __height,
		int[] __data, int __pxchars, int[] __codes, int[] __palette)
		throws IOException
	{
		// Read the XPM image data for each rows
__outer:
		for (int y = 0; y < __height; y++)
			for (int x = 0, z = (y * __width);; x++)
			{
				// Read color code
				int code = 0;
				for (int i = 0; i < __pxchars; i++)
				{
					// Read
					int c = __cs.read();
					
					// Next row?
					if (c == __CharStripper__.END_OF_LINE)
						continue __outer;
					
					// EOF?
					else if (c < 0)
						break __outer;
					
					// First read?
					if (i == 0)
						code = (c & 0xFFFF);
					
					// Second read?
					else if (i == 1)
						code |= (c & 0xFFFF) << 16;
				}
				
				// Find the code used
				__data[z++] = __locateCode(code, __codes, __palette);
			}
	}
}

