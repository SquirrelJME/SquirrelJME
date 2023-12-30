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
	
	/** The suite this is in. */
	protected final VirtualSuite suite;
	
	/** The name link. */
	private volatile LinkedCharStar _nameLink;
	
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the virtual library wrapper.
	 *
	 * @param __lib The library to map.
	 * @param __id The internal library ID.
	 * @param __suite The owning suite.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/27
	 */
	public VirtualLibrary(VMClassLibrary __lib, int __id,
		VirtualSuite __suite)
		throws NullPointerException, VMException
	{
		if (__lib == null || __suite == null)
			throw new NullPointerException("NARG");
		
		// We need these for the other initialization to be done
		this.library = __lib;
		this.suite = __suite;
		this.id = __id;
		
		// Initialize natively
		long nativePtr = VirtualLibrary.__init(this,
			__suite.pointerAddress());
		if (nativePtr == 0)
			throw new VMException("Could not wrap library.");
		
		// Store information accordingly
		this.link = AllocLink.ofBlockPtr(nativePtr);
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
	 * Returns the library ID.
	 * 
	 * @return The library ID.
	 * @since 2023/12/29
	 */
	@SuppressWarnings("unused")
	private int __id()
	{
		return this.id;
	}
	
	/**
	 * Returns the name pointer.
	 * 
	 * @return The name pointer.
	 * @since 2023/12/29
	 */
	@SuppressWarnings("unused")
	private long __name()
	{
		// Determined already?
		LinkedCharStar nameLink = this._nameLink;
		if (nameLink != null)
			return nameLink.pointerAddress();
		
		// If not, dup it
		nameLink = this.suite.pool.strDup(this.library.name());
		this._nameLink = nameLink;
		return nameLink.pointerAddress();
	}
	
	/**
	 * Initializes the native library.
	 *
	 * @param __self Self reference.
	 * @param __suitePtr The pointer to the owning suite.
	 * @return The pointer to the library structure.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/28
	 */
	private static native long __init(VirtualLibrary __self, long __suitePtr)
		throws VMException;
}
