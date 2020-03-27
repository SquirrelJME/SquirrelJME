// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Arrays;
import javax.microedition.lcdui.Image;

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
public class XPMReader
{
	/** Source stream. */
	protected final InputStream in;
	
	/**
	 * Initializes the XPM image reader.
	 *
	 * @param __is The input stream.
	 * @since 2016/05/08
	 */
	public XPMReader(InputStream __is)
		throws NullPointerException
	{
		if (__is == null)
			throw new NullPointerException("NARG");
		
		this.in = __is;
	}
	
	/**
	 * Reads the XPM image data from the specified input stream.
	 *
	 * @return The read image data.
	 * @throws IOException If the XPM is not valid.
	 * @since 2017/02/10
	 */
	public Image parse()
		throws IOException
	{
		InputStream in = this.in;
		
		// Create character stripper
		__CharStripper__ cs = new __CharStripper__(new InputStreamReader(in,
			"utf-8"));
		
		// Read the XPM header
		int[] header = this.__readHeader(cs);
		
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
		boolean alpha = this.__readColorTable(
			cs, codes, palette, numcolors, pxchars);
		
		// Target array
		int area = width * height;
		int[] data = new int[area];
		
		// Read pixels
		this.__readPixels(cs, width, height, data, pxchars, codes, palette);
		
		// Create image
		return Image.createRGBImage(data, width, height, alpha);
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
	 * @return If an alpha channel was used.
	 * @throws IOException On read errors.
	 * @since 2016/05/22
	 */
	private boolean __readColorTable(Reader __cs, int[] __codes,
		int[] __palette, int __numcolors, int __pxchars)
		throws IOException
	{
		// Had alpha?
		boolean hasalpha = false;
		
		// Decode the color palette
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < __numcolors; i++)
		{
			// Read new input string
			sb.setLength(0);
			this.__readLine(__cs, sb);
			
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
			
			// Decode color, detect if there is a transparent pixel
			int col = this.__decodeColor(sb.subSequence(s + 1, e + 1));
			if ((col & 0xFF_000000) != 0xFF_000000)
				hasalpha = true;
			
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
		
		// Return the alpha channel status
		return hasalpha;
	}
	
	/**
	 * Reads the XPM image heder.
	 *
	 * @param __r The source characters.
	 * @return The header values.
	 * @throws IOException On read errors.
	 * @since 2016/05/22
	 */
	private int[] __readHeader(Reader __r)
		throws IOException
	{
		// Read XPM header
		int[] header = new int[7];
		for (int i = 0;; i++)
			if (this.__readInt(__r, header, Math.min(6, i)))
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
		int lastcode = -1;
		int lastpall = -1;
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
						code = c;
					
					// Second read?
					else if (i == 1)
						code |= c << 16;
				}
				
				// Used this color just before? In solidly linear areas, this
				// reduces the need for constant binary searches and increases
				// the parsing speed slightly.
				if (code == lastcode)
					__data[z++] = lastpall;
				
				// Find the code used
				else
				{
					lastpall = this.__locateCode((lastcode = code), __codes,
						__palette);
					__data[z++] = lastpall;
				}
			}
	}
}

