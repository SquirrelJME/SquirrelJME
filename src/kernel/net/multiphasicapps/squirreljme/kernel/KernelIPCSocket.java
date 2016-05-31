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

/**
 * This represents a kernel socket, it acts as a buffer between two endpoints.
 *
 * @since 2016/05/31
 */
public class KernelIPCSocket
{
	/** The client handle. */
	protected final int clienth;
	
	/** The server handle. */
	protected final int serverh;
	
	/**
	 * Initializes the kernel based IPC socket.
	 *
	 * @param __clh The client handle.
	 * @param __svh The server handle.
	 * @throws IllegalArgumentException If the client or server handle are
	 * zero or negative.
	 * @since 2016/05/31
	 */
	KernelIPCSocket(int __clh, int __svh)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AY09 (The client handle; The server handle)}
		if (__clh <= 0 || __svh <= 0)
			throw new IllegalArgumentException(String.format("AY09 %d %d",
				__clh, __svh));
		
		// Set
		this.clienth = __clh;
		this.serverh = __svh;
	}
}

