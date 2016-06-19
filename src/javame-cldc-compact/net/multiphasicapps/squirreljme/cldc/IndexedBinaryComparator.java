// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.cldc;

/**
 * This is the comparator which is ued
 *
 * @param <Q> The array or other indexed type.
 * @param <E> The type of element to compare against.
 * @since 2016/06/19
 */
public interface IndexedBinaryComparator<Q, E>
{
	/**
	 * Compares the given entry with the given index which is used in a binary
	 * search.
	 *
	 * @param __q The source input elements.
	 * @param __a The element to compare against.
	 * @param __b The index to compare against.
	 * @return The result of the comparison.
	 * @since 2016/06/19
	 */
	public abstract int binaryCompare(Q __q, E __a, int __b);
}

