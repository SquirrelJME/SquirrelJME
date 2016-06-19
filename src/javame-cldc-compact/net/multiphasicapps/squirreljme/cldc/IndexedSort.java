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
public final class IndexedSort
{
	/**
	 * Not used.
	 *
	 * @since 2016/06/18
	 */
	private IndexedSort()
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
		IndexedComparator<Q> __comp)
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
		int n = __to - __from;
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
		for (int i = 0, j = __from; i < n; i++, j++)
			rv[i] = j;
		
		// Calculate the stack size, the stack stores the ranges
		int maxstack = ((Integer.numberOfTrailingZeros(
			Integer.highestOneBit(n)) + 2)) * 2;
		int[] stack = new int[maxstack];
		int at = 0;
		
		// Start at input list size
		stack[at++] = 0;
		stack[at++] = n;
		
		// Temporary storage
		int[] store = null;
		try
		{
			/*store = new int[n];*/
		}
		
		// No room for the second array, use insertion sort when merging down
		// instead. Technically since this uses a temporary int array with
		// indices this uses potentially triple the memory.
		catch (OutOfMemoryError e)
		{
		}
		
		// Keep merging values
		for (; at > 0;)
		{
			// Get start and end
			int rss = stack[at - 2],
				ree = stack[at - 1];
			
			// Merge up or switch to right side?
			if (ree < 0)
			{
				// Invert to get normal value
				ree = -ree;
				
				// Nothing left to merge
				if (at < 4)
					break;
				
				// Get the previous range
				int pss = stack[at - 4],
					pee = stack[at - 3];
				
				// Switch to the right side
				if (ree != pee)
				{
					stack[at - 2] = ree;
					stack[at - 1] = pee;
				}
				
				// Merge
				else
				{
					// Determine the entire breadth size, use temporary storage
					int bn = pee - pss;
					
					// Go through both sides
					boolean hasl;
					for (int ll = pss, rr = rss, out = 0;
						(hasl = (ll < rss)) || rr < pee;)
					{
						// Has right?
						boolean hasr = (rr < pee);
						int lx, rx, comp;
						
						// Has both sides
						if (hasl && hasr)
						{
							// Get the low and high values
							lx = rv[ll];
							rx = rv[rr];
						
							// Compare them
							comp = __comp.compare(__q, lx, rx);
						}
						
						// Has only left
						else if (hasl)
						{
							lx = rx = rv[ll];
							comp = -1;
						}
						
						// Has only right
						else
						{
							lx = rx = rv[rr];
							comp = 1;
						}	
						
						// If the left is lower (or the same), insert that
						int use;
						if (comp <= 0)
							use = ll++;
						else
							use = rr++;
						
						// Get the value to insert
						int val = rv[use];
						
						// Using storage?
						if (store != null)
							store[out++] = val;
						
						// Otherwise use something similar to insertion sort
						else
						{
							throw new Error("TODO");
						}
					}
					
					// If using double memory, reinster
					if (store != null)
					{
						// Replace values
						for (int i = 0, out = pss; i < bn; i++, out++)
							rv[out] = store[i];
					
						// Pop from the stack
						at -= 2;
					
						// Switch or merge up the one above this
						if (at > 0)
							stack[at - 1] = -stack[at - 1];
					}
				}
			}
			
			// Otherwise split down.
			else
			{
				// Get the length of this range
				int len = (ree - rss);
				
				// Merge up?
				if (len == 1)
					stack[at - 1] = -stack[at - 1];
				
				// Split
				else
				{
					stack[at++] = rss;
					stack[at++] = rss + (len >>> 1);
				}
			}
		}
		
		// Return the sorted result
		return rv;
	}
}

