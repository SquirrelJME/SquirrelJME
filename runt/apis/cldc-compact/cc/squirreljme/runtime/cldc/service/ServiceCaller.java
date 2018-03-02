// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.service;

import cc.squirreljme.runtime.cldc.system.SystemCall;
import cc.squirreljme.runtime.cldc.system.SystemFunction;

/**
 * This class allows simpler access to performing system calls into services
 * by using the standard system call interface.
 *
 * @since 2018/03/02
 */
public final class ServiceCaller
{
	/** The service index. */
	protected final Integer index;
	
	/**
	 * Initializes the service caller.
	 *
	 * @param __dx The system call index.
	 * @since 2018/03/02
	 */
	public ServiceCaller(int __dx)
		throws NullPointerException
	{
		this.index = __dx;
	}
	
	/**
	 * Calls the service with the given arguments.
	 *
	 * @param <R> The return type of the call.
	 * @param __rv The class for the return type.
	 * @param
	 * @throws ClassCastException If the return type is not valid.
	 * @throws NullPointerException On null arguments.
	 */
	public final <R> R serviceCall(Class<R> __rv, Enum<?> __func,
		Object... __args)
		throws ClassCastException, NullPointerException
	{
		if (__func == null)
			throw new NullPointerException("NARG");
		
		// Do not treat this as fatal
		if (__args == null)
			__args = new Object[0];
		
		// Build arguments to pass to the service
		int nargs = __args.length;
		Object[] xargs = new Object[nargs + 2];
		xargs[0] = this.index;
		xargs[1] = __func;
		for (int i = 0, o = 2; i < nargs; i++, o++)
			xargs[o] = __args[i];
		
		// Perform the call
		return SystemCall.<R>systemCall(__rv, SystemFunction.SERVICE_CALL,
			xargs);
	}
}

