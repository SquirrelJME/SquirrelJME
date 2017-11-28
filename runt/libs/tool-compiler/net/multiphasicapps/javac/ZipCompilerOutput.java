// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac;

import java.io.Closeable;
import java.io.IOException;
import java.io.OutputStream;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This is used to wrap output to a ZIP file.
 *
 * @since 2017/11/28
 */
public final class ZipCompilerOutput
	implements Closeable, CompilerOutput
{
	/**
	 * Initialize the output which writes to the given ZIP file.
	 *
	 * @param __out The output ZIP to write to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/28
	 */
	public ZipCompilerOutput(ZipStreamWriter __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void close()
		throws IOException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public void flush()
		throws CompilerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/28
	 */
	@Override
	public OutputStream output(String __n)
		throws CompilerException, NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

