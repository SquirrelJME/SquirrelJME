// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is an output which when {@link #close()} is called, it will call
 * {@link LinkGlob#joinResource(String, InputStream)} with the bytes of
 * the data. This can be used for creating resources dynamically during
 * compilation stage if needed.
 *
 * @since 2022/09/07
 */
public final class OutputStreamToGlobResource
	extends OutputStream
{
	/** The glob target. */
	private final LinkGlob _glob;
	
	/** The resource name. */
	private final String _rcName;
	
	/** The buffer for bytes. */
	private volatile ByteArrayOutputStream _buffer =
		new ByteArrayOutputStream(4096);
	
	/** Has this been closed? */
	private volatile boolean _closed;
	
	/**
	 * Initializes the output.
	 * 
	 * @param __glob The glob to write to.
	 * @param __rcName The name of the resource to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/07
	 */
	public OutputStreamToGlobResource(LinkGlob __glob, String __rcName)
		throws NullPointerException
	{
		if (__glob == null || __rcName == null)
			throw new NullPointerException("NARG");
		
		this._glob = __glob;
		this._rcName = __rcName;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/07
	 */
	@Override
	public void close()
		throws IOException
	{
		synchronized (this)
		{
			// Close only once
			if (this._closed)
				return;
			
			// Mark closed
			this._closed = true;
			
			// Write to the output
			try (InputStream in = new ByteArrayInputStream(
				this._buffer.toByteArray()))
			{
				// Make it invalid now so it collects regardless
				this._buffer = null;
				
				// Join the resource
				this._glob.joinResource(this._rcName, in);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/07
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		synchronized (this)
		{
			if (this._closed)
				throw new IOException("CLOS");
			
			this._buffer.write(__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/07
	 */
	@Override
	public void write(byte[] __b)
		throws IOException
	{
		synchronized (this)
		{
			if (this._closed)
				throw new IOException("CLOS");
			
			this._buffer.write(__b);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/07
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		synchronized (this)
		{
			if (this._closed)
				throw new IOException("CLOS");
			
			this._buffer.write(__b, __o, __l);
		}
	}
}
