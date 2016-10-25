// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

/**
 * This represents the type of project that a project is.
 *
 * @since 2016/09/18
 */
public enum ProjectType
{
	/** Defines and/or an API. */
	API("api"),
	
	/** Inlcuded by midlets or liblets as dependencies. */
	LIBLET("liblet"),
	
	/** Is executable by the user. */
	MIDLET("midlet"),
	
	/** End. */
	;
	
	/** The string representation of this type. */
	protected final String string;
	
	/**
	 * Initializes the project type.
	 *
	 * @param __s The string representing it.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/25
	 */
	private ProjectType(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/25
	 */
	@Override
	public final String toString()
	{
		return this.string;
	}
	
	/**
	 * Returns the project type from the specified string.
	 *
	 * @param __s The string to get the project type from.
	 * @return The project type for the given string.
	 * @throws IllegalArgumentException If the string does not refer to any
	 * kind of project type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/25
	 */
	public static ProjectType of(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__s)
		{
			case "api": return API;
			case "midlet": return MIDLET;
			case "liblet": return LIBLET;
			
				// {@squirreljme.error CI0h Unknown input string to get
				// project type of. (The input string)}
			default:
				throw new IllegalArgumentException(String.format("CI0h %s",
					__s));
		}
	}
}

