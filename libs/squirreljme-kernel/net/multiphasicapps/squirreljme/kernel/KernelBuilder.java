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
 * This is used to create instances of the kernel.
 *
 * @since 2016/11/02
 */
public final class KernelBuilder
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/**
	 * Builds the target kernel.
	 *
	 * @return The resulting kernel.
	 * @throws IllegalArgumentException If the kernel was not correctly
	 * initialized.
	 * @since 2016/11/02
	 */
	public Kernel build()
		throws IllegalArgumentException
	{
		// Lock
		synchronized (this.lock)
		{
			return new Kernel(this);
		}
	}
}

