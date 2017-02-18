// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This contains multiple cached states for specific spots within the method.
 *
 * This class is mutable and will change as the JIT progresses.
 *
 * This class is not thread safe.
 *
 * @see CacheState
 * @since 2017/02/16
 */
public class CacheStates
{
	/** Number of stack entries. */
	protected final int maxstack;
	
	/** Number of locals. */
	protected final int maxlocals;
	
	/** The address where the cache states are valid. */
	private final List<Integer> _pos =
		new ArrayList<>();
	
	/** The individual cache states. */
	private final List<CacheState> _states =
		new ArrayList<>();
	
	/**
	 * Initializes the cache states.
	 *
	 * @param __ms The number of stack entries.
	 * @param __ml The number of local entries.
	 * @since 2017/02/16
	 */
	CacheStates(int __ms, int __ml)
	{
		// Set
		this.maxstack = __ms;
		this.maxlocals = __ml;
	}
	
	/**
	 * Potentially creates the cache state at the given index if it does not
	 * exist, otherwise it will return the already existing state.
	 *
	 * @param __i The address to get the state of or to initialize.
	 * @return The state of the cache at this address, which may be newly
	 * intiialized.
	 * @since 2017/02/16 
	 */
	public CacheState create(int __i)
	{
		List<Integer> pos = this._pos;
		List<CacheState> states = this._states;
		
		// Find it
		int dx = Collections.<Integer>binarySearch(this._pos, __i);
		
		// Not found, create
		if (dx < 0)
		{
			dx = (-(dx) - 1);
			pos.add(dx, __i);
			CacheState rv;
			states.add(dx,
				(rv = new CacheState(this.maxstack, this.maxlocals)));
			return rv; 
		}
		
		// Use it
		else
			return states.get(dx);
	}
	
	/**
	 * This returns an already existing state for the specified address.
	 *
	 * @parma __i The address to get the state of.
	 * @return The state at the given address or {@code null} if there is not
	 * set state.
	 * @since 2017/02/16
	 */
	public CacheState get(int __i)
	{
		// Find it
		int dx = Collections.<Integer>binarySearch(this._pos, __i);
		
		// Not found
		if (dx < 0)
			return null;
		
		// Get it
		return this._states.get(dx);
	}
}

