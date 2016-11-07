// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This is used to build immutable instances of kernel parameters without
 * requiring that a class be implemented to provide parameters.
 *
 * @since 2016/11/07
 */
public class KernelLaunchParametersBuilder
{
	/** Change lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Constructs the final immutabel kernel launch parameters.
	 *
	 * @return The launch parameters for the kernel.
	 * @since 2016/11/07
	 */
	public final KernelLaunchParameters build()
	{
		// Lock
		synchronized (this.lock)
		{
			throw new Error("TODO");
		}
	}
}

