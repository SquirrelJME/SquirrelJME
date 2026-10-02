// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.StreamConnection;

/**
 * This class manages the HTTP connection data and is able to encode and
 * establish a connection.
 *
 * @since 2019/05/13
 */
@SquirrelJMEVendorApi
public final class HTTPAgent
	implements HTTPSignalListener
{
	/** The remote address. */
	@SquirrelJMEVendorApi
	protected final HTTPAddress address;
	
	/** The state tracker. */
	@SquirrelJMEVendorApi
	protected final HTTPStateTracker tracker;
	
	/** The connector used. */
	@SquirrelJMEVendorApi
	protected final HTTPAgentConnector connector;
	
	/** The HTTP response. */
	HTTPResponse _response;
	
	/**
	 * Initializes the HTTP agent.
	 *
	 * @param __addr The address.
	 * @param __t The state tracker.
	 * @param __connector The connector used for connections.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	@SquirrelJMEVendorApi
	public HTTPAgent(HTTPAddress __addr, HTTPStateTracker __t,
		HTTPAgentConnector __connector)
		throws NullPointerException
	{
		if (__addr == null || __t == null)
			throw new NullPointerException("NARG");
		
		this.address = __addr;
		this.tracker = __t;
		this.connector = __connector;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	@SquirrelJMEVendorApi
	public final void requestReady(byte[] __data)
		throws IOException, NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Debug
		Debugging.debugNote(" -> %d", __data.length);
		
		// Open connection to remote server
		byte[] response;
		try (StreamConnection socket =
			 this.connector.connectStream(this.address))
		{
			// Connect to remote system and send all the HTTP data and
			// read it as well
			try (OutputStream out = socket.openOutputStream();
				InputStream in = socket.openInputStream())
			{
				// Write and send data
				out.write(__data, 0, __data.length);
				out.flush();
				
				// Read response data
				try (ByteArrayOutputStream baos = new ByteArrayOutputStream(
					Math.max(1024, in.available())))
				{
					// Copy all input data
					byte[] buf = new byte[512];
					for (;;)
					{
						int rc = in.read(buf);
						
						if (rc < 0)
							break;
						
						baos.write(buf, 0, rc);
					}
					
					// Store for later decode
					response = baos.toByteArray();
				}
			}
		}
		
		// Failed read/write
		catch (IOException e)
		{
			// Debug
			e.printStackTrace();
			
			throw e;
		}
		
		// Parse the input
		this._response = HTTPResponse.parse(response);
		
		// Enter the connected state
		this.tracker.state = HTTPState.CONNECTED;
	}
}

