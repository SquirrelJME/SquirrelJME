// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.microedition.io.Connector;
import javax.microedition.io.SocketConnection;
import net.multiphasicapps.io.HexDumpOutputStream;

/**
 * This class manages the HTTP connection data and is able to encode and
 * establish a connection.
 *
 * @since 2019/05/13
 */
public final class HTTPAgent
	implements HTTPSignalListener
{
	/** The remote address. */
	protected final HTTPAddress address;
	
	/** The state tracker. */
	protected final HTTPStateTracker tracker;
	
	/**
	 * Initializes the HTTP agent.
	 *
	 * @param __addr The address.
	 * @param __t The state tracker.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public HTTPAgent(HTTPAddress __addr, HTTPStateTracker __t)
		throws NullPointerException
	{
		if (__addr == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.address = __addr;
		this.tracker = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void requestReady(byte[] __data)
		throws IOException, NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Debug
		todo.DEBUG.note(">>> REQUEST:");
		HexDumpOutputStream.dump(System.err, __data);
		todo.DEBUG.note("<<<<<<<<<<<<");
		
		// Open connection to remote server
		try (SocketConnection socket = (SocketConnection)Connector.open(
			"socket://" + this.address.ipaddr))
		{
			// Connect to remote system and send all the HTTP data
			try (OutputStream out = socket.openOutputStream())
			{
				out.write(__data, 0, __data.length);
				out.flush();
			}
			
			if (true)
				throw new todo.TODO();
		}
		
		throw new todo.TODO();
	}
}

