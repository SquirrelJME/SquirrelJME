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
	 * Creates a new anonymous socket which is used to connect and send data
	 * to sockets which are listening.
	 *
	 * @param __k The kernel the socket works under.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public KIOSocket(Kernel __k)
		throws KIOException, NullPointerException
	{
		this(__k, 0);
	}
	
	/**
	 * Creates a new socket under the given kernel for the current process
	 * using the given service and port number.
	 *
	 * @param __k The kernel the socket works under, zero means that.
	 * @param __sv The service number.
	 * @throws IllegalArgumentException If the service number is negative.
	 * @throws KIOException If the socket could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	public KIOSocket(Kernel __k, int __sv)
		throws IllegalArgumentException, KIOException, NullPointerException
	{
		this(__k.currentProcess(), __determineServiceID(__k, __sv));
	}
	
	/**
	 * Creates a new socket under the given kernel for the current process
	 * using the given service and port number.
	 *
	 * @param __k The kernel the socket works under, zero and negative.
	 * @param __sv The service number.
	 * @throws KIOException If the socket could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	private KIOSocket(KernelProcess __kp, int __sv)
		throws KIOException, NullPointerException
	{
		// Check
		if (__kp == null)
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
	
	/**
	 * Determines the service number to use
	 *
	 * @param __k The owning kernel.
	 * @param __sv The requested service number, if zero then one is determined
	 * by the kernel.
	 * @throws KIOException If the service number is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/20
	 */
	private static int __determineServiceID(Kernel __k, int __sv)
		throws KIOException, NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AY0d Cannot initialize a socket with a negative
		// service number. (The service number)}
		if (__sv < 0)
			throw new KIOException(String.format("AY0d %d", __sv));
		
		// Known service, return the input value
		if (__sv > 0)
			return __sv;
		
		throw new Error("TODO");
	}
}

