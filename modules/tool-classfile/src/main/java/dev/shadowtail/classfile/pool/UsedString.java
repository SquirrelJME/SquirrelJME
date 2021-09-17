// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * Represents a string which was used.
 *
 * @since 2019/04/27
 */
public final class UsedString
{
	/** The used string. */
	public final String string;
	
	/**
	 * Initializes the used string.
	 *
	 * @param __s The string to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/27
	 */
	public UsedString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.string = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/27
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof UsedString))
			return false;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		return this.string.equals(((UsedString)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/27
	 */
	@Override
	public final int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/27
	 */
	@Override
	public final String toString()
	{
		return this.string;
	}
}

