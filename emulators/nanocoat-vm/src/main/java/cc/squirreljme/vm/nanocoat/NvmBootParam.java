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
 * Boot parameters for the virtual machine.
 *
 * @since 2023/12/12
 */
public final class NvmBootParam
	implements Pointer
{
	/** The pointer where the parameters are stored. */
	private final long _pointer;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the base boot parameters.
	 *
	 * @param __pool The pool to allocate within.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/12
	 */
	public NvmBootParam(AllocPool __pool)
		throws NullPointerException
	{
		if (__pool == null)
			throw new NullPointerException("NARG");
		
		this._pointer = NvmBootParam.__allocBootParam(__pool.pointerAddress());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/12
	 */
	@Override
	public long pointerAddress()
	{
		return this._pointer;
	}
	
	/**
	 * Sets the suite handler.
	 *
	 * @param __suite The suites to use.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/13
	 */
	public void setSuite(VirtualSuite __suite)
		throws VMException
	{
		NvmBootParam.__setSuite(this._pointer,
			(__suite == null ? 0 : __suite.pointerAddress()));
	}
	
	/**
	 * Allocates the boot parameters. 
	 *
	 * @param __poolPtr The pointer to the memory pool.
	 * @return The pointer to the allocated memory.
	 * @throws VMException If allocation failed.
	 * @since 2023/12/12
	 */
	private static native long __allocBootParam(long __poolPtr)
		throws VMException;
	
	/**
	 * Sets the suite handler.
	 *
	 * @param __thisPtr The structure pointer.
	 * @param __suitePtr The suite functions pointer.
	 * @throws VMException If it could not be set.
	 * @since 2023/12/13
	 */
	private static native void __setSuite(long __thisPtr, long __suitePtr)
		throws VMException;
}
