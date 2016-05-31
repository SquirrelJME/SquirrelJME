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
 * This is the base class for both client and server socket types.
 *
 * @since 2016/05/31
 */
public abstract class KernelIPCSocket
	implements __Identifiable__
{
	/** The socket identifier. */
	protected final int id;
	
	/**
	 * Initializes the base socket.
	 *
	 * @param __id The handle that the socket uses to identify itself.
	 * @throws IPCException If the socket identifier is negative.
	 * @since 2016/05/31
	 */
	KernelIPCSocket(int __id)
		throws IPCException
	{
		// {@squirreljme.error AY09 The handle ID for a connected socket cannot
		// be zero or negative. (The primary handle; The secondary handle)}
		if (__id <= 0)
			throw new IPCException(String.format("AY09 %d", __id));
		
		// Set
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int id()
	{
		return this.id;
	}
}

