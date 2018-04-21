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
import java.util.ArrayList;
import java.util.Deque;
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
	
	/** Marks which have been made. */
	private final Deque<Integer> _marks =
		new ArrayDeque<>();
	
	/** Tokens which are currently being peeked. */
	private final List<Token> _peeked =
		new LinkedList<>();
	
	/** The current position in the peek queue where the token is at. */
	private int _peekedpos;
	
	/**
	 * Initializes the buffered token source from the given stream and input
	 * stream.
	 *
	 * @param __fn The name of the file being read.
	 * @param __in The input file stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/21
	 */
	public BufferedTokenSource(String __fn, InputStream __in)
		throws NullPointerException
	{
		this(new Tokenizer(__fn, __in));
	}
	
	/**
	 * Initializes the buffered token source from the given stream and input
	 * stream.
	 *
	 * @param __fn The name of the file being read.
	 * @param __in The input file stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/21
	 */
	public BufferedTokenSource(String __fn, Reader __in)
		throws NullPointerException
	{
		this(new Tokenizer(__fn, __in));
	}
	
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
		// Close it if the input is closeable
		TokenSource input = this.input;
		if (input instanceof Closeable)
			try
			{
				((Closeable)this.input).close();
			}
			catch (IOException e)
			{
				// {@squirreljme.error AQ3d Could not close the token source.}
				throw new TokenizerException("AQ3d", e);
			}
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
		// {@squirreljme.error AQ3e Cannot reset a mark when there have been
		// no marks which were made.}
		Deque<Integer> marks = this._marks;
		int n = marks.size();
		if (n == 0)
			throw new IllegalStateException("AQ3e");
		
		// Remove the last mark but keep the peek position the
		marks.remove(n - 1);
		
		// If this was the last mark then all tokens which were read before
		// this will just be dropped
		if (n <= 1)
		{
			// Drain all peeked tokens to the current position
			int peekedpos = this._peekedpos;
			List<Token> peeked = this._peeked;
			for (int i = 0; i < peekedpos; i++)
				peeked.remove(0);
			
			// Set the peek position to zero so that the next read position
			// is the first token in the queue
			this._peekedpos = 0;
		}
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
		// The current peek position is just stored
		this._marks.addLast(this._peekedpos);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/04/18
	 */
	@Override
	public final Token next()
	{
		// Always peek the first token to be returned
		Token rv = this.peek(0);
		
		// If there are no marks that exist, 
		int peekedpos = this._peekedpos;
		Deque<Integer> marks = this._marks;
		if (marks.isEmpty())
			this._peeked.remove(0);
		
		// Otherwise increase the peek index so that the next token is offset
		// by the peek position
		else
			this._peekedpos = peekedpos + 1;
		
		// Debug
		todo.DEBUG.note("Next (%d, m=%d): %s", peekedpos, marks.size(), rv);
		
		return rv;
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
		
		TokenSource input = this.input;
		
		// Offset by the peek position because if marking is enabled then peek
		// mode will always be used
		__o += this._peekedpos;
		
		// Need to fill the queue with peeked tokens in order to peek this
		// far ahead?
		List<Token> peeked = this._peeked;
		int n = peeked.size();
		while (__o >= n)
			peeked.add(n++, input.next());
		
		// Return the peeked token here
		return peeked.get(__o);
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
		// {@squirreljme.error AQ3e Cannot reset a mark when there have been
		// no marks which were made.}
		Deque<Integer> marks = this._marks;
		int n = marks.size();
		if (n == 0)
			throw new IllegalStateException("AQ3e");
		
		// The peek position becomes the index of the last mark
		this._peekedpos = marks.removeLast();
	}
}

