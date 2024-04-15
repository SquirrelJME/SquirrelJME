// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.net.Socket;

/**
 * Kills the socket on exit.
 *
 * @since 2024/01/19
 */
class __SocketKill__
	implements Runnable
{
	/** The socket to be killed. */
	protected final Socket socket;
	
	/**
	 * Initializes the socket killer.
	 *
	 * @param __socket The socket to kill.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/19
	 */
	__SocketKill__(Socket __socket)
		throws NullPointerException
	{
		if (__socket == null)
			throw new NullPointerException("NARG");
		
		this.socket = __socket;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/19
	 */
	@Override
	public void run()
	{
		// Close it
		try
		{
			this.socket.close();
		}
		catch (IOException __e)
		{
			__e.printStackTrace(System.err);
		}
	}
}
