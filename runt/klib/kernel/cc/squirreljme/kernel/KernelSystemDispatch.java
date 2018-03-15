// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

import cc.squirreljme.runtime.cldc.system.SystemCallDispatch;
import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.system.type.VoidType;

/**
 * This implements dispatch to the kernel for system call related operations.
 *
 * @since 2018/03/14
 */
public final class KernelSystemDispatch
	implements SystemCallDispatch
{
	/** Kernel service manager. */
	protected final KernelServices services;
	
	/**
	 * Initializes the system dispatch for the kernel side.
	 *
	 * @param __sv The service manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/15
	 */
	public KernelSystemDispatch(KernelServices __sv)
		throws NullPointerException
	{
		if (__sv == null)
			throw new NullPointerException("NARG");
		
		this.services = __sv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/14
	 */
	@Override
	public final Object dispatch(SystemFunction __func, Object... __args)
		throws NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		switch (__func)
		{
				// This is not needed, it just indicates that the kernel is
				// initialized and a main program is ready to execute
			case INITIALIZED:
				return VoidType.INSTANCE;
			
				// {@squirreljme.error AP03 Unimplemented kernel function.
				// (The kernel function)}
			default:
				throw new RuntimeException(String.format("AP03 %s", __func));
		}
	}
}

