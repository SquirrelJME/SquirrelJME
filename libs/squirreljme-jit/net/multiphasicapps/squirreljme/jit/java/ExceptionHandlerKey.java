// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This acts as a key for exception handlers for basic blocks for exception
 * handlers which are unique for a given address. Effectively this class
 * allows for exceptions to be expanded so that any exceptions in the code are
 * treated for the most part just like normal code.
 *
 * @since 2017/08/08
 */
public final class ExceptionHandlerKey
	implements BasicBlockKey
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/07
	 */
	@Override
	public boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/07
	 */
	@Override
	public int hashCode()
	{
		throw new todo.TODO();
	}
}

