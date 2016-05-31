// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel;

import __squirreljme.IPC;
import __squirreljme.IPCAlternative;

/**
 * This class provides the alternative implementation of SquirrelJME's IPC
 * interfaces which are used by all applications. This must be used by the
 * kernel because the normal IPC interface is purely virtual and on a host
 * JVM an exception will be thrown, while running on SquirrelJME it would
 * attempt to interact with actual processes running outside of the guest JVM.
 *
 * @since 2016/05/31
 */
public class KernelIPCAlternative
	implements IPCAlternative
{
	/** The lock used to synchronize communications. */
	protected final Object lock =
		new Object();;
	
	/** The owning kernel. */
	protected final Kernel kernel;
	
	/**
	 * This initializes the alternative IPC interface for usage by the kernel.
	 *
	 * @param __k The kernel which owns this alternative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/31
	 */
	public KernelIPCAlternative(Kernel __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.kernel = __k;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int listen(int __svid)
	{
		// Check
		if (__svid <= 0)
			return IPC.ERROR_INVALID_SERVICE_ID;
		
		throw new Error("TODO");
	}
}

