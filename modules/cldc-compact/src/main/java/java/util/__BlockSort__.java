// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This class provides the block sort algorithm used to sort items.
 *
 * https://en.wikipedia.org/wiki/Block_sort
 *
 * @since 2018/10/26
 */
final class __BlockSort__
{
	/**
	 * Sorts the given list using block sort.
	 *
	 * @param <T> The type to use.
	 * @param __a The list to sort.
	 * @param __from From index.
	 * @param __to To index.
	 * @param __comp The comparator to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/10/16
	 */
	public static <T> void sort(List<T> __a, int __from, int __to,
		Comparator<? super T> __comp)
		throws NullPointerException
	{
		if (__a == null || __comp == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

