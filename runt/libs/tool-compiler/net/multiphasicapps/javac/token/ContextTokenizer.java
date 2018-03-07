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

/**
 * This is a tokenizer.
 *
 * @since 2018/03/07
 */
public final class ContextTokenizer
	implements Closeable, LineAndColumn
{
	/** The bottom tokenizer to use. */
	protected final BottomTokenizer bottom;
	
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
	 * @since 2018/03/07
	 */
	public final ContextToken next()
	{
		throw new todo.TODO();
	}
}

