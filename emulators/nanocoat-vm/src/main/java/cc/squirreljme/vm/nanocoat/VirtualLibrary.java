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
	static
	{
		__Native__.__loadLibrary();
	}
	
	/**
	 * Initializes the virtual library wrapper.
	 *
	 * @param __pool The pool to be in.
	 * @param __lib The library to map.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If it could not be initialized.
	 * @since 2023/12/27
	 */
	public VirtualLibrary(AllocPool __pool, VMClassLibrary __lib)
		throws NullPointerException, VMException
	{
		if (__pool == null || __lib == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
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
}
