// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

import net.multiphasicapps.squirreljme.nativecode.NativeAllocation;

/**
 * This represents the state of variables.
 *
 * @since 2016/09/03
 */
final class __VarStates__
{
	/** Are these local variables? */
	protected final boolean arelocals;
	
	/** The current set of allocations. */
	final NativeAllocation[] _allocs;
	
	/**
	 * Initializes the variable states.
	 *
	 * @param __n The number of states to store.
	 * @param __lv Are these variables local.
	 * @since 2016/09/03
	 */
	__VarStates__(int __n, boolean __lv)
	{
		this._allocs = new NativeAllocation[__n];
		
		// Set
		this.arelocals = __lv;
	}
	
	/**
	 * Copy constructs the variable states.
	 *
	 * @param __vs The source states to clone from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	__VarStates__(__VarStates__ __vs)
		throws NullPointerException
	{
		// Check
		if (__vs == null)
			throw new NullPointerException("NARG");
		
		// Just clone
		this.arelocals = __vs.arelocals;
		this._allocs = __vs._allocs.clone();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public String toString()
	{
		// Setup
		StringBuilder sb = new StringBuilder("[");
		
		// Get
		NativeAllocation[] allocs = this._allocs;
		
		// Go through int
		int n = allocs.length;
		for (int i = 0; i < n; i++)
		{
			// Comma?
			if (i > 0)
				sb.append(", ");	
			
			// Add alloctions
			sb.append(allocs[i]);
		}
		
		// Build
		sb.append(']');
		return sb.toString();
	}
}

