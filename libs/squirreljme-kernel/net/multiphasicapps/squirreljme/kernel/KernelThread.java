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
 * This represents a single kernel thread.
 *
 * @since 2016/11/01
 */
public class KernelThread
{
	/** The kernel which owns this thread. */
	protected final Kernel kernel;
	
	/**
	 * Initializes the kernel thread.
	 *
	 * @param __k The owning kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/01
	 */
	KernelThread(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
	}
}

