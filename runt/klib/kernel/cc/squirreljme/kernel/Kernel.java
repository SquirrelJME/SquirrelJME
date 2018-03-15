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

/**
 * This class represents the kernel which manages the entire SquirrelJME
 * system and all of the needed IPC and running tasks/threads.
 *
 * @since 2017/12/08
 */
public final class Kernel
{
	/** The owning primitive kernel. */
	protected final PrimitiveKernel primitive;
	
	/** The manager for kernel services. */
	protected final KernelServices services;
	
	/** Dispatch for the system calls coming from kernel space. */
	protected final KernelSystemDispatch dispatch;
	
	/**
	 * Initializes the kernel.
	 *
	 * @param __pk The primitive kernel.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/03
	 */
	public Kernel(PrimitiveKernel __pk)
		throws NullPointerException
	{
		if (__pk == null)
			throw new NullPointerException("NARG");
		
		// Make sure the primitive kernel is always set
		this.primitive = __pk;
		
		// Initialize kernel side service information
		KernelServices services = new KernelServices(__pk.serviceMap());
		this.services = services;
		
		// Initialize the dispatcher
		this.dispatch = new KernelSystemDispatch(services);
	}
	
	/**
	 * Returns the dispatcher for incoming systems calls from the kernel
	 * task.
	 *
	 * @return The kernel dispatch.
	 * @since 2018/03/14
	 */
	public final KernelSystemDispatch systemDispatch()
	{
		return this.dispatch;
	}
}

