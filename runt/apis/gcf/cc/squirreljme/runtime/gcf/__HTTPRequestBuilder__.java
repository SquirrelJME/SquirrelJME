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
 * This is used for building HTTP requests.
 *
 * @since 2019/05/12
 */
final class __HTTPRequestBuilder__
	extends OutputStream
{
	/** Byte data output. */
	final ByteArrayOutputStream _bytes =
		new ByteArrayOutputStream();
	
	/** Request properties. */
	final Map<String, String> _rqprops =
		new LinkedHashMap<>();
	
	/** The connection method. */
	String _rqmethod =
		"GET";
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/12
	 */
	@Override
	public final void close()
		throws IOException
	{
		// Only close once
		if (this._closed)
			return;
		this._closed = true;
		
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
		if (this._closed)
			throw new IOException("EC0o");
		
		// Note
		todo.TODO.note("Implement HTTP Flush");
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
		if (this._closed)
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
		if (this._closed)
			throw new IOException("EC0n");
		
		// Write to bytes
		this._bytes.write(__a, __o, __l);
	}
}

