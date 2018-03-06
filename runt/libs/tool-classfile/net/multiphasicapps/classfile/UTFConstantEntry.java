// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This represents a UTF-8 constant pool entry.
 *
 * @since 2017/06/09
 */
public final class UTFConstantEntry
{
	/** The string representation. */
	protected final String string;
	
	/**
	 * Initializes the constant entry.
	 *
	 * @param __s The string used.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/09
	 */
	public UTFConstantEntry(String __s)
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
	 * @since 2017/06/09
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof UTFConstantEntry))
			return false;
		
		return this.string.equals(((UTFConstantEntry)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/09
	 */
	@Override
	public int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/09
	 */
	@Override
	public String toString()
	{
		return this.string;
	}
}

