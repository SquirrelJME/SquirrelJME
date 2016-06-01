// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This is the implementation of processes which act as a bridge to the
 * interpreter memory space.
 *
 * @since 2016/05/31
 */
public class JVMKernelProcess
	extends KernelProcess
{
	/** The owning JVM kernel. */
	protected final JVMKernel jvmkernel;
	
	/**
	 * Initializes the kernel process.
	 *
	 * @param __k The owning kernel.
	 * @since 2016/05/31
	 */
	JVMKernelProcess(JVMKernel __k)
	{
		super(__k);
		
		// Set
		this.jvmkernel = (JVMKernel)this.kernel;
		
		// Setup interpreter process
		if (true)
			throw new Error("TODO");
	}
}

