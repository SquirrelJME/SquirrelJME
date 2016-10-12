// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.midp.lcdui;

import java.io.IOException;
import javax.microedition.io.Connector;
import javax.microedition.io.IMCConnection;
import javax.microedition.io.IMCServerConnection;

/**
 * This is a class which implements the display server used by the LCDUI
 * interface. The client implementation communicates with this server.
 *
 * @since 2016/10/11
 */
public abstract class DisplayServer
	implements Runnable
{
	/** The URI the client uses to connect. */
	public static final String CLIENT_URI =
		"imc://*:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"1.0;authmode=false";
	
	/** The URI the server uses to host. */
	private static final String _SERVER_URI =
		"imc://:net.multiphasicapps.squirreljme.midp.lcdui.DisplayServer:" +
		"1.0;authmode=false";
	
	/** The display server thread. */
	protected final Thread thread;
	
	/** The master IMC socket. */
	private final IMCServerConnection _serversock;
	
	/**
	 * Initializes the base display server.
	 *
	 * @since 2016/10/11
	 */
	public DisplayServer()
	{
		// Setup display server thread
		Thread thread;
		this.thread = (thread = new Thread(this, "SquirrelJMEDisplayServer"));
		
		// Server specific thread modification?
		modifyThread(thread);
		
		// Create server socket
		try
		{
			this._serversock = (IMCServerConnection)Connector.open(
				_SERVER_URI);
		}
		
		// {@squirreljme.error EB05 Could not create the display server
		// socket.}
		catch (IOException e)
		{
			throw new RuntimeException("EB05", e);
		}
		
		// Start it
		thread.start();
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
	 * @since 2016/10/11
	 */
	@Override
	public final void run()
	{
		// Infinite loop
		IMCServerConnection svsock = this._serversock;
		for (;;)
			try (IMCConnection sock = (IMCConnection)svsock.acceptAndOpen())
			{
				throw new Error("TODO");
			}
			
			// Failed read, ignore
			catch (IOException e)
			{
			}
	}
}

