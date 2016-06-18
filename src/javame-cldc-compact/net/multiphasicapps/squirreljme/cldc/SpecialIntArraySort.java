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
 * This class contains a special method which implements an in place merge
 * sort algorithm except that it works on a special int array of indices
 * instead of the original array. This permits
 *
 * @since 2016/06/18
 */
public final class SpecialIntArraySort
{
	/**
	 * Not used.
	 *
	 * @since 2016/06/18
	 */
	private SpecialIntArraySort()
	{
	}
	
	/**
	 * Sorts the given input indices using a special comparator of an unknown
	 * type. The result is a set of indices of the sorted input which is in the
	 * index order of the data that is sorted.
	 *
	 * @param <Q> Original data passed to the 
	 * @param __q The original data to sort.
	 * @param __from The inclusive starting index.
	 * @param __to The exclusive ending index.
	 * @param __comp The special comparator for the input data.
	 * @return The array of indices in their sorted order, the indices in the
	 * array start from {@code __from} and end at {@code __to}.
	 * @throws IllegalArgumentException If the from and/or to index are
	 * negative, or the to index is before the from index.
	 * @throws NullPointerException If no comparator was specified.
	 * @since 2016/06/18
	 */
	public static <Q> int[] sort(Q __q, int __from, int __to,
		SpecialComparator<Q> __comp)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__comp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error The from or to index to sort is either negative
		// or the to index is before the from index.}
		if (__from < 0 || __to < 0 || __to < __from)
			throw new IllegalArgumentException("ZZ0r");
		
		// Not sorting anything?
		int n = __from - __to;
		if (n == 0)
			return new int[0];
		
		// Only sorting a single element
		else if (n == 1)
			return new int[]{__from};
		
		// Comparing only two elements
		else if (n == 2)
		{
			// Get indices
			int ax = __from,
				bx = __from + 1;
			
			// Compare them
			int comp = __comp.compare(__q, ax, bx);
			
			// A is higher?
			if (comp > 0)
				return new int[]{bx, ax};
			
			// Otherwise keep the same
			else
				return new int[]{ax, bx};
		}
		
		// Setup target array with sorted index entries
		int rv[] = new int[n];
		for (int i = 0, j = __from; i < n; i++)
			rv[i] = j++;
		
		// Calculate the stack size, the number of divisions that would be
		// used, The stack hold low and high values.
		// Need an extra stack entry for the starting point
		int maxstack = ((Integer.numberOfTrailingZeros(
			Integer.highestOneBit(n)) + 1) * 2;
		int[] stack = new int[maxstack];
		int at = 0;
		
		// The first entry in the stack is the fully sorted list
		stack[at++] = 0;
		stack[at++] = n;
		
		// The second entry is the left side of that sort
		stack[at++] = 0;
		stack[at++] = n >>> 1;
		
		// Perform an in place merge sort starting at the top region
		for (;;)
		{
			// Get the stack region before this one
			int befs = stack[at - 4],
				befe = stack[at - 3];
			
			// Get the stack region that is currently being looked at
			int nows = stack[at - 2],
				nowe = stack[at - 1];
			
			if (true)
				throw new Error("TODO");
		}
		
		// Return the sorted result
		return rv;
	}
	
	/**
	 * This is the the comparator which is used for comparing two values
	 * by their index.
	 *
	 * @param <Q> The original data, may be an array or collection.
	 * @since 2016/06/18
	 */
	public static interface SpecialComparator<Q>
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
}

