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
	implements KernelIPCHandles
{
	/** The primary handle. */
	protected final int ahandle;
	
	/** The secondary handle. */
	protected final int bhandle;
	
	/**
	 * Initializes the kernel based IPC socket.
	 *
	 * @param __ah The primary handle.
	 * @param __bh The secondary handle.
	 * @throws IllegalArgumentException If the primary or secondary handles are
	 * zero or negative.
	 * @since 2016/05/31
	 */
	KernelIPCSocket(int __ah, int __bh)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AY09 The handle ID for a connected socket cannot
		// be zero or negative. (The primary handle; The secondary handle)}
		if (__ah <= 0 || __bh <= 0)
			throw new IllegalArgumentException(String.format("AY09 %d %d",
				__ah, __bh));
		
		// {@squirreljme.error AY0b The primary and secondary handle cannot
		// be the same value. (The primary and secondary handle)}
		if (__ah == __bh)
			throw new IllegalArgumentException(String.format("AY0b %d", __ah));
		
		// Set
		this.ahandle = __ah;
		this.bhandle = __bh;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int getPrimaryHandle()
	{
		return this.ahandle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/31
	 */
	@Override
	public int getSecondaryHandle()
	{
		return this.bhandle;
	}
}

