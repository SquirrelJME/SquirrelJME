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
import java.io.Reader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.javac.FileNameLineAndColumn;

/**
 * This layers on top of the tokenizer and allows for special handling of
 * comments and input tokens so that parsing of the input file is made simpler.
 *
 * The tokenizer keeps track of the number of angle brackets which are
 * opened for generics so that it can properly decode closing sequences.
 *
 * @since 2018/03/12
 */
public final class ExpandingTokenizer
	extends ExpandingSource
	implements Closeable
{
	/** The tokenizer this is laid ontop of. */
	protected final Tokenizer tokenizer;
	
	/** This stores the temporary token queue which allows for peeking. */
	private final LinkedList<ExpandedToken> _queue =
		new LinkedList<>();
	
	/** Decomposed bracket tokens, if that is enabled. */
	private final Deque<ExpandedToken> _decompqueue =
		new ArrayDeque<>();
	
	/** Pusher for comment tokens. */
	private final List<Token> _commentpush =
		new ArrayList<>();
	
	/** The number of open angle brackets. */
	private volatile int _opencount;
	
	/** Used for angle bracket decomposition. */
	private volatile TokenType _lasttype;
	
	/**
	 * Initializes the expanding tokenizer.
	 *
	 * @param __n The input name.
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public ExpandingTokenizer(String __n, InputStream __in)
		throws NullPointerException
	{
		this(new Tokenizer(__n, __in));
	}
	
	/**
	 * Initializes the expanding tokenizer.
	 *
	 * @param __n The input name.
	 * @param __in The input stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/22
	 */
	public ExpandingTokenizer(String __n, Reader __in)
		throws NullPointerException
	{
		this(new Tokenizer(__n, __in));
	}
	
	/**
	 * This initializes the tokenizer layer which allows for slight
	 * modification of input tokens accordingly.
	 *
	 * @param __t The tokenizer to layer on top.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/12
	 */
	public ExpandingTokenizer(Tokenizer __t)
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
		throws TokenizerException
	{
		this.tokenizer.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	protected final ExpandedToken readNext()
		throws TokenizerException
	{
		int opencount = this._opencount;
		try
		{
			ExpandedToken rv;
		
			// If there are decomposed tokens waiting then return those first
			// because they got generated
			Deque<ExpandedToken> decompqueue = this._decompqueue;
			if (!decompqueue.isEmpty())
			{
				rv = decompqueue.removeFirst();
				return rv;
			}
		
			// Peek a single token and remove it from the queue
			rv = this.__read();
			
			// Determine if decomposition is to be performed
			TokenType type = rv.type();
			TokenType lasttype = this._lasttype;
			int maxdecouple = 0;
			switch (type)
			{
					// Count open angle brackets but only if they follow
					// dots or identifiers
					// Foo<Bar>
					// foo.<Bar>boop()
				case COMPARE_LESS_THAN:
					if (lasttype != null &&
						(lasttype == TokenType.IDENTIFIER ||
						lasttype == TokenType.SYMBOL_DOT))
						opencount++;
					break;
				
					// Closing bracket, never close it negatively
				case COMPARE_GREATER_THAN:
					if (opencount > 0)
						opencount--; 
					break;
				
					// Decouple two closers
				case OPERATOR_SSHIFT_RIGHT:
					maxdecouple = 2;
					break;
				
					// Decouple three closers
				case OPERATOR_USHIFT_RIGHT:
					maxdecouple = 3;
					break;
			
					// These may appear in the middle of generic brackets so
					// if they are read do not clear the count
				case IDENTIFIER:
				case KEYWORD_EXTENDS:
				case KEYWORD_BOOLEAN:
				case KEYWORD_BYTE:
				case KEYWORD_SHORT:
				case KEYWORD_CHAR:
				case KEYWORD_INT:
				case KEYWORD_LONG:
				case KEYWORD_FLOAT:
				case KEYWORD_DOUBLE:
				case SYMBOL_OPEN_BRACKET:
				case SYMBOL_CLOSED_BRACKET:
				case SYMBOL_QUESTION:
				case SYMBOL_DOT:
				case SYMBOL_COMMA:
					break;
				
					// Every other symbol cannot appear in a generic
					// declaration so lose track of it completely
				default:
					opencount = 0;
					break;
			}
			
			// Use for later processing
			this._lasttype = type;
			
			// There are tokens which may be decoupled
			if (maxdecouple > 0)
			{
				// If there are more things to decouple than things which are
				// open then it is possible there is something illegal like
				// Foo<Bar>> 2 or similar
				if (maxdecouple > opencount)
					opencount = 0;
				
				// Translate the >> or >>> into multiple tokens according to
				// the decoupling count
				else
				{
					String fn = rv.fileName();
					int ln = rv.line(),
						co = rv.column();
				
					for (int i = 0; i < maxdecouple; i++)
						decompqueue.addLast(new ExpandedToken(new Token(
							TokenType.COMPARE_GREATER_THAN, ">", fn, ln, co),
							(i == 0 ? rv.comments() : new Token[0])));
					
					// Treat decoupled tokens as closing statements
					opencount -= maxdecouple;
				}
			}
			
			// Use decomposed tokens if any were added (check again)
			if (!decompqueue.isEmpty())
				rv = decompqueue.removeFirst();
			
			// Use that original token
			return rv;
		}
		
		// Always remember the open count because this needs to be stored
		// for later runs during parsing
		finally
		{
			this._opencount = opencount;
		}
	}
	
	
	/**
	 * Returns the next internal token which may be decomposed.
	 *
	 * @return The next input token.
	 * @throws IOException On read errors.
	 * @since 2018/03/12
	 */
	private final ExpandedToken __read()
		throws TokenizerException
	{
		List<Token> commentpush = this._commentpush;
		for (;;)
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
			ExpandedToken gen;
			if (commentpush.isEmpty())
				gen = new ExpandedToken(next);
			else
			{
				gen = new ExpandedToken(next, commentpush.<Token>toArray(
					new Token[commentpush.size()]));
				commentpush.clear();
			}
			
			// Use that token
			return gen;
		}
	}
}

