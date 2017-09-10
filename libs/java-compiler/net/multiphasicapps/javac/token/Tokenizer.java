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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * This class is the tokenizer which is used to provide tokens.
 *
 * @since 2017/09/04
 */
public class Tokenizer
{
	/** The number of characters in the queue. */
	private static final int _QUEUE_SIZE =
		8;
	
	/** Input character source. */
	protected final LogicalReader in;
	
	/** Is there a character waiting? */
	private volatile boolean _nxwaiting;
	
	/** The next character that is waiting. */
	private volatile int _nxchar;
	
	/** The next line. */
	private volatile int _nxline;
	
	/** The next column. */
	private volatile int _nxcolumn;
	
	/** The current line. */
	private volatile int _curline;
	
	/** The current column. */
	private volatile int _curcolumn;
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __is The tokenizer input, it is treated as UTF-8.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If UTF-8 is not supported, but this should
	 * not occur.
	 * @since 2017/09/04
	 */
	public Tokenizer(InputStream __is)
		throws NullPointerException, RuntimeException
	{
		this(__wrap(__is));
	}
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __r The reader to read characters from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	public Tokenizer(Reader __r)
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		this.in = new LogicalReader(__r);
	}
	
	/**
	 * Returns the next token.
	 *
	 * @return The next token or {@code null} if none remain.
	 * @throws IOException On read errors.
	 * @throws TokenizerException If a token sequence is not valid.
	 * @since 2017/09/05
	 */
	public Token next()
		throws IOException, TokenizerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the next character.
	 *
	 * @return The next character.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private int __next()
		throws IOException
	{
		// Peek character so it is in the buffer
		if (!this._nxwaiting)
			__peek();
		
		// Position needed for finding errors
		this._curline = this._nxline;
		this._curcolumn = this._nxcolumn;
		
		// Use the next character
		int rv = this._nxchar;
		this._nxwaiting = false;
		return rv;
	}
	
	/**
	 * Peeks the next character.
	 *
	 * @return The next character.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private int __peek()
		throws IOException
	{
		// Use the pre-existing character
		if (this._nxwaiting)
			return this._nxchar;
		
		// Read in next character
		LogicalReader in = this.in;
		int ln = in.line(),
			cl = in.column(),
			c = in.read();
		
		// Set details
		this._nxchar = c;
		this._nxline = ln;
		this._nxcolumn = cl;
		this._nxwaiting = true;
		
		// Need to know the actual character
		return c;
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

