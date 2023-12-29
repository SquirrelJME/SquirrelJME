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
import cc.squirreljme.vm.VMClassLibrary;

/**
 * Provides a virtual library.
 *
 * @since 2023/12/12
 */
public final class VirtualLibrary
	implements Pointer
{
	/** The allocation link used for the structure. */
	protected final AllocLink link;
	
	/** The library that this wraps. */
	protected final VMClassLibrary library;
	
	/** The internal library ID. */
	protected final int id;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the virtual library wrapper.
	 *
	 * @param __pool The pool to be in.
	 * @param __lib The library to map.
	 * @param __id The internal library ID.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/27
	 */
	public VirtualLibrary(AllocPool __pool, VMClassLibrary __lib, int __id)
		throws NullPointerException, VMException
	{
		if (__pool == null || __lib == null)
			throw new NullPointerException("NARG");
		
		// Initialize natively
		long nativePtr = VirtualLibrary.__init(__pool.pointerAddress(), __lib,
			__lib.name(), __id);
		if (nativePtr == 0)
			throw new VMException("Could not wrap library.");
		
		// Store information accordingly
		this.link = AllocLink.ofBlockPtr(nativePtr);
		this.library = __lib;
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/13
	 */
	@Override
	public long pointerAddress()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Initializes the native library.
	 *
	 * @param __poolPtr The pool to allocate within.
	 * @param __self Self reference.
	 * @param __name The library name.
	 * @param __id The library ID.
	 * @return The pointer to the library structure.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/28
	 */
	private static native long __init(long __poolPtr, VMClassLibrary __self,
		String __name, int __id)
		throws VMException;
}
