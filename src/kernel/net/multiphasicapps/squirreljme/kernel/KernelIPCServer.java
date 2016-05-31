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
 * This represents the server for an IPC socket which is only capable of
 * accepting sockets and initializing them.
 *
 * @since 2016/05/31
 */
public class KernelIPCServer
	implements KernelIPCHandles
{
	/** The handle of this socket. */
	protected final int handle;
	
	/** The service identifier. */
	protected final int serviceid;
	
	/**
	 * Initializes the kernel IPC server.
	 *
	 * @param __handle The handle for the server socket.
	 * @param __svid The service ID that this hosts.
	 * @throws IllegalArgumentException If the handle or service ID are zero
	 * or negative.
	 * @since 2016/05/31
	 */
	public KernelIPCServer(int __handle, int __svid)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AY0a The handle or service IDs cannot be zero
		// or negative. (The handle ID; The service ID)}
		if (__handle <= 0 || __svid <= 0)
			throw new IllegalArgumentException(String.format("AY0a %d %d",
				__handle, __svid));
		
		// Set
		this.handle = __handle;
		this.serviceid = __svid;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int getPrimaryHandle()
	{
		return this.handle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int getSecondaryHandle()
	{
		return this.handle;
	}
}

