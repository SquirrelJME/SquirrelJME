// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mascot;

import java.io.InputStream;
import java.util.ServiceLoader;
import net.multiphasicapps.io.hex.HexInputStream;

/**
 * This is the base class for looking up resources which contain the mascot.
 *
 * @since 2016/06/16
 */
public abstract class MascotResources
{
	/** The service lookup instance. */
	private static final ServiceLoader<MascotResources> _SERVICES =
		ServiceLoader.<MascotResources>load(MascotResources.class);
	
	/** The type of image data used. */
	protected final String type;
	
	/**
	 * This initializes the base resource lookup.
	 *
	 * @param __type The data type of the input image.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public MascotResources(String __type)
		throws NullPointerException
	{
		// Check
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __type;
	}
	
	/**
	 * Internally looks up the given mascot for an image that has the specified
	 * width and height.
	 *
	 * @param __hex If {@code true} then a request is made for a hexadecimal
	 * encoded image, which will be automatically translated.
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @return The input stream to the given image or {@code null} if it was
	 * not found.
	 * @since 2016/06/16
	 */
	protected abstract InputStream internalLookup(boolean __hex,
		int __w, int __h);
	
	/**
	 * Loads an image with the specified type, width, and height.
	 *
	 * @param __type The type of image to find.
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @return The input stream of the image data or {@code null} if not found.
	 * @since 2016/06/16
	 */
	public static InputStream loadImage(String __type, int __w, int __h)
	{
		// Lock on services
		ServiceLoader<MascotResources> svl = _SERVICES;
		synchronized (svl)
		{
			// Go through all services and handle the same type
			for (MascotResources mr : svl)
				if (mr.type.equals(__type))
				{
					// Try hex image first
					InputStream is = internalLookup(true, __w, __h);
					if (is != null)
						return new HexInputStream(is);
					
					// Otherwise try normal data
					is = internalLookup(false, __w, __h);
					if (is != null)
						return is;
				}
		}
		
		// Not found
		return null;
	}
}

