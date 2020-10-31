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
}
