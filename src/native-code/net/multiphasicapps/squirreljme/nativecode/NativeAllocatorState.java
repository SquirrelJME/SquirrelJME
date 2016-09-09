// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

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
public final class NativeAllocatorState
{
	/** The ABI used. */
	protected final NativeABI abi;
	
	/** Saved integer queue. */
	private final NativeRegister[] _savedintq;
	
	/** Saved float queue. */
	private final NativeRegister[] _savedfloatq;
	
	/** Temporary integer queue. */
	private final NativeRegister[] _tempintq;
	
	/** Temporary float queue. */
	private final NativeRegister[] _tempfloatq;
	
	/** The stack size. */
	private final int _stacksize;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Snapshots the state of the allocator.
	 *
	 * @param __ga The allocator to snapshot from.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	NativeAllocatorState(NativeAllocator __ga)
		throws NullPointerException
	{
		// Check
		if (__ga == null)
			throw new NullPointerException("NARG");
		
		// Copy ABI
		this.abi = __ga.abi;
		
		// Get parts for copying
		Deque<NativeRegister> savedintq = __ga._savedintq;
		Deque<NativeRegister> savedfloatq = __ga._savedfloatq;
		Deque<NativeRegister> tempintq = __ga._tempintq;
		Deque<NativeRegister> tempfloatq = __ga._tempfloatq;
		
		// Make arrays
		this._savedintq = savedintq.<NativeRegister>toArray(
			new NativeRegister[savedintq.size()]);
		this._savedfloatq = savedfloatq.<NativeRegister>toArray(
			new NativeRegister[savedfloatq.size()]);
		this._tempintq = tempintq.<NativeRegister>toArray(
			new NativeRegister[tempintq.size()]);
		this._tempfloatq = tempfloatq.<NativeRegister>toArray(
			new NativeRegister[tempfloatq.size()]);
		
		// Set stack size
		this._stacksize = __ga._stacksize;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/09
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
			
			if (true)
				throw new Error("TODO");
			
			// Create
			sb.append('}');
			rv = sb.toString();
			this._string = new WeakReference<>(rv);
		}
		
		// Return
		return rv;
	}
}

