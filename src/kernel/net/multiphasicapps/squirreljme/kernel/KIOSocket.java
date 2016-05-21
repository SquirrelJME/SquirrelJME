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

import java.io.Closeable;
import java.io.IOException;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.squirreljme.kernel.KernelProcess;

/**
 * This is a socket which establishes an endpoint for sending and receiving
 * datagrams. Each socket is associated with a service number.
 * Sockets may optionally be connected to remote endpoints as a default
 * destination.
 *
 * A process may only have a single socket for a given service at any one time.
 *
 * Internally negative service values are anonymous services which can only
 * send datagrams and receive responses from the other side.
 *
 * @since 2016/05/20
 */
public final class KIOSocket
	implements Closeable
{
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
	
	/**
	 * Returns the socket's service identifier number.
	 *
	 * @return The server identifier number of the socket.
	 * @since 2016/05/21
	 */
	public int getId()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns {@code true} if this is an anonymous socket.
	 *
	 * @return {@code true} if an anonymous socket.
	 * @since 2016/05/20
	 */
	public boolean isAnonymous()
	{
		throw new Error("TODO");
	}
}

