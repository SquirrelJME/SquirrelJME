// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.io;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.OutputStream;

/**
 * This is an output stream which does not carry over {@link #close()}.
 *
 * @since 2022/08/28
 */
public final class NonClosedOutputStream
	extends OutputStream
{
	/** The stream to target. */
	private final OutputStream _out;
	
	/**
	 * Wraps the given output stream.
	 * 
	 * @param __out The stream to wrap.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/28
	 */
	public NonClosedOutputStream(OutputStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this._out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/28
	 */
	@Override
	public void close()
		throws IOException
	{
		// Does nothing
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/28
	 */
	@Override
	public void flush()
		throws IOException
	{
		this._out.flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/28
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		this._out.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/28
	 */
	@Override
	public void write(byte[] __b)
		throws IOException
	{
		this._out.write(__b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/28
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		this._out.write(__b, __o, __l);
	}
}
