// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

/**
 * Noted string is similar to used string however it means it is just used
 * by its pointer data and not an actual string object which is initialized
 * for it. The noted string is generally used for debug purposes.
 *
 * @since 2019/09/11
 */
public final class NotedString
{
	/** The noted string. */
	public final String string;
	
	/**
	 * Initializes the noted string.
	 *
	 * @param __s The string to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/09/11
	 */
	public NotedString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.string = __s;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof NotedString))
			return false;
		
		if (this.hashCode() != __o.hashCode())
			return false;
		
		return this.string.equals(((NotedString)__o).string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/11
	 */
	@Override
	public final int hashCode()
	{
		return this.string.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/09/11
	 */
	@Override
	public final String toString()
	{
		return this.string;
	}
}
