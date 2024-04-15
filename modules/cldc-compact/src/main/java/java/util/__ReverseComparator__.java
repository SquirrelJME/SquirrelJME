// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Reverses the operation of a given {@link Comparator}, used by
 * {@link Collections#reverseOrder(Comparator)}.
 *
 * @param <T> The type to compare.
 * @see Collections#reverseOrder(Comparator) 
 * @since 2022/07/29
 */
final class __ReverseComparator__<T>
	implements Comparator<T>
{
	/** The comparator to reverse. */
	final Comparator<T> _comparator;
	
	/**
	 * Initializes the reverse comparator.
	 * 
	 * @param __comp The comparator to reverse.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/07/29
	 */
	__ReverseComparator__(Comparator<T> __comp)
		throws NullPointerException
	{
		if (__comp == null)
			throw new NullPointerException("NARG");
		
		this._comparator = __comp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/29
	 */
	@Override
	public int compare(T __a, T __b)
	{
		// Negation makes it so earlier items are later, equals stay the same
		return -this._comparator.compare(__a, __b);
	}
}
