// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jit.objectfile;

/**
 * This represents a symbol which is contained within the object file and
 * what it exports.
 *
 * @since 2018/02/23
 */
public final class ExportedSymbol
	extends Symbol
{
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/23
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * This represents the scope of the exported symbol.
	 *
	 * @since 2018/02/23
	 */
	public static enum Scope
	{
		/** Globally visible, the same object file also sees this. */
		GLOBAL,
		
		/** Visible only to the same object file. */
		STATIC,
		
		/** End. */
		;
	}
}

