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

/**
 * This contains a snapshot of the state of the locals and stack variables
 * at a given operation.
 *
 * @since 2016/09/09
 */
public final class GenericAllocatorState
{
	/** Local variables. */
	private final __VarStates__ _jlocals;
	
	/** Stack variables. */
	private final __VarStates__ _jstack;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * This snapshots the state of the allocator.
	 *
	 * @param __ga The state to snapshot.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/09
	 */
	GenericAllocatorState(GenericAllocator __ga)
		throws NullPointerException
	{
		// Check
		if (__ga == null)
			throw new NullPointerException("NARG");
		
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
			sb.append(", stack(");
			sb.append(this._stacksize);
			sb.append(")=");
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

