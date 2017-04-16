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
 * This class is mutable and will change as the JIT progresses, although the
 * individual states are immutable.
 *
 * This class is not thread safe.
 *
 * @see SnapshotCacheState
 * @since 2017/02/16
 */
public final class SnapshotCacheStates
{
	/** The code stream being used. */
	protected final __JITCodeStream__ _codestream;
	
	/** The address where the cache states are valid. */
	private final List<Integer> _pos =
		new ArrayList<>();
	
	/** The individual cache states. */
	private final List<SnapshotCacheState> _states =
		new ArrayList<>();
	
	/**
	 * Initializes the cache states.
	 *
	 * @param __cs The code stream being used.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/16
	 */
	SnapshotCacheStates(__JITCodeStream__ __cs)
		throws NullPointerException
	{
		// Check
		if (__cs == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._codestream = __cs;
	}
	
	/**
	 * Checks whether the given state exists or not for the given address.
	 *
	 * @parma __i The address to check.
	 * @return {@code true} if a state exists at the specified point.
	 * @since 2017/02/23
	 */
	public boolean contains(int __i)
	{
		return get(__i) != null;
	}
	
	/**
	 * This returns an already existing state for the specified address.
	 *
	 * @parma __i The address to get the state of.
	 * @return The state at the given address or {@code null} if there is not
	 * set state.
	 * @since 2017/02/16
	 */
	public SnapshotCacheState get(int __i)
	{
		// Find it
		int dx = Collections.<Integer>binarySearch(this._pos, __i);
		
		// Not found
		if (dx < 0)
			return null;
		
		// Get it
		return this._states.get(dx);
	}
	
	/**
	 * Sets the cache state for a given address, each address may only be set
	 * once.
	 *
	 * @param __i The address to set.
	 * @param __v The state to set, all information is copied and made
	 * immutable.
	 * @throws JITException If a state already exists for the given address.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/18
	 */
	public void set(int __i, CacheState __v)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		List<Integer> pos = this._pos;
		List<SnapshotCacheState> states = this._states;
		
		// Find it
		int dx = Collections.<Integer>binarySearch(this._pos, __i);
		
		// Not found, insert it
		if (dx < 0)
		{
			dx = (-(dx) - 1);
			pos.add(dx, __i);
			states.add(dx, new SnapshotCacheState(this._codestream, __v));
		}
		
		// {@squirreljme.error AQ1r A cache state already exists at the
		// specified address. (The address)}
		else
			throw new JITException(String.format("AQ1r %d", __i));
	}
}

