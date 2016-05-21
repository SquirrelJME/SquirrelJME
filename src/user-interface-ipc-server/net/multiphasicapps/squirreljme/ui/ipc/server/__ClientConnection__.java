// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui.ipc.server;

import net.multiphasicapps.squirreljme.kernel.KIOConnectionClosedException;
import net.multiphasicapps.squirreljme.kernel.KIODatagram;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.ipc.DMCommandID;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This represents a client socket connection which is used as a two-way
 * communication channel to the display manager hosted by a server.
 *
 * @since 2016/05/21
 */
final class __ClientConnection__
	implements Runnable
{
	/** The socket used to communicate with the client. */
	protected final KIOSocket socket;
	
	/** The displays server which owns this client. */
	protected final UIDisplayManagerServer server;
	
	/**
	 * Initializes the client connection.
	 *
	 * @param __sock The socket which is piped to the client.
	 * @param __ds The associated display server.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	__ClientConnection__(KIOSocket __sock, UIDisplayManagerServer __ds)
		throws NullPointerException
	{
		// Check
		if (__sock == null || __ds == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.socket = __sock;
		this.server = __ds;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public void run()
	{
		System.err.printf("DEBUG -- Run UIClient %s%n", this);
		
		// Client run loop
		KIOSocket socket = this.socket;
		for (;;)
		{
			// 
			KIODatagram dg;
			try
			{
				// Try to immedietly read a packet
				dg = socket.receive(1L);
			}
			
			// The connection was closed, remove this connection and any
			// displays it may have
			catch (KIOConnectionClosedException e)
			{
				__closeConnection();
			}
			
			// Stop if this occurs
			catch (KIOException|InterruptedException e)
			{
				break;
			}
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * The connection from the client was closed, so destroy any resources
	 * that may be used by the display.
	 *
	 * @since 2016/05/21
	 */
	private void __closeConnection()
	{
		throw new Error("TODO");
	}
}

