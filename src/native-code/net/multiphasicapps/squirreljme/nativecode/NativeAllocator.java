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

import java.util.Deque;
import java.util.List;
import net.multiphasicapps.util.msd.MultiSetDeque;

/**
 * This is a class which is used to allocate and manage native registers
 * which are available for a given CPU.
 *
 * @since 2016/08/30
 */
public class NativeAllocator
{
	/** The ABI used. */
	protected final NativeABI abi;
	
	/** The multi-set deque, used for removal. */
	final MultiSetDeque<NativeRegister> _msd;
	
	/** Saved int register queue. */
	final Deque<NativeRegister> _savedintq;
	
	/** Saved float register queue. */
	final Deque<NativeRegister> _savedfloatq;
	
	/** Temporary int register queue. */
	final Deque<NativeRegister> _tempintq;
	
	/** Temporary float register queue. */
	final Deque<NativeRegister> _tempfloatq;
	
	/** The current size of the stack. */
	volatile int _stacksize;
	
	/**
	 * Initializes the register allocator using the specified ABI.
	 *
	 * @param __abi The ABI used on the target system.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	NativeAllocator(NativeABI __abi)
		throws NullPointerException
	{
		// Check
		if (__abi == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.abi = __abi;
		
		// Setup queues
		MultiSetDeque<NativeRegister> msd = new MultiSetDeque<>();
		Deque<NativeRegister> savedintq = msd.subDeque();
		Deque<NativeRegister> savedfloatq = msd.subDeque();
		Deque<NativeRegister> tempintq = msd.subDeque();
		Deque<NativeRegister> tempfloatq = msd.subDeque();
		this._msd = msd;
		this._savedintq = savedintq;
		this._savedfloatq = savedfloatq;
		this._tempintq = tempintq;
		this._tempfloatq = tempfloatq;
		
		// Add all registers to the queues
		savedintq.addAll(__abi.saved(NativeRegisterKind.INTEGER));
		savedfloatq.addAll(__abi.saved(NativeRegisterKind.FLOAT));
		tempintq.addAll(__abi.temporary(NativeRegisterKind.INTEGER));
		tempfloatq.addAll(__abi.temporary(NativeRegisterKind.FLOAT));
	}
	
	/**
	 * Returns the ABI that is used for this.
	 *
	 * @return The used ABI.
	 * @since 2016/09/03
	 */
	public final NativeABI abi()
	{
		return this.abi;
	}
	
	/**
	 * Records the current state of the allocator.
	 *
	 * @return The allocator state.
	 * @since 2016/09/03
	 */
	public final NativeAllocatorState recordState()
	{
		return new NativeAllocatorState(this);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/09/03
	 */
	@Override
	public String toString()
	{
		return recordState().toString();
	}
}

