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
	
	/** Comment tokens which have been queued. */
	private final Deque<BottomToken> _commentqueue =
		new ArrayDeque<>();
	
	/** The stack for context parsing. */
	private final Deque<__Context__> _stack =
		new ArrayDeque<>();
	
	/** The queue for the bottom tokens since it is peekless. */
	private volatile BottomToken _bottomqueue;
	
	/** The last read token. */
	private volatile ContextToken _lasttoken;
	
	/** The last bottom token. */
	private volatile BottomToken _lastbottom;
	
	/**
	 * Initializes the context stack.
	 *
	 * @since 2018/03/07
	 */
	{
		Deque<__Context__> stack = this._stack;
		stack.addLast(new __ContextIntroPackage__());
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
		BottomToken lastbottom = this._lastbottom;
		if (lastbottom != null)
			return lastbottom.column();
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
		BottomToken lastbottom = this._lastbottom;
		if (lastbottom != null)
			return lastbottom.line();
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
	 * Returns the next bottom token.
	 *
	 * @return The next bottom token.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	private final BottomToken __bottomNext()
		throws IOException
	{
		return this.__bottomNext(false);
	}
	
	/**
	 * Returns the next bottom token.
	 *
	 * @param __caneof Can this EOF?
	 * @return The next bottom token.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	private final BottomToken __bottomNext(boolean __caneof)
		throws IOException
	{
		// {@squirreljme.error AQ13 Unexpected end of file while obtaining
		// the next token.}
		BottomToken next = this.__bottomPeek(__caneof);
		if (next == null && !__caneof)
			throw new TokenizerException(this.bottom, "AQ13");
		
		// Make sure it is consumed
		this._bottomqueue = null;
		return next;
	}
	
	/**
	 * Peeks the next bottom token.
	 *
	 * @return The peeked bottom token that would be returned.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	private final BottomToken __bottomPeek()
		throws IOException
	{
		return this.__bottomPeek(false);
	}
	
	/**
	 * Peeks the next bottom token.
	 *
	 * @param __caneof Can this EOF?
	 * @return The peeked bottom token that would be returned.
	 * @throws IOException On read errors.
	 * @since 2018/03/07
	 */
	private final BottomToken __bottomPeek(boolean __caneof)
		throws IOException
	{
		BottomToken peek = this._bottomqueue;
		if (peek != null)
			return peek;
		
		Deque<BottomToken> commentqueue = this._commentqueue;
		for (;;)
		{
			// There may be comments in the incoming token stream so store them
			// accordingly
			peek = this.bottom.next();
			if (peek != null && peek.isComment())
			{
				commentqueue.addLast(peek);
				peek = null;
				continue;
			}
			
			// Use this token
			this._bottomqueue = peek;
			
			// {@squirreljme.error AQ14 Unexpected end of file while obtaining
			// the next token.}
			if (peek == null && !__caneof)
				throw new TokenizerException(this.bottom, "AQ14");
			break;
		}
		return peek;
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
		// Constantly try filling tokens
		Deque<__Context__> stack = this._stack;
		Deque<ContextToken> queue = this._queue;
		for (;;)
		{
			// Used to detect EOF if the stack is empty
			__Context__ context = stack.peekLast();
			if (context == null)
				return queue.peekFirst();
			
			// Depends on the area
			ContextArea area = context.area;
			switch (area)
			{
				case INTRO_PACKAGE:
					__runIntroPackage((__ContextIntroPackage__)context);
					break;
				
				case INTRO_IMPORTS:
					__runIntroImports((__ContextIntroImports__)context);
					break;
			
					// Not implemented
				default:
					throw new RuntimeException(String.format("OOPS %s", area));
			}
		
			// If no tokens were enqueued then try again because some
			// parsers might switch state without generating any tokens
			if (queue.isEmpty())
				continue;
			break;
		}
		
		return queue.peekFirst();
	}
	
	/**
	 * Parses import statements.
	 *
	 * @param __context THe input context.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/08
	 */
	private final void __runIntroImports(__ContextIntroImports__ __context)
		throws IOException, NullPointerException
	{
		if (__context == null)
			throw new NullPointerException("NARG");
		
		BottomToken peek = this.__bottomPeek(),
			base = peek;
		BottomType type = peek.type();
		
		// Import statement
		if (type == BottomType.KEYWORD_IMPORT)
		{
			// Consume import
			this.__bottomNext();
			
			/** Static import. */
			boolean isstatic = false;
			peek = this.__bottomPeek();
			if (peek.type() == BottomType.KEYWORD_STATIC)
			{
				isstatic = true;
				this.__bottomNext();
			}
			
			// Target string for the import identifier
			StringBuilder sb = new StringBuilder();
			
			// Reading loop, identifier word followed by dot or semicolon
			// May contain asterisk potentially
			for (boolean firstrun = true;;)
			{
				BottomToken next = this.__bottomNext();
				
				// Need to detect wildcards
				boolean iswildcard = false;
				if (next.type != BottomType.IDENTIFIER)
				{
					// {@squirreljme.error AQ19 Expected identifier while
					// parsing import statement. (The token)}
					if (firstrun)
						throw new TokenizerException(next,
							String.format("AQ19 %s", next));
					
					// {@squirreljme.error AQ20 Expected either identifier
					// or asterisk whule parsing import statement. (The token)}
					if (next.type != BottomType.OPERATOR_MULTIPLY)
						throw new TokenizerException(next,
							String.format("AQ20 %s", next));
					iswildcard = true;
				}
				
				// Use this identifier
				sb.append(next.characters());
				
				// Wildcard statements must end
				next = this.__bottomNext();
				type = next.type();
				if (iswildcard)
				{
					// {@squirreljme.error AQ21 Expected semicolon to follow
					// asterisk in wildcard import.}
					if (type != BottomType.SYMBOL_SEMICOLON)
						throw new TokenizerException(next,
							String.format("AQ21 %s", next));
					
					// Either 
					this.__token((isstatic ?
						ContextType.IMPORT_STATIC_MEMBERS :
						ContextType.IMPORT_PACKAGE), base, sb);
					return;
				}
				
				// Adding another identifier?
				else if (type == BottomType.SYMBOL_DOT)
				{
					sb.append('.');
					continue;
				}
				
				// Import has finished
				else if (type == BottomType.SYMBOL_SEMICOLON)
				{
					this.__token((isstatic ?
						ContextType.IMPORT_STATIC_MEMBER :
						ContextType.IMPORT_CLASS), base, sb);
					return;
				}
				
				// {@squirreljme.error AQ22 Expected either a dot or
				// semi-colon in the import statement. (The token)}
				else
					throw new TokenizerException(next,
						String.format("AQ22 %s", next));
			}
		}
		
		// Potential start of class, switch
		else if (type.isPotentialClassStart())
		{
			throw new todo.TODO();
		}
		
		// {@squirreljme.error AQ18 Unxpected token while looking for import
		// statements.}
		else
			throw new TokenizerException(peek, String.format("AQ18 %s", peek));
	}
	
	/**
	 * Decodes the introductory sequence.
	 *
	 * @param __context The input context.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	private final void __runIntroPackage(__ContextIntroPackage__ __context)
		throws IOException, NullPointerException
	{
		if (__context == null)
			throw new NullPointerException("NARG");
		
		BottomToken peek = this.__bottomPeek(),
			base = peek;
		BottomType type = peek.type();
		
		// Package delaration
		if (type == BottomType.KEYWORD_PACKAGE)
		{
			// Consume package
			this.__bottomNext();
			
			// Target string for the package identifier
			StringBuilder sb = new StringBuilder();
			
			// Reading loop, identifier word followed by dot or semicolon
			for (;;)
			{
				BottomToken next = this.__bottomNext();
				
				// {@squirreljme.error AQ16 Expected identifier while parsing
				// the package. (The token)}
				if (next.type != BottomType.IDENTIFIER)
					throw new TokenizerException(next,
						String.format("AQ16 %s", next));
				
				// Use this identifier
				sb.append(next.characters());
				
				// Adding another identifier?
				next = this.__bottomNext();
				type = next.type();
				if (type == BottomType.SYMBOL_DOT)
				{
					sb.append('.');
					continue;
				}
				
				// No more
				else if (type == BottomType.SYMBOL_SEMICOLON)
					break;
				
				// {@squirreljme.error AQ17 Expected either a dot or
				// semi-colon in the package statement. (The token)}
				else
					throw new TokenizerException(next,
						String.format("AQ17 %s", next));
			}
			
			// Build token
			this.__token(ContextType.PACKAGE_DECLARATION, base, sb);
			
			// Start reading imports
			this.__stackReplace(new __ContextIntroImports__());
			
			// Done
			return;
		}
		
		// Import statement
		else if (type == BottomType.KEYWORD_IMPORT)
		{
			// Go straight to import processing
			this.__stackReplace(new __ContextIntroImports__());
			return;
		}
		
		// Potential start of class, switch
		else if (type.isPotentialClassStart())
		{
			throw new todo.TODO();
		}
		
		// {@squirreljme.error AQ15 Unexpected token while searching for the
		// package keyword or other parts.}
		else
			throw new TokenizerException(peek, String.format("AQ15 %s", peek));
	}
	
	/**
	 * Adds the specified token to the queue.
	 *
	 * @param __t The type of token this is.
	 * @param __w Hint to use for the location of the token, may be
	 * {@code null}.
	 * @param __sb The input string sequence.
	 * @return The created token.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/08	 
	 */
	private final ContextToken __token(ContextType __t, LineAndColumn __w,
		CharSequence __sb)
		throws NullPointerException
	{
		if (__t == null || __sb == null)
			throw new NullPointerException("NARG");
		
		// Did not say where the token was
		if (__w == null)
			__w = this.bottom;
		
		// Drain all comments for this token
		Deque<BottomToken> commentqueue = this._commentqueue;
		int cn = commentqueue.size();
		String[] comments = new String[cn];
		for (int i = 0; i < cn; i++)
			comments[i] = commentqueue.removeFirst().characters();
		
		// Push new token to the queue
		ContextToken rv = new ContextToken(this._stack.peekLast().area,
			__t, __sb.toString(), __w.line(), __w.column(), comments);
		this._queue.addLast(rv);
		return rv;
	}
	
	/**
	 * Replaces the top stack entry with the new type.
	 *
	 * @param __new The new context.
	 * @return The new context.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/08
	 */
	private final __Context__ __stackReplace(__Context__ __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// Replace the top
		Deque<__Context__> stack = this._stack;
		stack.removeLast();
		stack.addLast(__new);
		
		return __new;
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
	 * Read of import statement.
	 *
	 * @since 2018/03/08
	 */
	private final class __ContextIntroImports__
		extends __Context__
	{
		/**
		 * Initializes the context.
		 *
		 * @since 2018/03/08
		 */
		private __ContextIntroImports__()
		{
			super(ContextArea.INTRO_IMPORTS);
		}
	}
	
	/**
	 * Reading of the package statement.
	 *
	 * @since 2018/03/07
	 */
	private final class __ContextIntroPackage__
		extends __Context__
	{
		/**
		 * Initializes the context.
		 *
		 * @since 2018/03/07
		 */
		private __ContextIntroPackage__()
		{
			super(ContextArea.INTRO_PACKAGE);
		}
	}
}

