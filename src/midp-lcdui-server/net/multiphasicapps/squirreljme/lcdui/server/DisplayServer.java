// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui.server;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;
import net.multiphasicapps.squirreljme.lcdui.common.DisplayProtocol;

/**
 * This class contains the implementation of the display server.
 *
 * @since 2016/10/15
 */
public abstract class DisplayServer
	implements Runnable
{
	/** The connection the server listens on. */
	private final StreamConnectionNotifier _server;
	
	/**
	 * Initializes the display server and uses the default server.
	 *
	 * @since 2016/10/15
	 */
	public DisplayServer()
	{
		this(__openDefaultServer());
	}
	
	/**
	 * Initializes the display server to use the given connection.
	 *
	 * @param __sc The server stream connection.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/15
	 */
	public DisplayServer(StreamConnectionNotifier __sc)
		throws NullPointerException
	{
		// Check
		if (__sc == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._server = __sc;
		
		// Create thread
		Thread t = new Thread(this, "SquirrelJMEDisplayServer");
		modifyThread(t);
		
		// Start it
		t.start();
	}
	
	/**
	 * This may be used by an implementation of the display server to modify
	 * the thread behavior.
	 *
	 * The default implementation does nothing.
	 *
	 * @param __t The thread to modify.
	 * @since 2016/10/11
	 */
	protected void modifyThread(Thread __t)
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/15
	 */
	@Override
	public final void run()
	{
		// Close server on completion
		try (StreamConnectionNotifier server = this._server)
		{
			// Loop for a very long time
			for (;;)
				try (StreamConnection sock = svsock.acceptAndOpen())
				{
				}
				
				// Ignore these
				catch (IOException e)
				{
				}
		}
		
		// {@squirreljme.error DX01 Error handling server data.}
		catch (IOException e)
		{
			throw new RuntimeException("DX01", e);
		}
	}
	
	/**
	 * Attempts to start the display server.
	 *
	 * @return The connection notifier for the server.
	 * @since 2016/10/15
	 */
	private static StreamConnectionNotifier __openDefaultServer()
	{
		// At least try
		try
		{
			return (StreamConnectionNotifier)Connector.open(
				Objects.toString(System.getProperty(
				DisplayProtocol.DISPLAY_SERVER_PROPERTY),
				DisplayProtocol.DISPLAY_SERVER_URI));
		}
		
		// {@squirreljme.error DX02 Failed to open the default server
		// connection.}
		catch (IOException e)
		{
			throw new RuntimeException("DX02", e);
		}
	}
}

