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
 * This class handles the server side aspects of the IPC socket.
 *
 * @since 2016/05/30
 */
public final class IPCServer
	extends IPC
{
	/**
	 * This creates a new server socket which is used by clients which want
	 * to access a service that the current process provides.
	 *
	 * @param __service The service identifier to provide.
	 * @throws IllegalArgumentException If the service identifier is negative.
	 * @throws IPCException If the service could not be hosted.
	 * @since 2016/05/30
	 */
	public IPCServer(int __service)
		throws IllegalArgumentException, IPCException
	{
		// {@squirreljme.error ZZ09 Negative service IDs are not permitted.}
		if (__service < 0)
			throw new IllegalArgumentException("ZZ09");
		
		throw new Error("TODO");
	}
}

