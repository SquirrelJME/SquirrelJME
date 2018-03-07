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

import java.io.Closeable;
import java.io.InputStream;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * This is a tokenizer which generates tokens which are context sensitive in
 * that it determine the types of tokens based on the prevously read tokens.
 *
 * @since 2018/03/07
 */
public final class ContextTokenizer
	implements Closeable, LineAndColumn
{
	/** The bottom tokenizer to use. */
	protected final BottomTokenizer bottom;
	
	/** The queue for the tokenizer. */
	protected final Deque<ContextToken> _queue =
		new ArrayDeque<>();
	
	/** The last read token. */
	private volatile ContextToken _lasttoken;
	
	/**
	 * Initializes the context sensitive tokenizer.
	 *
	 * @param __in The input characters.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	public ContextTokenizer(InputStream __in)
		throws NullPointerException
	{
		this(new BottomTokenizer(__in));
	}
	
	/**
	 * Initializes the context sensitive tokenizer.
	 *
	 * @param __in The input characters.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	public ContextTokenizer(Reader __in)
		throws NullPointerException
	{
		this(new BottomTokenizer(__in));
	}
	
	/**
	 * Initializes the context sensitive tokenizer.
	 *
	 * @param __b The bottom tokenizer to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	public ContextTokenizer(BottomTokenizer __b)
		throws NullPointerException
	{
		if (__b == null)
			throw new NullPointerException("NARG");
		
		this.bottom = __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.bottom.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int column()
	{
		ContextToken lasttoken = this._lasttoken;
		if (lasttoken != null)
			return lasttoken.column();
		return this.bottom.column();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int line()
	{
		ContextToken lasttoken = this._lasttoken;
		if (lasttoken != null)
			return lasttoken.line();
		return this.bottom.line();
	}
	
	/**
	 * Returns the next token that is available from the input.
	 *
	 * @return The next token or {@code null} if there is none.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	public final ContextToken next()
		throws IOException
	{
		Deque<ContextToken> queue = this._queue;
		if (queue.isEmpty())
			this.__fill();
		
		// Use the last token to determine where errors may have occurred
		ContextToken last = queue.pollFirst();
		this._lasttoken = last;
		return last;
	}
	
	/**
	 * Returns the token that will be returned on the next read without
	 * removing it from the input queue.
	 *
	 * @return The next token that will be returned or {@code null} if there
	 * is none.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	public final ContextToken peek()
		throws IOException
	{
		Deque<ContextToken> queue = this._queue;
		if (queue.isEmpty())
			this.__fill();
		
		return queue.peekFirst();
	}
	
	/**
	 * Reads 
	 *
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	private final void __fill()
		throws IOException
	{
		throw new todo.TODO();
	}
}

