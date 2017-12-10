// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.kernel;

/**
 * This is a kernel interface which directly interacts with a kernel.
 *
 * @since 2017/12/08
 */
public final class ContextKernelInterface
	extends KernelInterface
{
	/** The micro kernel to interact with. */
	protected final Kernel kernel;
	
	/** All operations on the kernel must operate within this context. */
	protected final Context context;
	
	/**
	 * Initializes the interface which interacts with the given Kernel
	 * using the given context.
	 *
	 * @param __uk The kernel to interact with.
	 * @param __c The context the interface operates under.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/08
	 */
	public ContextKernelInterface(Kernel __uk, Context __c)
		throws NullPointerException
	{
		if (__uk == null || __c == null)
			throw new NullPointerException("NARG");
		
		this.kernel = __uk;
		this.context = __c;
	}
}

