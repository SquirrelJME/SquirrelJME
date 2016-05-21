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
	/** The owning process. */
	protected final KernelProcess process;
	
	/** The service identifier. */
	protected final int id;
	
	/**
	 * Initializes a socket.
	 *
	 * @param __kp The process which owns this socket.
	 * @param __id The service identifier.
	 * @throws IllegalArgumentException If the service identifier is zero.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	KIOSocket(KernelProcess __kp, int __id)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__kp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY0f Zero service identifier. */
		if (__id == 0)
			throw new IllegalArgumentException("AY0f");
		
		// Set
		this.process = __kp;
		this.id = __id;
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
	
	/**
	 * Returns the socket's service identifier number.
	 *
	 * @return The server identifier number of the socket.
	 * @since 2016/05/21
	 */
	public int getId()
	{
		return this.id;
	}
	
	/**
	 * Returns the process which owns this given socket.
	 *
	 * @return The owning process for the socket.
	 * @since 2016/05/21
	 */
	public KernelProcess getProcess()
	{
		return this.process;
	}
	
	/**
	 * Returns {@code true} if this is an anonymous socket.
	 *
	 * @return {@code true} if an anonymous socket.
	 * @since 2016/05/20
	 */
	public boolean isAnonymous()
	{
		return this.id < 0;
	}
}

