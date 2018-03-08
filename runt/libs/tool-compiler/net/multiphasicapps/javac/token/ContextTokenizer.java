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
	private final Deque<ContextToken> _queue =
		new ArrayDeque<>();
	
	/** The stack for context parsing. */
	private final Deque<__Context__> _stack =
		new ArrayDeque<>();
	
	/** The last read token. */
	private volatile ContextToken _lasttoken;
	
	/**
	 * Initializes the context stack.
	 *
	 * @since 2018/03/07
	 */
	{
		Deque<__Context__> stack = this._stack;
		stack.addLast(new __ContextIntro__());
	}
	
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
	 * Reads input tokens from the bottom tokenizer and generates context
	 * based tokens for simpler parsing.
	 *
	 * @return The next token in the queue.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	private final ContextToken __fill()
		throws IOException
	{
		// Used to detect EOF if the stack is empty
		Deque<__Context__> stack = this._stack;
		__Context__ context = stack.peekLast();
		if (context == null)
			return null;
		
		// Depends on the area
		ContextArea area = context.area;
		switch (area)
		{
			case INTRO:
				__runIntro((__ContextIntro__)context);
				break;
			
				// Not implemented
			default:
				throw new RuntimeException(String.format("OOPS %s", area));
		}
		
		// {@squirreljme.error AQ12 Enqueued no input tokens during token
		// parsing when not in the EOF state.}
		Deque<ContextToken> queue = this._queue;
		if (queue.isEmpty() && !stack.isEmpty())
			throw new TokenizerException(this, "AQ12");
		return queue.peekFirst();
	}
	
	/**
	 * Decodes the introductory sequence.
	 *
	 * @param __context The input context.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	private final void __runIntro(__ContextIntro__ __context)
		throws IOException, NullPointerException
	{
		if (__context == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * This is the base class for context sensitive parsers for input
	 * tokens. This interface is just used as a base for state storage.
	 *
	 * @since 2018/03/07
	 */
	private abstract class __Context__
	{
		/** The context area. */
		public final ContextArea area;
		
		/**
		 * Initializes the base context.
		 *
		 * @param __a The area this is in.
		 * @throws NullPointerException On null arguments.
		 * @since 2018/03/07
		 */
		private __Context__(ContextArea __a)
			throws NullPointerException
		{
			if (__a == null)
				throw new NullPointerException("NARG");
			
			this.area = __a;
		}
	}
	
	/**
	 * This is the area for the introduction to the class file and is used
	 * to store data for the package and imports.
	 *
	 * @since 2018/03/07
	 */
	private final class __ContextIntro__
		extends __Context__
	{
		/**
		 * Initializes the context.
		 *
		 * @since 2018/03/07
		 */
		private __ContextIntro__()
		{
			super(ContextArea.INTRO);
		}
	}
}

