// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

/**
 * This represents a file name that represents a string, it is used for
 * location awareness.
 *
 * @since 2018/05/05
 */
public final class StringFileName
	implements FileName
{
	/** The file name that is used. */
	protected final String filename;
	
	/**
	 * Initializes the string file name.
	 *
	 * @param __fn The filename to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/05/05
	 */
	public StringFileName(String __fn)
		throws NullPointerException
	{
		if (__fn == null)
			throw new NullPointerException("NARG");
		
		this.filename = __fn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final int hashCode()
	{
		return this.filename.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final String fileName()
	{
		return this.filename;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/05/05
	 */
	@Override
	public final String toString()
	{
		return this.filename;
	}
}

