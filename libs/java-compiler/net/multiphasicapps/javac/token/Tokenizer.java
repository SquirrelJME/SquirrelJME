// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.token;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is the tokenizer which is used to provide tokens.
 *
 * @since 2017/09/04
 */
public class Tokenizer
{
	/** Input character source. */
	protected final Reader in;
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __is The tokenizer input, it is treated as UTF-8.
	 * @param __comments Should comments be included?
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If UTF-8 is not supported, but this should
	 * not occur.
	 * @since 2017/09/04
	 */
	public Tokenizer(InputStream __is, boolean __comments)
		throws NullPointerException, RuntimeException
	{
		this(__wrap(__is), __comments);
	}
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __r The reader to read characters from.
	 * @param __comments Should comments be included?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	public Tokenizer(Reader __r, boolean __comments)
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		this.in = new __DeUnicodeEscape__(__r);
	}
	
	/**
	 * Runs the tokenizer.
	 *
	 * @return The list of parsed tokens.
	 * @throws IOException On read/write errors.
	 * @since 2017/09/04
	 */
	public List<Token> run()
		throws IOException
	{
		Reader in = this.in;
		int c;
		while ((c = in.read()) >= 0)
			System.err.print((char)c);
		System.err.println();
		
		throw new todo.TODO();
	}
	
	/**
	 * Wraps the input stream for reading UTF-8.
	 *
	 * @param __is The read to read from.
	 * @return The wrapped reader.
	 * @throws RuntimeException If UTF-8 is not supported but this should
	 * never happen.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	private static Reader __wrap(InputStream __is)
		throws RuntimeException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Could fail, but it never should
		try
		{
			return new InputStreamReader(__is, "utf-8");
		}
		
		// Should never happen
		catch (UnsupportedEncodingException e)
		{
			throw new RuntimeException("OOPS", e);
		}
	}
}

