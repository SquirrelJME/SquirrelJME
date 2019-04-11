// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents an operation that needs to be performed during stack
 * transitions.
 *
 * @since 2019/04/11
 */
public final class StateOperation
{
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final int hashCode()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/04/11
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Represents the type of operation to perform.
	 *
	 * @since 2019/04/11
	 */
	public static enum Type
	{
		/** Count. */
		COUNT,
		
		/** Un-count. */
		UNCOUNT,
		
		/** Copy. */
		COPY,
		
		/** Wide copy. */
		WIDE_COPY,
		
		/** End. */
		;
	}
}

