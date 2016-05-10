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
import net.multiphasicapps.imagereader.ImageReader;

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
			case "application/x-xpm:"
				return true;
			
				// Not valid
			default
				return false;
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/08
	 */
	@Override
	public int[] readImage(InputStream __is, int[] __dim)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		// Check
		if (__is == null || __dim == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AT01 The output dimensions is less than two.}
		if (__dim.length < 2)
			throw new IllegalArgumentException("AT01");
		
		throw new Error("TODO");
	}
}

