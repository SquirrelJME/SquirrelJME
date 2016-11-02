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
	
	/** The thread manager. */
	volatile KernelThreadManager _threadmanager;
	
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
	
	/**
	 * Sets the interface which manages threads which are ran by the kernel.
	 *
	 * @param __tm The thread manager used by the kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/02
	 */
	public void threadManager(KernelThreadManager __tm)
		throws NullPointerException
	{
		// Check
		if (__tm == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (this.lock)
		{
			this._threadmanager = __tm;
		}
	}
}

