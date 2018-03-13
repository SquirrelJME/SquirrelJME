// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.javac.FileNameLineAndColumn;
import net.multiphasicapps.javac.token.Token;
import net.multiphasicapps.javac.token.Tokenizer;
import net.multiphasicapps.javac.token.TokenType;

/**
 * This layers on top of the tokenizer and allows for special handling of
 * comments and input tokens so that parsing of the input file is made simpler.
 *
 * Decomposing is enabled by default and it translate angle brackets from
 * their combined operator forms to simpler single symbols.
 *
 * @since 2018/03/12
 */
public final class TokenizerLayer
	implements Closeable, FileNameLineAndColumn
{
	/** The tokenizer this is laid ontop of. */
	protected final Tokenizer tokenizer;
	
	/** This stores the temporary token queue which allows for peeking. */
	private final LinkedList<LayeredToken> _queue =
		new LinkedList<>();
	
	/** Decomposed bracket tokens, if that is enabled. */
	private final Deque<LayeredToken> _decompqueue =
		new ArrayDeque<>();
	
	/** Pusher for comment tokens. */
	private final List<Token> _commentpush =
		new ArrayList<>();
	
	/** Are brackets to be decomposed into simpler parts? */
	private volatile boolean _decompose =
		true;
	
	/** The last token which was read. */
	private volatile FileNameLineAndColumn _last;
	
	/**
	 * This initializes the tokenizer layer which allows for slight
	 * modification of input tokens accordingly.
	 *
	 * @param __t The tokenizer to layer on top.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	public TokenizerLayer(Tokenizer __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		this.tokenizer = __t;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final void close()
		throws IOException
	{
		this.tokenizer.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int column()
	{
		FileNameLineAndColumn last = this._last;
		if (last != null)
			return last.column();
		return -1;
	}
	
	/**
	 * Sets whether tokens are to be decomposed.
	 *
	 * @param __d If {@code true} then brackets are decomposed into
	 * simpler parts.
	 * @since 2018/03/12
	 */
	public final void decompose(boolean __d)
	{
		this._decompose = __d;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		FileNameLineAndColumn last = this._last;
		if (last != null)
			return last.fileName();
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final int line()
	{
		FileNameLineAndColumn last = this._last;
		if (last != null)
			return last.line();
		return -1;
	}
	
	/**
	 * Returns the next token in the queue.
	 *
	 * This method may return different sets of tokens from peeked tokens if
	 * they are to be decomposed.
	 *
	 * @return The next token in the queue or end of file tokens if there are
	 * no more tokens.
	 * @throws IOException On read errors.
	 * @since 2018/03/12
	 */
	public final LayeredToken next()
		throws IOException
	{
		LayeredToken rv;
		
		// If there are decomposed tokens waiting then return those first
		// because they got generated
		Deque<LayeredToken> decompqueue = this._decompqueue;
		if (!decompqueue.isEmpty())
		{
			rv = decompqueue.removeFirst();
			this._last = rv;
			return rv;
		}
		
		// Peek a single token and remove it from the queue
		this.peek();
		rv = this._queue.removeFirst();
		
		// If decomposing, turn bitshifts into single angle brackets
		if (this._decompose)
		{
			// Only >> and >>> need to be decomposed. The comparison
			// and assignment operators do not need to be included at all
			// because in the normal code sense there will never be an
			// assignment following a generic
			// The left side does not need to be decomposed because it always
			// will be split by another identifier.
			TokenType type = rv.type();
			int decompcount;
			switch (type)
			{
				case OPERATOR_SSHIFT_RIGHT:
					decompcount = 2;
					break;
					
				case OPERATOR_USHIFT_RIGHT:
					decompcount = 3;
					break;
				
					// Not decomposed
				default:
					decompcount = 0;
					break;
			}
			
			// Decompose the right angle brackets
			if (decompcount > 0)
			{
				String fn = rv.fileName();
				int ln = rv.line(),
					co = rv.column();
				
				for (int i = 0; i < decompcount; i++)
					decompqueue.addLast(new LayeredToken(new Token(
						TokenType.COMPARE_GREATER_THAN, ">", fn, ln, co),
						(i == 0 ? rv.comments() : new Token[0])));
			}
			
			// Use decomposed tokens if any were added (check again)
			if (!decompqueue.isEmpty())
				rv = decompqueue.removeFirst();
		}
		
		// Use that original token
		this._last = rv;
		return rv;
	}
	
	/**
	 * Returns the token that would be returned on the call to {@link #next()}
	 *
	 * @return The next token that would be returned.
	 * @throws IOException On read errors.
	 * @since 2018/03/12
	 */
	public final LayeredToken peek()
		throws IOException
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
	 * @throws IOException On read errors.
	 * @since 2018/03/12
	 */
	public final LayeredToken peek(int __o)
		throws IndexOutOfBoundsException, IOException
	{
		// {@squirreljme.error AQ12 Cannot peek a token with a negative
		// offset.}
		if (__o < 0)
			throw new IndexOutOfBoundsException("AQ12");
		
		// Keep filling the queue with tokens as needed
		LinkedList<LayeredToken> queue = this._queue;
		List<Token> commentpush = this._commentpush;
		Tokenizer tokenizer = this.tokenizer;
		int qsize;
		while ((qsize = queue.size()) <= __o)
		{
			// Consume a new token, if it returned null then there was an EOF
			// so just return an infinite amount of EOF tokens
			Token next = tokenizer.next();
			if (next == null)
				next = new Token(TokenType.END_OF_FILE, "",
					tokenizer.fileName(), tokenizer.line(),
					tokenizer.column());
			
			// If this is a comment, push it because comments are never
			// considered as parsing input (they are just notes)
			if (next.type() == TokenType.COMMENT)
			{
				commentpush.add(next);
				continue;
			}
			
			// Generate token
			LayeredToken gen;
			if (commentpush.isEmpty())
				gen = new LayeredToken(next);
			else
			{
				gen = new LayeredToken(next, commentpush.<Token>toArray(
					new Token[commentpush.size()]));
				commentpush.clear();
			}
			
			// Store token for later usage
			queue.addLast(gen);
		}
		
		// Use that given token
		if (__o == 0)
			return queue.getFirst();
		else if (__o == qsize - 1)
			return queue.getLast();
		return queue.get(__o);
	}
}

