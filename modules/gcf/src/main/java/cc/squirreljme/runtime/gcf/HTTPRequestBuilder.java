// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.gcf;

import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
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
	/** The user agent SquirrelJME uses. */
	public static final String USER_AGENT =
		"SquirrelJME/" + SquirrelJME.RUNTIME_VERSION + " " +
		"Configuration/CLDC-1.0 Configuration/CLDC-1.1 " +
		"Configuration/CLDC-1.8 Profile/MIDP-1.0 Profile/MIDP-2.0 " +
		"Profile/MIDP-2.1 Profile/MIDP-3.0 Profile/MEEP-8.0";
	
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
		
		// Send the agent out request bytes
		this.listener.requestReady(this.getBytes());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final void flush()
		throws IOException
	{
		// {@squirreljme.error EC04 Cannot flush closed HTTP stream.}
		if (this.tracker._state != HTTPState.SETUP)
			throw new IOException("EC04");
		
		// Note
		Debugging.todoNote("Implement HTTP Flush", new Object[] {});
	}
	
	/**
	 * Builds the bytes for the request.
	 *
	 * @return The bytes to send down the stream.
	 * @throws IOException If they could not be set.
	 * @since 2019/05/13
	 */
	public final byte[] getBytes()
		throws IOException
	{
		HTTPAddress address = this.address;
		
		// Build output
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream(1024))
		{
			// Get any written data
			byte[] bytes = this._bytes.toByteArray();
			
			// Print header data, note that CRLF is used everywhere regardless
			// of whether the system uses it or not
			try (PrintStream ps = new PrintStream(baos, true, "utf-8"))
			{
				// HTTP header
				ps.printf("%s %s HTTP/1.1\r\n",
					this._rqmethod, address.file);
				
				// Add some implicit properties
				Map<String, String> rqp = new LinkedHashMap<>(this._rqprops);
				rqp.put("host", address.ipaddr.hostname);
				
				// If the user agent was specified, add to it or set one
				String ua = rqp.get("user-agent");
				rqp.put("user-agent", (ua == null ?
					HTTPRequestBuilder.USER_AGENT :
					ua + HTTPRequestBuilder.USER_AGENT));
				
				// Is content being specified?
				if (bytes != null)
					rqp.put("content-length", Integer.toString(bytes.length));
				
				// Write headers
				for (Map.Entry<String, String> e : rqp.entrySet())
					ps.printf("%s: %s\r\n", e.getKey(), e.getValue());
				
				// End of header
				ps.print("\r\n");
				
				// Flush to make sure nothing is buffered
				ps.flush();
			}
			
			// Write any data
			if (bytes != null)
				baos.write(bytes, 0, bytes.length);
			
			// Build
			return baos.toByteArray();
		}
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
		// {@squirreljme.error EC05 Cannot write more HTTP data.}
		if (this.tracker._state != HTTPState.SETUP)
			throw new IOException("EC05");
		
		// Write to bytes
		this._bytes.write(__b);
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2019/05/13
	 */
	@Override
	public final void write(byte[] __a)
		throws IOException, NullPointerException
	{
		this.write(__a, 0, __a.length);
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
		
		// {@squirreljme.error EC06 Cannot write more HTTP data.}
		if (this.tracker._state != HTTPState.SETUP)
			throw new IOException("EC06");
		
		// Write to bytes
		this._bytes.write(__a, __o, __l);
	}
}

