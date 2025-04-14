// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream that is guarded by being closed.
 *
 * @since 2020/11/21
 */
public final class GuardedOutputStream
	extends OutputStream
{
	/** The true output stream. */
	protected final OutputStream out;
	
	/** Is this closed? */
	volatile boolean _closed;
	
	/**
	 * Initializes the guarded stream.
	 * 
	 * @param __out The output stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/21
	 */
	public GuardedOutputStream(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void close()
		throws IOException
	{
		if (!this._closed)
		{
			// Make sure everything is written before closing
			try
			{
				this.out.flush();
			}
			finally
			{
				this._closed = true;
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void flush()
		throws IOException
	{
		if (this._closed)
			throw new IOException("Stream is closed.");
		
		this.out.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		if (this._closed)
			throw new IOException("Stream is closed.");
		
		this.out.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void write(byte[] __b)
		throws IOException
	{
		if (this._closed)
			throw new IOException("Stream is closed.");
		
		this.out.write(__b);
		this.out.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/21
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		if (this._closed)
			throw new IOException("Stream is closed.");
		
		this.out.write(__b, __o, __l);
		this.out.flush();
	}
}
