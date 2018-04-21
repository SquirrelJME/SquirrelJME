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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.javac.FileNameLineAndColumn;

/**
 * This is a token source which performs buffering of an input set of tokens
 * enabling token sources and such to be marked, committed, and reset similarly
 * to input streams. It additionally provides peeking functionality.
 *
 * The marking system is stack based and as such multiple marks will push
 * onto other marks, while commits or resets will pop those marks.
 *
 * @since 2018/04/18
 */
public final class BufferedTokenSource
	implements Closeable, FileNameLineAndColumn, TokenSource
{
	/** The input token source to read from. */
	protected final TokenSource input;
	
	/**
	 * Initializes the buffered token source.
	 *
	 * @param __t The input token source.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/18
	 */
	public BufferedTokenSource(TokenSource __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.input = __t;
	}
	
	/**
	 * Closes the input token source.
	 *
	 * @throws TokenizerException If it cannot be closed.
	 * @since 2018/04/18
	 */
	@Override
	public final void close()
		throws TokenizerException
	{
		this.input.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/18
	 */
	@Override
	public final int column()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Commits the current position in the marked set (and internally removes
	 * the mark) so that the token stream is set to the given position rather
	 * than being used as a mark. So a commit being performed later in a
	 * stream will make it so that all the previously read tokens were
	 * actually read from the input so that they cannot be reversed.
	 *
	 * @throws IllegalStateException If there are no marks.
	 * @since 2018/04/18
	 */
	public final void commit()
		throws IllegalStateException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/18
	 */
	@Override
	public final String fileName()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/18
	 */
	@Override
	public final int line()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Marks the current position in the token stream and pushes it to the
	 * marking stack so that a future {@link #commit()} or {@link #reset()}
	 * will either set the given position or clear it.
	 *
	 * @since 2018/04/18
	 */
	public final void mark()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/18
	 */
	@Override
	public final Token next()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the token that would be returned on the call to {@link #next()}
	 *
	 * @return The next token that would be returned.
	 * @throws TokenizerException On read errors.
	 * @since 2018/03/12
	 */
	public final Token peek()
		throws TokenizerException
	{
		// Just read the first token
		return this.peek(0);
	}
	
	/**
	 * Returns the token which seen in the future from a given token offset.
	 *
	 * @param __o The number of tokens to read into the future.
	 * @return The next token that would be returned.
	 * @throws IndexOutOfBoundsException If a negative index was specified.
	 * @throws TokenizerException On read errors.
	 * @since 2018/03/12
	 */
	public final Token peek(int __o)
		throws IndexOutOfBoundsException, TokenizerException
	{
		// {@squirreljme.error AQ3c Cannot peek a token with a negative
		// offset.}
		if (__o < 0)
			throw new IndexOutOfBoundsException("AQ3c");
		
		throw new todo.TODO();
	}
	
	/**
	 * Resets and clears the last mark and returns the token source stream
	 * to the position it was marked at.
	 *
	 * @throws IllegalStateException If there are no marks.
	 * @since 2018/04/18
	 */
	public final void reset()
		throws IllegalStateException
	{
		throw new todo.TODO();
	}
}

