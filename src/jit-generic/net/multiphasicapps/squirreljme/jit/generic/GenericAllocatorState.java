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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Deque;

/**
 * This contains a state for the register allocator which is used in the
 * branch processor so that the state that has been previously set can be
 * matched when jumping to that location.
 *
 * This class is immutable.
 *
 * @since 2016/08/31
 */
public final class GenericAllocatorState
{
	/** Saved integer queue. */
	private final GenericRegister[] _savedintq;
	
	/** Saved float queue. */
	private final GenericRegister[] _savedfloatq;
	
	/** Temporary integer queue. */
	private final GenericRegister[] _tempintq;
	
	/** Temporary float queue. */
	private final GenericRegister[] _tempfloatq;
	
	/** Local variables. */
	private final __VarStates__ _jlocals;
	
	/** Stack variables. */
	private final __VarStates__ _jstack;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Snapshots the state of the allocator.
	 *
	 * @param __ga The allocator to snapshot from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	GenericAllocatorState(GenericAllocator __ga)
		throws NullPointerException
	{
		// Check
		if (__ga == null)
			throw new NullPointerException("NARG");
		
		// Get parts for copying
		Deque<GenericRegister> savedintq = __ga._savedintq;
		Deque<GenericRegister> savedfloatq = __ga._savedfloatq;
		Deque<GenericRegister> tempintq = __ga._tempintq;
		Deque<GenericRegister> tempfloatq = __ga._tempfloatq;
		
		// Make arrays
		this._savedintq = savedintq.<GenericRegister>toArray(
			new GenericRegister[savedintq.size()]);
		this._savedfloatq = savedfloatq.<GenericRegister>toArray(
			new GenericRegister[savedfloatq.size()]);
		this._tempintq = tempintq.<GenericRegister>toArray(
			new GenericRegister[tempintq.size()]);
		this._tempfloatq = tempfloatq.<GenericRegister>toArray(
			new GenericRegister[tempfloatq.size()]);
		
		// Copy variable states
		this._jlocals = new __VarStates__(__ga._jlocals);
		this._jstack = new __VarStates__(__ga._jstack);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache string?
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("{");
			
			// Add locals
			sb.append("locals=");
			sb.append(this._jlocals);
			
			// And stack state
			sb.append(", stack=");
			sb.append(this._jstack);
			
			// Create
			sb.append('}');
			rv = sb.toString();
			this._string = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
}

