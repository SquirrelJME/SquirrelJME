// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

/**
 * This represents the type of library or application that the project is, this
 * determines how it is used and depended upon.
 *
 * @since 2017/10/31
 */
public enum ProjectType
{
	/** APIs which implement configurations, profiles, and standards. */
	API,
	
	/** Liblets which are only included by midlets and APIs. */
	LIBLET,
	
	/** Midlets which are actual applications. */
	MIDLET,
	
	/** End. */
	;
	
	/**
	 * Returns the type of project that should be returned.
	 *
	 * @param __s The string to get the project type for.
	 * @return The project type or {@code null} if it is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public static ProjectType ofString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s)
		{
			case "api":		return API;
			case "liblet":	return LIBLET;
			case "midlet":	return MIDLET;
			
			default:
				return null;
		}
	}
}

