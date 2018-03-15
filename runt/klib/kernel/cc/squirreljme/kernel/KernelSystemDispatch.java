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

/**
 * This implements dispatch to the kernel for system call related operations.
 *
 * @since 2018/03/14
 */
public final class KernelSystemDispatch
	implements SystemCallDispatch
{
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
				// {@squirreljme.error AP03 Unimplemented kernel function.
				// (The kernel function)}
			default:
				throw new RuntimeException(String.format("AP03 %s", __func));
		}
	}
}

