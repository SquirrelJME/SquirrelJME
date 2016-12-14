// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

/**
 * This is the base class for API name and version representations.
 *
 * APIs are not case sensitive.
 *
 * @since 2016/12/14
 */
public abstract class API
{
	/**
	 * Initializes the constant in name and version form.
	 *
	 * @param __n
	 * @throws IllegalArgumentException If the name and version form is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	API(String __n)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Initializes the constant using the given name and version.
	 *
	 * @param __n The API name.
	 * @param __v The API version.
	 * @throws IllegalArgumentException If the arguments are not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	API(String __n, MidletVersion __v)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__n == null || __v == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

