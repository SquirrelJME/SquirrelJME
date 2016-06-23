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

/**
 * This is the base class for looking up resources which contain the mascot.
 *
 * Resources are looked up on the class of the service.
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
	
	/** Does size matter? */
	protected final boolean usesize;
	
	/**
	 * This initializes the base resource lookup.
	 *
	 * @param __type The data type of the input image.
	 * @param __sm If {@code true} then the size of the image is used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/16
	 */
	public MascotResources(String __type, boolean __sm)
		throws NullPointerException
	{
		// Check
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.type = __type;
		this.usesize = __sm;
	}
	
	/**
	 * Builds the name that the resource would use.
	 *
	 * @param __head Load the head image?
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __de The encoding of the image.
	 * @return The file name to lookup.
	 * @since 2016/06/16
	 */
	private final String buildName(boolean __head, int __w, int __h,
		DataEncoding __de)
		throws NullPointerException
	{
		// Check
		if (__de == null)
			throw new NullPointerException("NARG");
		
		// Head or body?
		String base = (__head ? "head" : "body");
		String type = this.type;
		
		// Does size matter?
		if (this.usesize)
			return String.format("%s_%d%d.%s%s", base, type, __w, __h, __de);
		
		// Does not matter
		else
			return String.format("%s.%s%s", base, type, __de);
	}
	
	/**
	 * Loads an image with the specified type, width, and height.
	 *
	 * @param __type The type of image to find.
	 * @param __head If {@code true} then the image to find is the head rather
	 * than the body of the mascot. The head may be used for a program icon
	 * while the body may be used for more general display art.
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @return The input stream of the image data or {@code null} if not found.
	 * @since 2016/06/16
	 */
	public static InputStream loadImage(String __type, boolean __head, int __w,
		int __h)
	{
		// Get all encoding types
		DataEncoding[] des = DataEncoding.values();
		
		// Lock on services
		ServiceLoader<MascotResources> svl = _SERVICES;
		synchronized (svl)
		{
			// Go through all services and handle the same type
			for (MascotResources mr : svl)
				if (mr.type.equals(__type))
				{
					// Use the class since relative names are used
					Class<?> cl = mr.getClass();
					
					// Go through all possible encodings
					for (DataEncoding de : des)
					{
						// Build name to find
						String name = mr.buildName(__head, __w, __h, de);
						
						// Lookup resource
						InputStream rv = cl.getResourceAsStream(name);
						
						// If found, decode it
						if (rv != null)
							return de.decode(rv);
					}
				}
		}
		
		// Not found
		return null;
	}
}

