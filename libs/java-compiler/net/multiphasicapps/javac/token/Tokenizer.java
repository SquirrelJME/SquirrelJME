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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
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
	protected final LogicalReader in;
	
	/** Is there a character waiting? */
	private volatile boolean _nxwaiting;
	
	/** The next character that is waiting. */
	private volatile int _nxchar;
	
	/** The next line. */
	private volatile int _nxline =
		1;
	
	/** The next column. */
	private volatile int _nxcolumn =
		1;
	
	/** The current line. */
	private volatile int _curline =
		1;
	
	/** The current column. */
	private volatile int _curcolumn =
		1;
	
	/** The current token's line. */
	private volatile int _atline =
		1;
	
	/** The current token's column. */
	private volatile int _atcolumn =
		1;
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __is The tokenizer input, it is treated as UTF-8.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If UTF-8 is not supported, but this should
	 * not occur.
	 * @since 2017/09/04
	 */
	public Tokenizer(InputStream __is)
		throws NullPointerException, RuntimeException
	{
		this(__wrap(__is));
	}
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __r The reader to read characters from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	public Tokenizer(Reader __r)
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		this.in = new LogicalReader(__r);
	}
	
	/**
	 * Returns the next token.
	 *
	 * @return The next token or {@code null} if none remain.
	 * @throws IOException On read errors.
	 * @throws TokenizerException If a token sequence is not valid.
	 * @since 2017/09/05
	 */
	public Token next()
		throws IOException, TokenizerException
	{
		// Loop to skip whitespace
		for (;;)
		{
			// Record before read because the read will make the column count
			// and possibly even the row be off by one
			int line = this._curline,
				column = this._curcolumn;
			
			// Stop at EOF
			int c = __next();
			if (c < 0)
				return null;
			
			// Skip whitespace
			if (CharacterTest.isWhite(c))
				continue;
			
			// Token position, used for debugging
			this._atline = line;
			this._atcolumn = column;
			
			// Forward slash: comments, /, or /=.
			if (c == '/')
				return __decideForwardSlash();
			
			// Identifiers
			else if (CharacterTest.isIdentifierStart((char)c))
				return __getIdentifier((char)c);
			
			throw new todo.TODO();
		}
	}
	
	/**
	 * Decides what to do with a forward slash.
	 *
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private Token __decideForwardSlash()
		throws IOException
	{
		int c = __peek();
		
		// Not-divide
		if (c == '*' || c == '/' || c == '=')
		{
			// Eat character
			__next();
			
			// Multi-line comment
			if (c == '*')
				throw new todo.TODO();
		
			// Single line comment
			else if (c == '/')
				return __getSingleLineComment();
		
			// Divide and assign
			else
				return __token(TokenType.OPERATOR_DIVIDE_AND_ASSIGN, "/=");
		}
		
		// Divide otherwise
		else
			return __token(TokenType.OPERATOR_DIVIDE, "/");
	}
	
	/**
	 * Reads an identifier.
	 *
	 * @param __ic The initial character.
	 * @return The read identifier.
	 * @since 2017/09/10
	 */
	private Token __getIdentifier(char __ic)
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(__ic);
		for (;;)
		{
			// Only consider identifier parts
			int c = __peek();
			if (c < 0 || !CharacterTest.isIdentifierPart((char)c))
			{
				String s = sb.toString();
				TokenType t;
				switch (s)
				{
						// {@squirreljme.error AQ04 The specified keywords
						// are reserved and not valid. (The keyword)}
					case "const":
					case "goto":
						throw new TokenizerException(String.format("AQ04 %s",
							__token(TokenType.IDENTIFIER, s)));
					
					case "abstract":	t = TokenType.KEYWORD_ABSTRACT; break;
					case "assert":		t = TokenType.KEYWORD_ASSERT; break;
					case "boolean":		t = TokenType.KEYWORD_BOOLEAN; break;
					case "break":		t = TokenType.KEYWORD_BREAK; break;
					case "byte":		t = TokenType.KEYWORD_BYTE; break;
					case "case":		t = TokenType.KEYWORD_CASE; break;
					case "catch":		t = TokenType.KEYWORD_CATCH; break;
					case "char":		t = TokenType.KEYWORD_CHAR; break;
					case "class":		t = TokenType.KEYWORD_CLASS; break;
					case "continue":	t = TokenType.KEYWORD_CONTINUE; break;
					case "default":		t = TokenType.KEYWORD_DEFAULT; break;
					case "do":			t = TokenType.KEYWORD_DO; break;
					case "double":		t = TokenType.KEYWORD_DOUBLE; break;
					case "else":		t = TokenType.KEYWORD_ELSE; break;
					case "enum":		t = TokenType.KEYWORD_ENUM; break;
					case "extends":		t = TokenType.KEYWORD_EXTENDS; break;
					case "final":		t = TokenType.KEYWORD_FINAL; break;
					case "finally":		t = TokenType.KEYWORD_FINALLY; break;
					case "float":		t = TokenType.KEYWORD_FLOAT; break;
					case "for":			t = TokenType.KEYWORD_FOR; break;
					case "if":			t = TokenType.KEYWORD_IF; break;
					case "implements":
						t = TokenType.KEYWORD_IMPLEMENTS;
						break;
						
					case "import":		t = TokenType.KEYWORD_IMPORT; break;
					case "instanceof":
						t = TokenType.KEYWORD_INSTANCEOF;
						break;
					case "int":			t = TokenType.KEYWORD_INT; break;
					case "interface":	t = TokenType.KEYWORD_INTERFACE; break;
					case "long":		t = TokenType.KEYWORD_LONG; break;
					case "native":		t = TokenType.KEYWORD_NATIVE; break;
					case "new":			t = TokenType.KEYWORD_NEW; break;
					case "package":		t = TokenType.KEYWORD_PACKAGE; break;
					case "private":		t = TokenType.KEYWORD_PRIVATE; break;
					case "protected":	t = TokenType.KEYWORD_PROTECTED; break;
					case "public":		t = TokenType.KEYWORD_PUBLIC; break;
					case "return":		t = TokenType.KEYWORD_RETURN; break;
					case "short":		t = TokenType.KEYWORD_SHORT; break;
					case "static":		t = TokenType.KEYWORD_STATIC; break;
					case "strictfp":	t = TokenType.KEYWORD_STRICTFP; break;
					case "super":		t = TokenType.KEYWORD_SUPER; break;
					case "switch":		t = TokenType.KEYWORD_SWITCH; break;
					case "synchronized":
						t = TokenType.KEYWORD_SYNCHRONIZED;
						break;
						
					case "this":		t = TokenType.KEYWORD_THIS; break;
					case "throw":		t = TokenType.KEYWORD_THROW; break;
					case "throws":		t = TokenType.KEYWORD_THROWS; break;
					case "transient":	t = TokenType.KEYWORD_TRANSIENT; break;
					case "try":			t = TokenType.KEYWORD_TRY; break;
					case "void":		t = TokenType.KEYWORD_VOID; break;
					case "volatile":	t = TokenType.KEYWORD_VOLATILE; break;
					case "while":		t = TokenType.KEYWORD_WHILE; break;
					default:			t = TokenType.IDENTIFIER; break;
				}
				
				return __token(t, s); 
			}
			
			// Consume it
			sb.append((char)__next());
		}
	}
	
	/**
	 * Reads a single line comment.
	 *
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private Token __getSingleLineComment()
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// Stop if it is consider the end of line
			int c = __peek();
			if (c < 0 || CharacterTest.isNewline(c))
				return __token(TokenType.COMMENT, sb);
			
			// Otherwise consume it
			sb.append((char)__next());
		}
	}
	
	/**
	 * Returns the next character.
	 *
	 * @return The next character.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private int __next()
		throws IOException
	{
		// Peek character so it is in the buffer
		__peek();
		
		// Position needed for finding errors
		this._curline = this._nxline;
		this._curcolumn = this._nxcolumn;
		
		// Use the next character
		int rv = this._nxchar;
		this._nxwaiting = false;
		this._nxchar = -1;
		return rv;
	}
	
	/**
	 * Peeks the next character.
	 *
	 * @return The next character.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private int __peek()
		throws IOException
	{
		// Use the pre-existing character
		if (this._nxwaiting)
			return this._nxchar;
		
		// Read in next character
		LogicalReader in = this.in;
		int c = in.read(),
			ln = in.line(),
			cl = in.column();
		
		// Set details
		this._nxchar = c;
		this._nxline = ln;
		this._nxcolumn = cl;
		this._nxwaiting = true;
		
		// Need to know the actual character
		return c;
	}
	
	/**
	 * Creates a token and returns it.
	 *
	 * @param __t The type of token to create.
	 * @param __s The string making up the token characters.
	 * @return The created token.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/09
	 */
	private Token __token(TokenType __t, CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__t == null || __s == null)
			throw new NullPointerException("NARG");
		
		return new Token(__t, __s.toString(), this._atline, this._atcolumn);
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

