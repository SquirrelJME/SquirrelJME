// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.descriptors;

/**
 * This is the base class for all symbols.
 *
 * @since 2016/03/14
 */
abstract class __BaseSymbol__
	implements CharSequence
{
	/** The internal string. */
	protected final String string;	
	
	/**
	 * Initializes the base symbol.
	 *
	 * @param __s The string to use which contains the symbol information.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/14
	 */
	__BaseSymbol__(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		string = __s;
		
		// Cannot be blank
		int n = length();
		if (n <= 0)
			throw new IllegalSymbolException("DS01");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public final char charAt(int __i)
	{
		return string.charAt(__i);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Must be of the same type
		if (!getClass().isInstance(__o) || !(__o instanceof __BaseSymbol__))
			return false;
		
		// Check the string
		return string.equals(((__BaseSymbol__)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public final int hashCode()
	{
		return string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public final int length()
	{
		return string.length();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public final CharSequence subSequence(int __s, int __e)
	{
		return string.subSequence(__s, __e);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/14
	 */
	@Override
	public final String toString()
	{
		return string;
	}
}

