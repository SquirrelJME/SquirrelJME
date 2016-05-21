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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.squirreljme.kernel.KIOException;
import net.multiphasicapps.squirreljme.kernel.KIOSocket;
import net.multiphasicapps.squirreljme.ui.UIDisplayManager;

/**
 * This is a server which is given a socket along with a display manager and
 * wraps the given display manager through the socket so that clients may
 * access it.
 *
 * @since 2016/05/21
 */
public class UIDisplayManagerServer
	implements Runnable
{
	/** The process rate of commands in miliseconds. */
	public static final long PROCESS_TIME =
		10L;
	
	/** The socket which acts as the display manager server. */
	protected final KIOSocket socket;
	
	/** The display manager which is wrapped by the IPC. */
	protected final UIDisplayManager manager;
	
	/** List of sockets which are connected to this server. */
	private final List<__ClientConnection__> _connections =	
		new LinkedList<>();
	
	/**
	 * Initializes the wrapper around a display manager and provides an IPC
	 * service
	 *
	 * @param __sock The socket which acts as the server
	 * @param __dm The display manager which this server interface interacts
	 * with to provide an interface to the client.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/21
	 */
	public UIDisplayManagerServer(KIOSocket __sock, UIDisplayManager __dm)
		throws NullPointerException
	{
		// Check
		if (__sock == null || __dm == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.socket = __sock;
		this.manager = __dm;
		
		// Set the monitor to be notified when a packet is received or an
		// accept is made
		__sock.setMonitor(this._connections);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/21
	 */
	@Override
	public void run()
	{
		// Infinite loop
		KIOSocket socket = this.socket;
		List<__ClientConnection__> connections = this._connections;
		for (boolean firstloop = true;; firstloop = false)
		{
			// Lock
			synchronized (connections)
			{
				// Could be interrupted
				try
				{
					// Wait on the monitor for awhile, but for the first time
					// wait for a second.
					connections.wait((firstloop ? 1_000L : 0L));
					
					// Were any clients added to be accepted?
					KIOSocket cls = socket.accept(1L);
					
					// New client connection
					if (cls != null)
						connections.add(new __ClientConnection__(cls, this));
				}
				
				// Was interrupted, just run the normal loop
				catch (KIOException|InterruptedException e)
				{
				}
				
				// Run all connections
				if (!connections.isEmpty())
				{
					Iterator<__ClientConnection__> it = connections.iterator();
					while (it.hasNext())
						it.next().run();
				}
			}
		}
	}
}

