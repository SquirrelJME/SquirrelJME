// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Boot parameters for the virtual machine.
 *
 * @since 2023/12/12
 */
public final class NvmBootParam
	implements Pointer
{
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
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/12
	 */
	@Override
	public long pointerAddress()
	{
		throw Debugging.todo();
	}
}
