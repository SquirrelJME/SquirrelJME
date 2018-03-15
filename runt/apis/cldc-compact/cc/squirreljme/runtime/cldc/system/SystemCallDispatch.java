// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system;

import cc.squirreljme.runtime.cldc.system.api.Call;

/**
 * This interface is used for any system call which must be dispatched to the
 * kernel.
 *
 * Local system calls do not need to be dispatched however.
 *
 * @since 2018/02/21
 */
public interface SystemCallDispatch
	extends Call
{
	/**
	 * Performs the given system call intended to be implemented by the local
	 * end.
	 *
	 * @param __func The function being called.
	 * @param __args The arguments to the function.
	 * @return The value returned from the call.
	 * @since 2018/02/21
	 */
	public abstract Object dispatch(SystemFunction __func, Object... __args)
		throws NullPointerException;
}

