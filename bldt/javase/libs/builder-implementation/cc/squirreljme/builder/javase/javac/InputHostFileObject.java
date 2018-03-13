// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.javase.javac;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import net.multiphasicapps.javac.CompilerInput;

/**
 * This is a file object which is used for input.
 *
 * @since 2017/11/29
 */
public class InputHostFileObject
	extends HostFileObject
{
	/** The file to source input from. */
	protected final CompilerInput input;
	
	/**
	 * Initializes the file object for input.
	 *
	 * @param __i The stream for input of classes and sources.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/11/29
	 */
	public InputHostFileObject(CompilerInput __i)
		throws NullPointerException
	{
		super(__i.fileName());
		
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.input = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public CharSequence getCharContent(boolean __a)
		throws IOException
	{
		// Read in all the characters
		StringBuilder sb = new StringBuilder();
		try (Reader r = new InputStreamReader(openInputStream(), "utf-8"))
		{
			char[] buf = new char[2048];
			for (;;)
			{
				int rc = r.read(buf);
				
				// EOF?
				if (rc < 0)
					break;
				
				// Write
				sb.append(buf, 0, rc);
			}
		}
		
		// Return it
		return sb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public InputStream openInputStream()
		throws IOException
	{
		return this.input.open();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public OutputStream openOutputStream()
		throws IOException
	{
		// {@squirreljme.error BM07 Attempted to open input file as output.}
		throw new IllegalStateException("BM07");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Reader openReader(boolean __a)
		throws IOException
	{
		return new InputStreamReader(openInputStream(), "utf-8");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/11/29
	 */
	@Override
	public Writer openWriter()
		throws IOException
	{
		// {@squirreljme.error BM08 Cannot open a writer for an input file.}
		throw new IllegalStateException("BM08");
	}
}

