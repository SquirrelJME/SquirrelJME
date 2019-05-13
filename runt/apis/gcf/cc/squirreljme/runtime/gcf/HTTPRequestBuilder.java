// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This is used for building HTTP requests which will build the request to use
 * for the server.
 *
 * @since 2019/05/12
 */
public final class HTTPRequestBuilder
	extends OutputStream
{
	/** The remote address. */
	protected final HTTPAddress address;
	
	/** The listener when the connection is closed. */
	protected final HTTPSignalListener listener;
	
	/** State tracker for connections. */
	protected final HTTPStateTracker tracker;
	
	/** Byte data output. */
	private final ByteArrayOutputStream _bytes =
		new ByteArrayOutputStream();
	
	/** Request properties. */
	private final Map<String, String> _rqprops =
		new LinkedHashMap<>();
	
	/** The connection method. */
	private String _rqmethod =
		"GET";
	
	/**
	 * Initializes the request builder.
	 *
	 * @param __addr The remote address.
	 * @param __st State tracker for HTTP connections.
	 * @param __l The agent listener.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/13
	 */
	public HTTPRequestBuilder(HTTPAddress __addr, HTTPStateTracker __st,
		HTTPSignalListener __l)
		throws NullPointerException
	{
		if (__l == null || __st == null || __addr == null)
			throw new NullPointerException("NARG");
		
		this.listener = __l;
		this.tracker = __st;
		this.address = __addr;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Only close once
		if (this.tracker._state != HTTPState.SETUP)
			return;
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final void flush()
		throws IOException
	{
		// {@squirreljme.error EC0o Cannot flush closed HTTP stream.}
		if (this.tracker._state != HTTPState.SETUP)
			throw new IOException("EC0o");
		
		// Note
		todo.TODO.note("Implement HTTP Flush");
	}
	
	/**
	 * Sets the request method to use.
	 *
	 * @param __m The method to use.
	 * @throws IOException If this is not in the setup phase.
	 * @throws NullPointerException If no method was specified.
	 * @since 2019/05/13
	 */
	public final void setRequestMethod(String __m)
		throws IOException, NullPointerException
	{
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._rqmethod = __m.toUpperCase();
	}
	
	/**
	 * Adds or replaces an existing request property, note that for multiple
	 * request property specifications they need to manually be comma
	 * separated.
	 *
	 * @param __k The request header key.
	 * @param __v The value to use, {@code null} clears.
	 * @throws IOException If this is not in the setup phase.
	 * @throws NullPointerException If the key was null.
	 * @since 2019/05/13
	 */
	public final void setRequestProperty(String __k, String __v)
		throws IOException
	{
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// All fields are case insensitive, so lowercase them!
		__k = __k.toLowerCase();
		
		// Clear?
		Map<String, String> rqprops = this._rqprops;
		if (__v == null)
			rqprops.remove(__k);
		
		// Otherwise add
		else
			rqprops.put(__k, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final void write(int __b)
		throws IOException
	{
		// {@squirreljme.error EC0m Cannot write more HTTP data.}
		if (this.tracker._state != HTTPState.SETUP)
			throw new IOException("EC0m");
		
		// Write to bytes
		this._bytes.write(__b);
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final void write(byte[] __a, int __o, int __l)
		throws IndexOutOfBoundsException, IOException, NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __a.length)
			throw new IndexOutOfBoundsException("IOOB");
		
		// {@squirreljme.error EC0n Cannot write more HTTP data.}
		if (this.tracker._state != HTTPState.SETUP)
			throw new IOException("EC0n");
		
		// Write to bytes
		this._bytes.write(__a, __o, __l);
	}
}

