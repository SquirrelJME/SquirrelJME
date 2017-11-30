// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.midlet.id;

/**
 * This represents the name of a midlet suite.
 *
 * @since 2016/10/12
 */
public final class SuiteName
	implements Comparable<SuiteName>
{
	/** String value. */
	protected final String string;
	
	/**
	 * Initializes the suite name.
	 *
	 * @param __v The value to parse.
	 * @throws InvalidMidletException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/12
	 */
	public SuiteName(String __v)
		throws InvalidMidletException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// Trim whitespace
		__v = __v.trim();
		
		// Go through all characters
		int n = __v.length();
		for (int i = 0; i < n; i++)
		{
			char c = __v.charAt(i);
			
			// Invalid character?
			switch (c)
			{
					// {@squirreljme.error AD0d An illegal character was
					// specified in the midlet suite name. (The midlet suite
					// name)}
				case '\0':
				case '\r':
				case '\n':
				case ':':
				case ';':
					throw new InvalidMidletException(
						String.format("AD0d %s", __v));
				
					// Valid
				default:
					continue;
			}
		}
		
		// Set
		this.string = __v;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int compareTo(SuiteName __o)
	{
		if (this == __o)
			return 0;
		return this.string.compareTo(__o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		// Check
		if (!(__o instanceof SuiteName))
			return false;
		
		return this.string.equals(((SuiteName)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/12
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

