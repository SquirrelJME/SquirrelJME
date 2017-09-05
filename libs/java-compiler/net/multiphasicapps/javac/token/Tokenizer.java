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
	/** The number of characters in the queue. */
	private static final int _QUEUE_SIZE =
		8;
	
	/** Input character source. */
	protected final Reader in;
	
	/** Character queue. */
	private final int[] _cq =
		new int[_QUEUE_SIZE];
	
	/** The characters within the queue. */
	private volatile int _qz;
	
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
		for (;;)
		{
			__peek(_QUEUE_SIZE - 1);
			if ((c = __next()) < 0)
				break;
			System.err.print((char)c);
		}
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of characters available in the queue.
	 *
	 * @return The number of available queue characters.
	 * @since 2017/09/05
	 */
	private int __available()
	{
		return this._qz;
	}
	
	/**
	 * Consumes the specified number of characters without returning them.
	 *
	 * @param __n The number of characters to consume.
	 * @throws IndexOutOfBoundsException If the after count is negative or
	 * exceeds the number of available characters in the queue.
	 */
	private void __consume(int __n)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error AQ02 Cannot consume more characters that are
		// waiting in the queue. (The number of characters to consume; The
		// number of characters in the queue)}
		int qz = this._qz;
		if (__n < 0 || __n > qz)
			throw new IndexOutOfBoundsException(String.format("AQ02 %d %d",
				__n, qz));
		
		// If consuming all the characters the queue does not need shuffling
		if (qz == __n)
		{
			this._qz = 0;
			return;
		}
		
		// Not consuming anything?
		if (__n == 0)
			return;
		
		// Shift characters down
		int[] cq = this._cq;
		for (int i = 0, b = __n; i < __n; i++, b++)
			cq[i] = cq[b];
		this._qz = qz - __n;
	}
	
	/**
	 * Returns the next character.
	 *
	 * @return The next character.
	 * @throws IOException On read errors.
	 * @since 2017/09/05
	 */
	private int __next()
		throws IOException
	{
		// Just a peek followed by a consume
		int rv = __peek();
		__consume(1);
		return rv;
	}
	
	/**
	 * Peeks the next character without removing it from the queue.
	 *
	 * @return The next character.
	 * @throws IOException On read errors.
	 * @since 2017/09/05
	 */
	private int __peek()
		throws IOException
	{
		return __peek(0);
	}
	
	/**
	 * Peeks the next character without removing it from the queue.
	 *
	 * @param __a The character ahead of the current position to return.
	 * @return The next character.
	 * @throws IndexOutOfBoundsException If the after count is negative or
	 * exceeds the queue size.
	 * @throws IOException On read errors.
	 * @since 2017/09/05
	 */
	private int __peek(int __a)
		throws IndexOutOfBoundsException, IOException
	{
		// Check
		if (__a < 0 || __a >= _QUEUE_SIZE)
			throw new IndexOutOfBoundsException(String.format("AQ01 %d", __a));
		
		// No need to read in more characters
		int[] cq = this._cq;
		int qz = this._qz;
		if (__a < qz)
			return cq[__a];
		
		// Read in characters
		Reader in = this.in;
		while (qz < __a)
			cq[qz++] = in.read();
		this._qz = qz;
		
		return cq[__a];
	}
	
	/**
	 * Skips the specified number of characters in the input.
	 *
	 * @param __n The number of characters to skip.
	 * @return The number of characters which were skipped before EOF was
	 * reached.
	 * @throws IOException On null arguments.
	 * @since 2017/09/05
	 */
	private int __skip(int __n)
		throws IOException
	{
		for (int i = 0; i < __n; i++)
			if (__next() < 0)
				return i;
		return __n;
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

