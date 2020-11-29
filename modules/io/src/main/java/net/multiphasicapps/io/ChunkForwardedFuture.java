// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a future that is a forward of another future.
 *
 * @since 2020/11/29
 */
public final class ChunkForwardedFuture
	implements ChunkFuture
{
	/** The future to get the value from. */
	private volatile ChunkFuture _future;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/29
	 */
	@Override
	public int get()
	{
		synchronized (this)
		{
			// {@squirreljme.error BD05 A future was never set.} 
			ChunkFuture future = this._future;
			if (future == null)
				throw new IllegalStateException("BD05");
			
			return future.get();
		}
	}
	
	/**
	 * Sets the future to read a value from.
	 * 
	 * @param __future The future to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/29
	 */
	public void set(ChunkFuture __future)
		throws NullPointerException
	{
		if (__future == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			this._future = __future;
		}
	}
}
