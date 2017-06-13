// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents an identifier which acts as a name of a fragment of a class
 * or a member of a class.
 *
 * @since 2017/06/12
 */
public final class Identifier
{
	/** The string which makes up the identifier. */
	protected final String string;
	
	/**
	 * Initializes the identifier.
	 *
	 * @param __n The input identifier to decode.
	 * @throws JITException If it is not a valid identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/12
	 */
	public Identifier(String __n)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.string = __n;
		
		// Check characters
		for (int i = 0, n = __n.length(); i < n; i++)
		{
			char c = __n.charAt(i);
			
			// {@squirreljme.error JI0i The specified identifier contains an
			// invalid character. (The identifier)}
			if (c == '.' || c == ';' || c == '[' || c == '/')
				throw new JITException(String.format("JI0i %s", __n));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof Identifier))
			return false;
		
		return this.string.equals(((Identifier)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/12
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

