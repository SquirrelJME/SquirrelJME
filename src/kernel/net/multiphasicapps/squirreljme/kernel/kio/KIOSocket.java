// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.kio;

import java.io.Closeable;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This is a socket which establishes an endpoint for sending and receiving
 * datagrams. Each socket is associated with a service number and a port
 * number. Sockets may optionally be connected to remote endpoints as a default
 * destination.
 *
 * A process may only have a single socket for a given address and port at any
 * one time.
 *
 * @since 2016/05/20
 */
public final class KIOSocket
	implements Closeable
{
	/**
	 * 
	 */
	public KIOSocket(Kernel __k)
	{
		this(__k, 0, 0);
	}
	
	/**
	 * Creates a new socket under the given kernel for the current process
	 * using the given service and port number.
	 *
	 * @param __k The kernel the socket works under.
	 * @param __sv The service number.
	 * @param __port The port number.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public KIOSocket(Kernel __k, int __sv, int __port)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/20
	 */
	@Override
	public void close()
		throws KIOException
	{
		throw new Error("TODO");
	}
}

