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
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.classfile.ClassFlag;
import net.multiphasicapps.classfile.ClassFlags;
import net.multiphasicapps.classfile.InvalidClassFormatException;

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
	private final Deque<__At__> _stack =
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
		Deque<__At__> stack = this._stack;
		stack.addLast(new __AtIntroPackage__());
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
		return this.__lastLineAndColumn().column();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/07
	 */
	@Override
	public final int line()
	{
		return this.__lastLineAndColumn().column();
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
		try
		{
			// Constantly try filling tokens
			Deque<__At__> stack = this._stack;
			Deque<ContextToken> queue = this._queue;
			for (;;)
			{
				// Used to detect EOF if the stack is empty
				__At__ at = stack.peekLast();
				if (at == null)
					return queue.peekFirst();
			
				// Depends on the area
				ContextArea area = at.area;
				switch (area)
				{
					case ANNOTATED_THING:
						__runAnnotatedThing((__AtAnnotatedThing__)at);
						break;
					
					case CLASS:
						__runClass((__AtClass__)at);
						break;
					
					case INTRO_PACKAGE:
						__runIntroPackage((__AtIntroPackage__)at);
						break;
				
					case INTRO_IMPORTS:
						__runIntroImports((__AtIntroImports__)at);
						break;
			
						// Not implemented
					default:
						throw new RuntimeException(
							String.format("OOPS %s", area));
				}
		
				// If no tokens were enqueued then try again because some
				// parsers might switch state without generating any tokens
				if (queue.isEmpty())
					continue;
				break;
			}
		
			return queue.peekFirst();
		}
		
		// {@squirreljme.error AQ23 The class being tokenizer would result in
		// an illegal class.}
		catch (InvalidClassFormatException e)
		{
			throw new TokenizerException(this, "AQ23", e);
		}
	}
	
	/**
	 * Returns the last lien and column to use when reporting an exception.
	 *
	 * @return The last line and column to use for reporting.
	 * @since 2018/03/10
	 */
	private final LineAndColumn __lastLineAndColumn()
	{
		LineAndColumn rv;
		
		// Prefer the last read bottom token since it would be usually
		// around where the error is
		rv = this._lastbottom;
		if (rv != null)
			return rv;
		
		// Use the last token pushed to the queue if this is EOF
		Deque<ContextToken> queue = this._queue;
		rv = queue.peekLast();
		if (rv != null)
			return rv;
		
		// Otherwise use the bottom tokenizer's position
		return this.bottom;
	}
	
	/**
	 * Parses things which are annotated.
	 *
	 * @param __at Annotated thing state.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/09
	 */
	private final void __runAnnotatedThing(__AtAnnotatedThing__ __at)
		throws IOException, NullPointerException
	{
		if (__at == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses the introductory class statement.
	 *
	 * @param __at The input context.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/09
	 */
	private final void __runClass(__AtClass__ __at)
		throws IOException, NullPointerException
	{
		if (__at == null)
			throw new NullPointerException("NARG");
		
		// There might be a bunch of annotations to be processed here
		BottomToken peek = this.__bottomPeek();
		BottomType type = peek.type();
		if (type == BottomType.SYMBOL_AT)
		{
			this.__stackPush(new __AtAnnotatedThing__(__at));
			return;
		}
		
		// Is this an inner class?
		boolean isinner = __at.isinner;
		
		// Read input class flags and parse them, if there are any
		Set<ClassFlag> rawflags = new LinkedHashSet<>();
		for (;;)
		{
			peek = this.__bottomPeek();
			type = peek.type();
			
			// Determine the flag which was 
			ClassFlag which = null;
			switch (type)
			{
				case KEYWORD_ABSTRACT:
					which = ClassFlag.ABSTRACT;
					break;
					
				case KEYWORD_FINAL:
					which = ClassFlag.FINAL;
					break;
					
				case KEYWORD_PRIVATE:
					// {@squirreljme.error AQ26 Only inner classes may be
					// private.}
					if (!isinner)
						throw new TokenizerException(peek, "AQ26");
					throw new todo.TODO();
					/*which = ClassFlag.INNER_PRIVATE;
					break;*/
					
				case KEYWORD_PROTECTED:
					// {@squirreljme.error AQ27 Only inner classes may be
					// protected.}
					if (!isinner)
						throw new TokenizerException(peek, "AQ27");
					throw new todo.TODO();
					/*which = ClassFlag.INNER_PROTECTED;
					break;*/
					
				case KEYWORD_PUBLIC:
					which = ClassFlag.PUBLIC;
					break;
					
				case KEYWORD_STATIC:
					which = ClassFlag.STATIC;
					break;
					
				case KEYWORD_STRICTFP:
					which = ClassFlag.STRICTFP;
					break;
					
					// {@squirreljme.error AQ24 Illegal token while parsing
					// class flags.}
				default:
					throw new TokenizerException(peek,
						String.format("AQ24 %s", peek));
			}
			
			// No flag read, is something else
			if (which == null)
				break;
			
			// Consume that token
			this.__bottomNext();
			
			// {@squirreljme.error AQ25 Duplicate class flag. (The flag which
			// was duplicated; The currently parsed class flags)}
			if (rawflags.contains(which))
				throw new TokenizerException(peek,
					String.format("AQ25 %s %s", which, rawflags));
		}
		
		// Build class flags
		ClassFlags cflags = new ClassFlags(rawflags);
		
		throw new todo.TODO();
	}
	
	/**
	 * Parses import statements.
	 *
	 * @param __at The input context.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/08
	 */
	private final void __runIntroImports(__AtIntroImports__ __at)
		throws IOException, NullPointerException
	{
		if (__at == null)
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
			this.__stackReplace(new __AtClass__(false));
			return;
		}
		
		// {@squirreljme.error AQ18 Unxpected token while looking for import
		// statements.}
		else
			throw new TokenizerException(peek, String.format("AQ18 %s", peek));
	}
	
	/**
	 * Decodes the introductory sequence.
	 *
	 * @param __at The input context.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/07
	 */
	private final void __runIntroPackage(__AtIntroPackage__ __at)
		throws IOException, NullPointerException
	{
		if (__at == null)
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
			this.__stackReplace(new __AtIntroImports__());
			
			// Done
			return;
		}
		
		// Import statement
		else if (type == BottomType.KEYWORD_IMPORT)
		{
			// Go straight to import processing
			this.__stackReplace(new __AtIntroImports__());
			return;
		}
		
		// Potential start of class, switch
		else if (type.isPotentialClassStart())
		{
			this.__stackReplace(new __AtClass__(false));
			return;
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
		
		// Did not say where the token was, so use the position the bottom
		// queue was at
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
	 * Pushes a new state to the top.
	 *
	 * @param __new The new stack entry.
	 * @throws NullPointerException
	 * @since 2018/03/09
	 */
	private final __At__ __stackPush(__At__ __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// Add to the top
		Deque<__At__> stack = this._stack;
		stack.addLast(__new);
		
		return __new;
	}
	
	/**
	 * Replaces the top stack entry with the new type.
	 *
	 * @param __new The new context.
	 * @return The new context.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/08
	 */
	private final __At__ __stackReplace(__At__ __new)
		throws NullPointerException
	{
		if (__new == null)
			throw new NullPointerException("NARG");
		
		// Replace the top
		Deque<__At__> stack = this._stack;
		stack.removeLast();
		stack.addLast(__new);
		
		return __new;
	}
}

