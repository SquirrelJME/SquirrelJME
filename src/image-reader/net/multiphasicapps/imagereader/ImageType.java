// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.imagereader;

/**
 * The represents how the data in an image is stored.
 *
 * @since 2016/05/22
 */
public enum ImageType
{
	/** 8-bit channel alpha, red, green, and blue image stored as int. */
	INT_ARGB(int.class)
	{
		/**
		 * {@inheritDoc}
		 * @since 2016/05/23
		 */
		@Override
		int __atARGB(Object __buf, int __dx)
		{
			return ((int[])__buf)[__dx];
		}
	},
	
	/** End. */
	;
	
	/** The primitive image type. */
	final Class<?> _prim;
	
	/**
	 * Initializes the image type with the expected primitive type.
	 *
	 * @param __cl The expected primitive type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/23
	 */
	private ImageType(Class<?> __cl)
		throws NullPointerException
	{
		// Check
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// The primitive type
		_prim = __cl;
	}
	
	/**
	 * Returns the linear ARGB value in the given buffer at the specified
	 * index.
	 *
	 * @param __buf The buffer to read from.
	 * @param __dx The linear pixel to read.
	 * @return The linear ARGB value.
	 * @since 2016/05/23
	 */
	abstract int __atARGB(Object __buf, int __dx);
}

