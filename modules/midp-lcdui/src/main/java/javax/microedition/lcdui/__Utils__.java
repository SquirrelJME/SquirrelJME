// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Common utilities.
 *
 * @since 2020/10/31
 */
final class __Utils__
{
	/**
	 * Not used.
	 * 
	 * @since 2020/10/31
	 */
	private __Utils__()
	{
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
		int numSel = __c.getSelectedFlags(flags);
		if (numSel <= 0)
			return -1;
		
		// This should not occur
		if (numSel > 1)
			throw Debugging.oops(numSel);
		
		// Find the first (and only) selected index
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
	public static void __setSelectedIndex(Choice __c, int __type, int __i,
		boolean __e)
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
		
		// Otherwise, only a single item becomes selected
		else if (n > 0)
		{
			// Update set of flags to use for the selections
			boolean hadSelection = false;
			for (int i = 0; i < n; i++)
			{
				hadSelection |= flags[__i];
				flags[__i] = (__e && i == __i);
			}
			
			// If we are deselecting something and we had a selection then
			// do nothing at all
			if (!__e && hadSelection)
				return;
		}
		
		// Set new flag state
		__c.setSelectedFlags(flags);
	}
}
