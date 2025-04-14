// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.nanocoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A native output stream.
 *
 * @since 2024/02/04
 */
public class NativeOutputStream
	extends OutputStream
{
	/** The stream to write to. */
	protected final Pointer stream;
	
	/**
	 * Initializes the output stream.
	 *
	 * @param __stream The direct stream to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/04
	 */
	public NativeOutputStream(Pointer __stream)
		throws NullPointerException
	{
		if (__stream == null)
			throw new NullPointerException("NARG");
		
		this.stream = __stream;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void close()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void flush()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void write(int __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void write(byte[] __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public void write(byte[] __b, int __o, int __l)
		throws IOException
	{
		throw Debugging.todo();
	}
}
