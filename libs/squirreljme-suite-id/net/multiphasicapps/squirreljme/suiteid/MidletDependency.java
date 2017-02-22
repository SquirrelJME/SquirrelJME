// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.suiteid;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Objects;

/**
 * This represents a dependency that a LIBlet or MIDlet may depend on.
 *
 * @since 2017/02/22
 */
public final class MidletDependency
{
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the dependency which is parsed from the given input string.
	 *
	 * @param __s The string to parse.
	 * @throws IllegalArgumentException If the string is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/22
	 */
	public MidletDependency(String __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Trim whitespace
		__s = __s.trim();
		
		// Extract all semicolon positions
		int[] sc = new int[4];
		for (int i = 0; i < 4; i++)
		{
			int lastpos = (i == 0 ? 0 : sc[i - 1] + 1);
			
			// {@squirreljme.error CC0e Expected four semi-colons in the
			// dependency field. (The input dependency)}
			int com = __s.indexOf(';', lastpos);
			if (com < 0)
				throw new IllegalArgumentException(String.format(
					"CC0e %s", __s));
			
			// Store
			sc[i] = com;
		}
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof MidletDependency))
			return false;
		
		// Compare
		MidletDependency o = (MidletDependency)__o;
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public int hashCode()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/22
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
}

