// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package __squirreljme;

/**
 * This class handles the client side interface of IPC sockets.
 *
 * @since 2016/05/30
 */
public final class IPCClient
	extends IPC
{
	/**
	 * Creates a new IPC connect and connects it to a remote socket specified
	 * by the host and service ID.
	 *
	 * @param __host The process to connect to which hosts the specified
	 * service.
	 * @param __service The service to connect to for that given host.
	 * @throws IllegalArgumentException If the host or service identifiers are
	 * negative.
	 * @throws IPCException If the connection could not be made.
	 * @since 2016/05/30
	 */
	public IPCClient(int __host, int __service)
		throws IllegalArgumentException, IPCException
	{
		// {@squirreljme.error ZZ08 Negative host and service IDs are
		// not permitted.}
		if (__host < 0 || __service < 0)
			throw new IllegalArgumentException("ZZ08");
		
		throw new Error("TODO");
	}
}

