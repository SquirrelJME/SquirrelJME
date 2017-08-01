// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

/**
 * This represents the type of the dependency that is to be included.
 *
 * @since 2017/02/22
 */
public enum MidletDependencyType
{
	/** Liblet. */
	LIBLET,
	
	/** Standard. */
	STANDARD,
	
	/** Service. */
	SERVICE,
	
	/** Proprietary. */
	PROPRIETARY,
	
	/** End. */
	;
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public String toString()
	{
		// Convert string
		switch (this)
		{
			case LIBLET:		return "liblet";
			case STANDARD:		return "standard";
			case SERVICE:		return "service";
			case PROPRIETARY:	return "proprietary";
			default:
				throw new RuntimeException("OOPS");
		}
	}
	
	/**
	 * Returns the dependency type based on the input string.
	 *
	 * @param __s The input string to parse.
	 * @return The dependency type for the given string.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public static MidletDependencyType of(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s.trim())
		{
			case "liblet":		return LIBLET;
			case "standard":	return STANDARD;
			case "service":		return SERVICE;
			case "proprietary":	return PROPRIETARY;
			
				// Should not happen
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

