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

import __squirreljme.IPCException;

/**
 * This represents the server for an IPC socket which is only capable of
 * accepting sockets and initializing them.
 *
 * @since 2016/05/31
 */
public final class KernelIPCServer
	extends KernelIPCSocket
{
	/** The service identifier. */
	protected final int serviceid;
	
	/**
	 * Initializes the kernel IPC server.
	 *
	 * @param __id The handle for the server socket.
	 * @param __svid The service ID that this hosts.
	 * @throws IPCException If the handle or service ID are zero
	 * or negative.
	 * @since 2016/05/31
	 */
	KernelIPCServer(int __id, int __svid)
		throws IPCException
	{
		super(__id);
		
		// {@squirreljme.error AY0a The service ID cannot be zero
		// or negative. (The service ID)}
		if (__svid <= 0)
			throw new IPCException(String.format("AY0a %d",
				__svid));
		
		// Set
		this.serviceid = __svid;
	}
	
	/**
	 * Returns the service ID of this socket.
	 *
	 * @return The socket's service ID.
	 * @since 2016/05/31
	 */
	public int serviceId()
	{
		return this.serviceid;
	}
}

