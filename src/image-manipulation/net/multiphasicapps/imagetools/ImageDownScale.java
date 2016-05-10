// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.imagetools;

import java.io.IOException;

/**
 * This describes a weighted downscale of an image.
 *
 * @since 2016/05/10
 */
public class ImageDownScale
{
	/**
	 * Downscales an image.
	 *
	 * @param __args Program arguments.
	 * @since 2016/05/10
	 */
	public static void main(String... __args)
	{
		// Must always exist
		if (__args == null)
			__args = new String[0];
		
		// {@squirreljme.error AS04 Usage: [image] [width] [height] <weights>;
		// The width or height may be "auto" to signify that it should match
		// the ratio of the other downscaled dimension; weights is optional and
		// is used to determine which color to select when downscaling.}
		int an;
		if ((an = __args.length) < 3)
			throw new IllegalArgumentException("AS04");
		
		// Read in the details
		String input = __args[0];
		int w = __decode(__args[1]),
			h = __decode(__args[2]);
		String sway = (an >= 4 ? __args[3] : null);
		
		// {@squirreljme.error AS08 The target width and height cannot both be
		// automatic.}
		if (w < 0 && h < 0)
			throw new IllegalArgumentException("AS08");
		
		// Open the input image data
		int[] idim;
		int[] idata;
		try
		{
			idata = Main.loadImage(input, (idim = new int[2]));
		}
		
		// Failed
		catch (IOException e)
		{
			// {@squirreljme.error AS05 Failed to read the input image
			// data. (The input image file)}
			throw new RuntimeException(String.format("AS05 %s", input), e);
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * Parses the string as an input for the target dimension.
	 *
	 * @param __s The string to translate.
	 * @return The integer value for the dimension, negative values mean auto
	 * scaled.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/10
	 */
	private static int __decode(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Automatic?
		if (__s.equalsIgnoreCase("auto"))
			return -1;
		
		// Otherwise decode
		try
		{
			// Decode it
			int rv = Integer.decode(__s);
			
			// {@squirreljme.error AS07 A dimension cannot be zero or
			// negative. (The dimension)}
			if (rv <= 0)
				throw new IllegalArgumentException(String.format("AS07 %d",
					rv));
			
			// Return it
			return rv;
		}
		
		// Illegal number
		catch (NumberFormatException e)
		{
			// {@squirreljme.error AS06 The given number is not a valid
			// dimension.(The input string)}
			throw new IllegalArgumentException(String.format("AS06 %s", __s),
				e);
		}
	}
}

