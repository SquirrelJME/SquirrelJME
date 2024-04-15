// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.emulator.vm.VMException;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * NanoCoat virtual machine state.
 *
 * @since 2023/12/05
 */
public final class NvmState
	implements Pointer
{
	/** The pointer where the state is stored. */
	private final long _pointer;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the new virtual machine state.
	 *
	 * @param __pool The allocation pool to use.
	 * @param __reservedPool The reserved memory pool.
	 * @param __param Parameters for starting the virtual machine.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the virtual machine could not be initialized.
	 * @since 2023/12/08
	 */
	public NvmState(AllocPool __pool, AllocPool __reservedPool,
		NvmBootParam __param)
		throws NullPointerException, VMException
	{
		if (__pool == null || __reservedPool == null || __param == null)
			throw new NullPointerException("NARG");
		
		// Initialize state.
		this._pointer = NvmState.__nvmBoot(__pool.pointerAddress(),
			__reservedPool.pointerAddress(),
			this, __param.pointerAddress());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/05
	 */
	@Override
	public long pointerAddress()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes a new virtual machine.
	 *
	 * @param __poolPtr The pool this allocates within.
	 * @param __reservedPtr The reserved pool pointer.
	 * @param __wrapper The wrapper for this reference.
	 * @param __paramPtr The pointer to the parameters.
	 * @return The pointer to the virtual machine state.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/08
	 */
	private static native long __nvmBoot(long __poolPtr, long __reservedPtr,
		NvmState __wrapper, long __paramPtr)
		throws VMException;
}
