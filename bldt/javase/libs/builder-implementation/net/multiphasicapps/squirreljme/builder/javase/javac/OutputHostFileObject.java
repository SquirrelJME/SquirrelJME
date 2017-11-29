// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.javase.javac;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * This is a file object which is used for output.
 *
 * @since 2017/11/29
 */
public class OutputHostFileObject
	extends HostFileObject
{
	/** The output to write to. */
	protected final CompilerOutput output;
	
	/**
	 * Initializes the file output.
	 *
	 * @param __out The output to write to.
	 * @param __name The output file name.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public OutputHostFileObject(CompilerOutput __out, String __name)
		throws NullPointerException
	{
		super(__name);
		
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.output = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public CharSequence getCharContent(boolean __a)
		throws IOException
	{
		// {@squirreljme.error BM09 Cannot get character content for an output
		// file.}
		throw new IllegalStateException("BM09");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		// {@squirreljme.error BM0a Cannot open an input stream for an output
		// file.}
		throw new IllegalStateException("BM0a");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		return this.output.output(this.name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Reader openReader(boolean __a)
		throws IOException
	{
		// {@squirreljme.error BM0b Cannot open a reader for an output
		// file.}
		throw new IllegalStateException("BM0b");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Writer openWriter()
		throws IOException
	{
		return new OutputStreamWriter(this.openOutputStream(), "utf-8");
	}
}

