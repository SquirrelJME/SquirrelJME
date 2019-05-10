// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.RandomAccess;

/**
 * Shell sorting algorithm.
 *
 * @since 2019/05/10
 */
public class ShellSort
{
	/** Gaps used in shell sort. */
	static final int[] _GAPS =
		new int[]{701, 301, 132, 57, 23, 10, 4, 1};
	
	/**
	 * Sorts the specified collection.
	 *
	 * @param __a The collection to sort.
	 * @param __from The from index.
	 * @param __to The to index.
	 * @param __comp The comparator to use.
	 * @throws IndexOutOfBoundsException If the from or to index are
	 * outside of bounds.
	 * @throws IllegalArgumentException If the from address is greater than
	 * the to address.
	 * @throws NullPointerException If no collection was specified.
	 * @since 2019/05/09
	 */
	public static final <T> void sort(List<T> __a,
		int __from, int __to, Comparator<? super T> __comp)
		throws IndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		int an = __a.size();
		if (__from < 0 || __to > an)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		if (__from > __to)
			throw new IllegalArgumentException("IOOB");
		
		// Use natural comparator?
		if (__comp == null)
			__comp = NaturalComparator.<T>instance();
		
		// Pointless sort?
		int n = __to - __from;
		if (n == 0 || n == 1)
			return;
		
		// If the list is not random access, then there will be a great
		// penalty sorting it and this may result in quadratic performance loss
		if (false && !(__a instanceof RandomAccess))
		{
			// Setup duplicate
			List<T> dup = new ArrayList<>(n);
			
			// Copy values using source iterator (less CPU intensive)
			ListIterator<T> it = __a.listIterator(__from);
			for (int o = 0; o < n; o++)
				dup.set(o, it.next());
			
			// Sort this array
			ShellSort.<T>sort(dup, 0, n, __comp);
			
			// Our iterator should still be good, so we can go backwords
			// setting all the values in it
			for (int i = n - 1; i >= 0; i--)
			{
				it.set(dup.get(i));
				it.previous();
			}
			
			// Stop
			return;
		}
		
		// If only two values are being sorted, it is a simple swap check
		if (n == 2)
		{
			int ia = __from,
				ib = __from + 1;
				
			// Get both values
			T a = __a.get(ia),
				b = __a.get(ib);
			
			// If the second is lower than the first, we need to swap
			if (__comp.compare(b, a) < 0)
			{
				__a.set(ia, b);
				__a.set(ib, a);
			}
			
			// Nothing else needs to be done
			return;
		}
		
		// Work down from the highest gap to the lowest
		for (int gap : ShellSort._GAPS)
		{
			// Gapped insertion sort
			for (int i = gap; i < n; i++)
			{
				// Use this to make a hole
				T temp = __a.get(__from + i);
				
				// Shift earlier gap elements down
				int j;
				for (j = i; j >= gap && __comp.compare(
					__a.get(__from + (j - gap)), temp) > 0; j -= gap)
					__a.set(__from + j, __a.get(__from + (j - gap)));
				
				// Put in the correct position
				__a.set(__from + j, temp);
			}
		}
	}
}

