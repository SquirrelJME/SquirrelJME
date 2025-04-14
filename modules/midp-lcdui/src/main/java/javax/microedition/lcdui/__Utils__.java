// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Common utilities.
 *
 * @since 2020/10/31
 */
@Deprecated
final class __Utils__
{
	/**
	 * Not used.
	 * 
	 * @since 2020/10/31
	 */
	@Deprecated
	private __Utils__()
	{
	}
	
	/**
	 * Calculates the flags to use for the selected indexes.
	 * 
	 * @param __c The choice to set in.
	 * @param __type The type of list being set.
	 * @param __i The index being set.
	 * @param __e The state of this flag.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/15
	 */
	@Deprecated
	public static boolean[] __calculateSetSelectedIndexFlags(Choice __c,
		int __type, int __i, boolean __e)
		throws IndexOutOfBoundsException, NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Not within the list bounds?
		int n = __c.size();
		if (__i < 0 || __i >= n)
			throw new IndexOutOfBoundsException("IOOB");
		
		// Get current list selection flags
		boolean[] flags = new boolean[n];
		__c.getSelectedFlags(flags);
		
		// If multiple choice, we can set the field directly
		if (__type == Choice.MULTIPLE)
			flags[__i] = __e;
		
		// If we are deselecting something, this will effectively do nothing
		// unless we are deselecting the item that is currently selected
		else if (!__e)
		{
			// If this item is selected, deselect it and just select the first
			// item
			if (flags[__i])
			{
				flags[__i] = false;
				flags[0] = true;
			}
		}
		
		// Otherwise, only a single item becomes selected
		else
		{
			for (int i = 0; i < n; i++)
				flags[i] = (i == __i);
		}
		
		// Return the resultant flags to use
		return flags;
	}
	
	/**
	 * Gets all of the items which have been selected and stores them into
	 * the boolean array.
	 * 
	 * @param __c The choice to look within.
	 * @param __result The resultant boolean array, if the array is longer
	 * then the extra elements will be set to {@code false}.
	 * @return The number of selected elements.
	 * @throws IllegalArgumentException If the array is shorter than the
	 * number of items in the list.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	@Deprecated
	public static int __getSelectedFlags(Choice __c, boolean[] __result)
		throws IllegalArgumentException, NullPointerException
	{
		if (__c == null || __result == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error EB3b Invalid flag array length. (The length)} */
		int outLen = __result.length;
		int listLen = __c.size();
		if (outLen < listLen)
			throw new IllegalArgumentException("EB3b " + listLen);
		
		// Determine which items are selected
		int count = 0;
		for (int i = 0; i < listLen; i++)
		{
			boolean isSel = __c.isSelected(i);
			__result[i] = isSel;
			
			if (isSel)
				count++;
		}
		
		// Anything following the end is wiped out
		for (int i = listLen; i < outLen; i++)
			__result[i] = false;
		
		return count;
	}
	
	/**
	 * Calculates the selected index.
	 * 
	 * @param __c The choice to look within.
	 * @param __type The type of choice this is.
	 * @return The selected index.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/31
	 */
	@Deprecated
	static int __getSelectedIndex(Choice __c, int  __type)
		throws NullPointerException
	{
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Multiple choices are always unknown
		if (__type == Choice.MULTIPLE)
			return -1;
		
		// If the list is empty, nothing can ever be selected
		int n = __c.size();
		if (n <= 0)
			return -1;
		
		// We can get all the flags used, if nothing is selected we can just
		// stop without checking the elements at all
		boolean[] flags = new boolean[n];
		int numSel = __Utils__.__getSelectedFlags(__c, flags);
		
		// Find the first (and only) selected index
		if (numSel == 1)
			for (int i = 0; i < n; i++)
				if (flags[i])
					return i;
		
		// This should not occur
		throw Debugging.oops(numSel);
	}
	
	/**
	 * Sets the selected index.
	 * 
	 * @param __c The choice to set in.
	 * @param __type The type of list being set.
	 * @param __i The index being set.
	 * @param __e The state of this flag.
	 * @throws IndexOutOfBoundsException If the index is not within bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/14
	 */
	@Deprecated
	public static void __setSelectedIndex(Choice __c, int __type, int __i,
		boolean __e)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Set new flag state
		__c.setSelectedFlags(__Utils__.__calculateSetSelectedIndexFlags(
			__c, __type, __i, __e));
	}
}
