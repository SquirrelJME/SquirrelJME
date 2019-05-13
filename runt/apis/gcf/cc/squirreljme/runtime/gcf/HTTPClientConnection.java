// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.microedition.io.AccessPoint;
import javax.microedition.io.ConnectionNotFoundException;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.SocketConnection;
import javax.microedition.io.StreamConnection;

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
	
	/** The state of this connection. */
	private HTTPState _state =
		HTTPState.SETUP;
	
	/** The request builder. */
	__HTTPRequestBuilder__ _rqbuilder =
		new __HTTPRequestBuilder__();
	
	/**
	 * Initializes the HTTP connection.
	 *
	 * @param __addr The address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/12
	 */
	public HTTPClientConnection(HTTPAddress __addr)
		throws NullPointerException
	{
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		this.address = __addr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final AccessPoint getAccessPoint()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getDate()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getEncoding()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getExpiration()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getFile()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHeaderField(String __a)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHeaderField(int __a)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getHeaderFieldDate(String __a, long __b)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getHeaderFieldInt(String __a, int __b)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHeaderFieldKey(int __a)
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getHost()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getLastModified()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final long getLength()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getPort()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getProtocol()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getQuery()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getRef()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getRequestMethod()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getRequestProperty(String __a)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final int getResponseCode()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getResponseMessage()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getType()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final String getURL()
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/06
	 */
	@Override
	public final OutputStream openOutputStream()
		throws IOException
	{
		// {@squirreljme.error EC0l May only write to the HTTP connection
		// when in the setup phase.}
		if (this._state != HTTPState.SETUP)
			throw new IOException("EC0l");
		
		// The request builder is the output stream
		return this._rqbuilder;
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
		
		// Debug
		todo.DEBUG.note("%s", __m);
		
		// {@squirreljme.error EC0j The HTTP method may only be set before the
		// connection has been made.}
		if (this._state != HTTPState.SETUP)
			throw new IOException("EC0j");
		
		// Set
		this._rqbuilder._rqmethod = __m.toUpperCase();
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
		
		// Debug
		todo.DEBUG.note("%s = %s", __k, __v);
		
		// {@squirreljme.error EC0k Request properties may only be sent
		// when the connection is being setup.}
		if (this._state != HTTPState.SETUP)
			throw new IOException("EC0k");
		
		// All fields are case insensitive, so lowercase them!
		__k = __k.toLowerCase();
		
		// Clear?
		Map<String, String> rqprops = this._rqbuilder._rqprops;
		if (__v == null)
			rqprops.remove(__k);
		
		// Otherwise add
		else
			rqprops.put(__k, __v);
	}
	
	/**
	 * Connects to the given address using the given stream.
	 *
	 * @param __addr The address.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/06
	 */
	public static final HTTPClientConnection connect(HTTPAddress __addr)
		throws NullPointerException
	{
		if (__addr == null)
			throw new NullPointerException("NARG");
		
		return new HTTPClientConnection(__addr);
	}
}

