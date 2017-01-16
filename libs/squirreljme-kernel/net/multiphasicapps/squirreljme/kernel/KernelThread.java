// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

/**
 * This class represents threads which may be managed by a kernel.
 *
 * @see KernelNativeThread
 * @since 2016/11/01
 */
public abstract class KernelThread
	implements AutoCloseable
{
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/** The owning process. */
	protected final KernelProcess process;
	
	/**
	 * Initializes the kernel thread.
	 *
	 * @param __kp The process which owns the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/08
	 */
	protected KernelThread(KernelProcess __kp)
		throws NullPointerException
	{
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __kp.kernel();
		this.process = __kp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/16
	 */
	@Override
	public final void close()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the owning kernel.
	 *
	 * @return The owning kernel.
	 * @since 2017/01/16
	 */
	public final Kernel kernel()
	{
		return this.kernel;
	}
	
	/**
	 * Returns the owning process.
	 *
	 * @return The owning process.
	 * @since 2017/01/16
	 */
	public final KernelProcess process()
	{
		return this.process;
	}
}

