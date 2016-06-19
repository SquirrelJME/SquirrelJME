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
		int[] store = new int[n];
		
		// Keep merging values
		for (; at > 0;)
		{
			// Get start and end
			int rss = stack[at - 2],
				ree = stack[at - 1];
			
			System.err.printf("DEBUG -- Range %3d - %3d (%3d)%n", rss, ree,
				ree - rss);
			
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
						if (comp <= 0)
						{
							store[out++] = lx;
							ll++;
						}
						
						// Otherwise use the right
						else
						{
							store[out++] = rx;
							rr++;
						}
					}
					
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
		
		/*
		// The first entry in the stack is the fully sorted list
		stack[at++] = 0;
		stack[at++] = n;
		
		// The second entry is the left side of that sort
		stack[at++] = 0;
		stack[at++] = n >>> 1;
		
		// Perform an in place merge sort starting at the top region
		// If the stack ever gets only a single start/end pair then the
		// sort operation is complete.
		boolean mergeup = false;
		boolean maybemerge = false;
		for (; at >= 2;)
		{
			// Get the stack region before this one
			boolean lim = (at >= 4);
			int befs = (lim ? stack[at - 4] : 0),
				befe = (lim ? stack[at - 3] : n);
			int befn = befe - befs;
			
			// Get the stack region that is currently being looked at
			int nows = stack[at - 2],
				nowe = stack[at - 1];
			int nown = nowe - nows;
			
			// Maybe merge up?
			if (maybemerge)
			{
				// If the end is at the before end, merge up more
				if (nowe == befe)
					mergeup = true;
				
				// Otherwise switch to the right side
				else
				{
					stack[at - 2] = nowe;
					stack[at - 1] = befe;
				}
				
				// If not merging, continue
				maybemerge = false;
				if (!mergeup)
					continue;
			}
			
			// Perform a merge
			if (mergeup)
			{
				// Get the left side
				int lefs = befs,
					lefe = nows,
					lefn = lefe - lefs;
				
				// And the right side
				int rigs = nows,
					rige = nowe,
					rign = nown;
				
				// The full merge range
				int fuls = lefs,
					fule = rige,
					fuln = lefn + rign;
				
				// Perform the merge
				boolean hasl, hasr;
				for (int out = fuls, lldx = lefs, rrdx = rigs;
					(hasl = (lldx < lefe)) && (hasr = (rrdx < rige));)
				{
					// The index to place next
					int insert;
					
					// Has left side?
					if (hasl)
					{
						// Has right side also
						if (hasr)
						{
							// Compare the left, the right, and the output
							int odx = rv[out],
								adx = rv[lldx],
								bdx = rv[rrdx];
							
							// Compare sides against the output
							boolean lowl = (__comp.compare(__q, odx, adx) > 0),
								lowr = (__comp.compare(__q, odx, bdx) > 0);
							
							// Both are low? use the lower side
							if (lowl && lowr)
							{
								// Left side is lower?
								if (__comp.compare(__q, adx, bdx) < 0)
									insert = lldx++;
						
								// Right is lower (or equal)
								else
									insert = rrdx++;
							}
							
							// Only the left is lower
							else if (lowl)
								insert = lldx++;
							
							// Only the right is lower
							else if (lowr)
								insert = rrdx++;
							
							// Neither are lower
							else
								insert = (hasl ? lldx++ : rrdx++);
						}
						
						// Only left
						else
							insert = lldx++;
					}
					
					// Only has right side
					else
						insert = rrdx++;
					
					// Insert insertion index down
					boolean shift = false;
					int ival = rv[out];
					for (int z = fule - 1; z > out; z--)
					{
						// Is this the the insertion index?
						if (z == insert)
						{
							ival = rv[z];
							shift = true;
						}
						
						// Shift the value before this one up
						if (shift)
							rv[z] = rv[z - 1];
					}
					
					// Set the output value
					rv[out] = ival;
					
					*//*// Remember the swap point index
					int temp = rv[out];
					rv[out] = rv[insert];
					rv[insert] = temp;*//*
				}
				
				// Remove stack entry
				at -= 2;
				
				// Maybe merge again, possibly
				mergeup = false;
				maybemerge = true;
				continue;
			}
			
			// Down to two entries? Sort them, either merge up if this is
			// the trailing end or decend into the second side if this is not.
			// Also a single entry, if odd
			if (nown <= 2)
			{
				// Only sort for two entries, this can happen if the
				// input array is of an odd size
				if (nown == 2)
				{
					// Get the left and right indexes
					int ndx = nows + 1;
					int left = rv[nows],
						right = rv[ndx];
					
					// Compare them
					int comp = __comp.compare(__q, left, right);
					
					// If the one on the right is lower, switch them
					if (comp > 0)
					{
						int was = rv[nows];
						rv[nows] = rv[ndx];
						rv[ndx] = was;
					}
				}
				
				// Merge up?
				if (befe == nowe)
					mergeup = true;
				
				// Sort the right side
				else
				{
					stack[at - 2] = nowe;
					stack[at - 1] = befe;
				}
			}
			
			// Otherwise, descend the left side
			else
			{
				stack[at++] = nows;
				stack[at++] = nows + (nown >>> 1);
			}
		}*/
		
		// Return the sorted result
		return rv;
	}
}

