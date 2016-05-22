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
		
		throw new Error("TODO");
	}
}

