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
import java.io.InputStream;
import org.jetbrains.annotations.NotNull;

/**
 * A native input stream.
 *
 * @since 2024/02/04
 */
public class NativeInputStream
	extends InputStream
{
	/** The stream to read from. */
	protected final Pointer stream;
	
	/**
	 * Initializes the input stream.
	 *
	 * @param __stream The direct stream to read from.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/04
	 */
	public NativeInputStream(Pointer __stream)
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
	public int available()
		throws IOException
	{
		throw Debugging.todo();
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
	public int read()
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public int read(@NotNull byte[] __b)
		throws IOException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/04
	 */
	@Override
	public int read(@NotNull byte[] __b, int __o, int __l)
		throws IOException
	{
		throw Debugging.todo();
	}
}
