// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.cldc;

/**
 * This is the the comparator which is used for comparing two values
 * by their index.
 *
 * @param <Q> The original data, may be an array or collection.
 * @since 2016/06/18
 */
public interface IndexedComparator<Q>
{
	/**
	 * Compares to values based on their index number.
	 *
	 * @param __q The potential data source.
	 * @param __a The first index.
	 * @param __b The second index.
	 * @return The comparison index.
	 * @since 2016/06/18
	 */
	public abstract int compare(Q __q, int __a, int __b);
}

