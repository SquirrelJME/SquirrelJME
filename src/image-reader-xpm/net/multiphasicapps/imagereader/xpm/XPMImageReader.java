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
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageReader;
import net.multiphasicapps.imagereader.ImageType;

/**
 * This class is able to read XPM images.
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
		
		// Read XPM header
		int[] header = new int[7];
		for (int i = 0;; i++)
			if (__readInt(cs, header, Math.min(6, i)))
				break;
		
		System.err.print("DEBUG -- XPM header: ");
		for (int i = 0; i < header.length; i++)
			System.err.printf("%d ", header[i]);
		System.err.println();
		
		// DEBUG
		System.err.print("DEBUG -- XPM Chars: ");
		for (;;)
		{
			// Read character
			int c = cs.read();
			boolean eol = (c == __CharStripper__.END_OF_LINE);
			
			if (c < 0 && !eol)
				break;
			
			if (eol)
				System.err.println();
			else
				System.err.print((char)c);
		}
		System.err.println();
		
		throw new Error("TODO");
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
}

