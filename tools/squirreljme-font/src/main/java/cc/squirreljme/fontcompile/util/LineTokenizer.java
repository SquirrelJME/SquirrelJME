// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.fontcompile.util;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

/**
 * Tokenizes line inputs.
 *
 * @since 2024/05/24
 */
public class LineTokenizer
	implements Closeable
{
	/** The stream to read from. */
	protected final BufferedReader in;
	
	/**
	 * Initializes the tokenizer.
	 *
	 * @param __in The input source.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	public LineTokenizer(InputStream __in)
		throws NullPointerException
	{
		this(__in, "utf-8");
	}
	
	/**
	 * Initializes the tokenizer.
	 *
	 * @param __in The input source.
	 * @param __encoding The encoding to use.
	 * @throws IllegalArgumentException If the encoding is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	public LineTokenizer(InputStream __in, String __encoding)
		throws IllegalArgumentException, NullPointerException
	{
		this(LineTokenizer.__wrap(__in, __encoding));
	}
	
	/**
	 * Initializes the tokenizer.
	 *
	 * @param __in The input source.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	public LineTokenizer(Reader __in)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = new BufferedReader(__in);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/05/24
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * Wraps the input so it does not throw {@link IOException}.
	 *
	 * @param __in The stream to wrap.
	 * @param __encoding The encoding to use.
	 * @return The wrapped reader.
	 * @throws IllegalArgumentException If the encoding is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/05/24
	 */
	private static Reader __wrap(InputStream __in, String __encoding)
		throws IllegalArgumentException, NullPointerException
	{
		if (__in == null || __encoding == null)
			throw new NullPointerException("NARG");
		
		try
		{
			return new InputStreamReader(__in, __encoding);
		}
		catch (UnsupportedEncodingException __e)
		{
			throw new IllegalArgumentException(__e);
		}
	}
}
