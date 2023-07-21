// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.microedition.io.AccessPoint;
import javax.microedition.io.HttpConnection;

/**
 * This is a connection to a remote HTTP server, this runs off an existing
 * socket since it may be SSL encrypted. SquirrelJME does all the HTTP
 * handling since it could just be based on any existing stream connection
 * and does not need any other library.
 *
 * @since 2019/05/06
 */
public class HTTPClientConnection
	implements HttpConnection
{
	/** The remote address. */
	protected final HTTPAddress address;
	
	/** Tracker for the HTTP state. */
	protected final HTTPStateTracker tracker =
		new HTTPStateTracker();
	
	/** The target HTTP agent which contains the response. */
	protected final HTTPAgent agent;
	
	/** Request builder for outgoing connections. */
	private HTTPRequestBuilder _request;
	
	/**
	 * Initializes the HTTP connection.
	 *
	 * @param __addr The address.
	 * @param __connector The connector for HTTP calls.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/12
	 */
	public HTTPClientConnection(HTTPAddress __addr,
		HTTPAgentConnector __connector)
		throws NullPointerException
	{
		if (__addr == null || __connector == null)
			throw new NullPointerException("NARG");
		
		this.address = __addr;
		
		// Setup agents and handlers
		HTTPStateTracker tracker = this.tracker;
		HTTPAgent agent = new HTTPAgent(__addr, tracker, __connector);
		this.agent = agent;
		
		// Setup builder for the requests
		this._request = new HTTPRequestBuilder(__addr, tracker, agent);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Transition to the closed state
		this.tracker._state = HTTPState.CLOSED;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final AccessPoint getAccessPoint()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getDate()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getEncoding()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getExpiration()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getFile()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHeaderField(String __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHeaderField(int __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getHeaderFieldDate(String __a, long __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getHeaderFieldInt(String __a, int __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHeaderFieldKey(int __a)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHost()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getLastModified()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getLength()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getPort()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getProtocol()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getQuery()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getRef()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getRequestMethod()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getRequestProperty(String __a)
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getResponseCode()
		throws IOException
	{
		return this.__response().header.code;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getResponseMessage()
		throws IOException
	{
		return this.__response().header.message;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getType()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getURL()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final DataInputStream openDataInputStream()
		throws IOException
	{
		return new DataInputStream(this.openInputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final DataOutputStream openDataOutputStream()
		throws IOException
	{
		return new DataOutputStream(this.openOutputStream());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final InputStream openInputStream()
		throws IOException
	{
		// The response is the direct stream of the body
		return this.__response().inputStream();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final OutputStream openOutputStream()
		throws IOException
	{
		// This is the request stream directly
		return this.__request();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void setRequestMethod(String __m)
		throws IOException, NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Forward
		this.__request().setRequestMethod(__m);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void setRequestProperty(String __k, String __v)
		throws IOException, NullPointerException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Forward
		this.__request().setRequestProperty(__k, __v);
	}
	
	/**
	 * Attempts to obtain the request.
	 *
	 * @return The request information.
	 * @throws IOException If the connection was closed or is in connected
	 * state.
	 * @since 2019/05/13
	 */
	private HTTPRequestBuilder __request()
		throws IOException
	{
		/* {@squirreljme.error EC03 Cannot access the request} */
		if (this.tracker._state != HTTPState.SETUP)
		{
			// Clear before it is thrown
			this._request = null;
			
			// Toss
			throw new IOException("EC03");
		}
		
		// Return the request value
		return this._request;
	}
	
	/**
	 * Returns the response.
	 *
	 * @return The response.
	 * @throws IOException If the response could not be returned.
	 * @since 2019/05/13
	 */
	private HTTPResponse __response()
		throws IOException
	{
		HTTPAgent agent = this.agent;
		
		// Depends on the state
		HTTPStateTracker tracker = this.tracker;
		switch (tracker._state)
		{
				// Need to connect to the server, fall through to get the
				// agent response. Closing the request will send the HTTP
				// data over and load the server response
			case SETUP:
				this.__request().close();
			
				// Agent already has the response
			case CONNECTED:
			case CLOSED:
				return agent._response;
			
				// Should not occur
			default:
				throw Debugging.oops();
		}
	}
	
	/**
	 * Connects to the given address using the given stream.
	 *
	 * @param __addr The address.
	 * @return The open connection.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final HTTPClientConnection connectDefault(HTTPAddress __addr)
		throws NullPointerException
	{
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		return new HTTPClientConnection(__addr,
			new SocketHTTPAgentConnector());
	}
}

