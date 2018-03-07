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
public class BottomTokenizer
	implements Closeable, LineAndColumn
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
	public BottomTokenizer(InputStream __is)
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
	public BottomTokenizer(Reader __r)
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		this.in = new LogicalReader(__r);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public void close()
		throws IOException
	{
		this.in.close();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public int column()
	{
		return this._atcolumn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public int line()
	{
		return this._atline;
	}
	
	/**
	 * Returns the next token.
	 *
	 * @return The next token or {@code null} if none remain.
	 * @throws IOException On read errors.
	 * @throws TokenizerException If a token sequence is not valid.
	 * @since 2017/09/05
	 */
	public BottomToken next()
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
			
			// BottomToken position, used for debugging
			this._atline = line;
			this._atcolumn = column;
			
			// Forward slash: comments, /, or /=.
			if (c == '/')
				return __decideForwardSlash();
			
			// Equal sign
			else if (c == '=')
				return __decideEquals();
			
			// Open parenthesis
			else if (c == '(')
				return __token(BottomTokenType.SYMBOL_OPEN_PARENTHESIS, "(");
			
			// Closed parenthesis
			else if (c == ')')
				return __token(BottomTokenType.SYMBOL_CLOSED_PARENTHESIS, ")");
			
			// Open brace
			else if (c == '{')
				return __token(BottomTokenType.SYMBOL_OPEN_BRACE, "{");
			
			// Closed brace
			else if (c == '}')
				return __token(BottomTokenType.SYMBOL_CLOSED_BRACE, "}");
			
			// Open bracket
			else if (c == '[')
				return __token(BottomTokenType.SYMBOL_OPEN_BRACKET, "[");
			
			// Closed bracket
			else if (c == ']')
				return __token(BottomTokenType.SYMBOL_CLOSED_BRACKET, "]");
			
			// Semi-colon
			else if (c == ';')
				return __token(BottomTokenType.SYMBOL_SEMICOLON, ";");
			
			// Comma
			else if (c == ',')
				return __token(BottomTokenType.SYMBOL_COMMA, ",");
			
			// Dot
			else if (c == '.')
				return __token(BottomTokenType.SYMBOL_DOT, ".");
			
			// Ternary question
			else if (c == '?')
				return __token(BottomTokenType.OPERATOR_TERNARY_QUESTION, "?");
			
			// Ternary colon, case, or label
			else if (c == ':')
				return __token(BottomTokenType.SYMBOL_COLON, ":");
			
			// Not
			else if (c == '!')
				return __token(BottomTokenType.OPERATOR_NOT, "!");
			
			// Identifiers
			else if (CharacterTest.isIdentifierStart((char)c))
				return __getIdentifier((char)c);
			
			// Integer/float literals
			else if (c >= '0' && c <= '9')
				return __getNumberLiteral((char)c);
			
			// String
			else if (c == '\"')
				return this.__getString();
			
			// {@squirreljme.error AQ0e Unknown character while tokenizing the
			// Java source code. (The character; The line; The column)}
			else
				throw new TokenizerException(String.format("AQ0e %c %d %d",
					(char)c, line, column));
		}
	}
	
	/**
	 * Decides how to parse an equal sign.
	 *
	 * @return The read token.
	 * @throws IOException On read errors.
	 * @since 2017/09/11
	 */
	private BottomToken __decideEquals()
		throws IOException
	{
		// Checking for equality?
		int d = __peek();
		if (d == '=')
		{
			__next();
			return __token(BottomTokenType.OPERATOR_COMPARE_EQUALS, "==");
		}
		
		// Just an assignment
		else
			return __token(BottomTokenType.OPERATOR_ASSIGN, "=");
	}
	
	/**
	 * Decides what to do with a forward slash.
	 *
	 * @return The read token.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private BottomToken __decideForwardSlash()
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
				return __getMultiLineComment();
		
			// Single line comment
			else if (c == '/')
				return __getSingleLineComment();
		
			// Divide and assign
			else
				return __token(BottomTokenType.OPERATOR_DIVIDE_AND_ASSIGN,
					"/=");
		}
		
		// Divide otherwise
		else
			return __token(BottomTokenType.OPERATOR_DIVIDE, "/");
	}
	
	/**
	 * Reads an identifier.
	 *
	 * @param __ic The initial character.
	 * @return The read identifier.
	 * @since 2017/09/10
	 */
	private BottomToken __getIdentifier(char __ic)
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
				BottomTokenType t;
				switch (s)
				{
						// {@squirreljme.error AQ0f The specified keywords
						// are reserved and not valid. (The keyword)}
					case "const":
					case "goto":
						throw new TokenizerException(String.format("AQ0f %s",
							__token(BottomTokenType.IDENTIFIER, s)));
					
					case "abstract":	t = BottomTokenType.KEYWORD_ABSTRACT; break;
					case "assert":		t = BottomTokenType.KEYWORD_ASSERT; break;
					case "boolean":		t = BottomTokenType.KEYWORD_BOOLEAN; break;
					case "break":		t = BottomTokenType.KEYWORD_BREAK; break;
					case "byte":		t = BottomTokenType.KEYWORD_BYTE; break;
					case "case":		t = BottomTokenType.KEYWORD_CASE; break;
					case "catch":		t = BottomTokenType.KEYWORD_CATCH; break;
					case "char":		t = BottomTokenType.KEYWORD_CHAR; break;
					case "class":		t = BottomTokenType.KEYWORD_CLASS; break;
					case "continue":	t = BottomTokenType.KEYWORD_CONTINUE; break;
					case "default":		t = BottomTokenType.KEYWORD_DEFAULT; break;
					case "do":			t = BottomTokenType.KEYWORD_DO; break;
					case "double":		t = BottomTokenType.KEYWORD_DOUBLE; break;
					case "else":		t = BottomTokenType.KEYWORD_ELSE; break;
					case "enum":		t = BottomTokenType.KEYWORD_ENUM; break;
					case "extends":		t = BottomTokenType.KEYWORD_EXTENDS; break;
					case "final":		t = BottomTokenType.KEYWORD_FINAL; break;
					case "finally":		t = BottomTokenType.KEYWORD_FINALLY; break;
					case "float":		t = BottomTokenType.KEYWORD_FLOAT; break;
					case "for":			t = BottomTokenType.KEYWORD_FOR; break;
					case "if":			t = BottomTokenType.KEYWORD_IF; break;
					case "implements":
						t = BottomTokenType.KEYWORD_IMPLEMENTS;
						break;
						
					case "import":		t = BottomTokenType.KEYWORD_IMPORT; break;
					case "instanceof":
						t = BottomTokenType.KEYWORD_INSTANCEOF;
						break;
					case "int":			t = BottomTokenType.KEYWORD_INT; break;
					case "interface":	t = BottomTokenType.KEYWORD_INTERFACE; break;
					case "long":		t = BottomTokenType.KEYWORD_LONG; break;
					case "native":		t = BottomTokenType.KEYWORD_NATIVE; break;
					case "new":			t = BottomTokenType.KEYWORD_NEW; break;
					case "package":		t = BottomTokenType.KEYWORD_PACKAGE; break;
					case "private":		t = BottomTokenType.KEYWORD_PRIVATE; break;
					case "protected":	t = BottomTokenType.KEYWORD_PROTECTED; break;
					case "public":		t = BottomTokenType.KEYWORD_PUBLIC; break;
					case "return":		t = BottomTokenType.KEYWORD_RETURN; break;
					case "short":		t = BottomTokenType.KEYWORD_SHORT; break;
					case "static":		t = BottomTokenType.KEYWORD_STATIC; break;
					case "strictfp":	t = BottomTokenType.KEYWORD_STRICTFP; break;
					case "super":		t = BottomTokenType.KEYWORD_SUPER; break;
					case "switch":		t = BottomTokenType.KEYWORD_SWITCH; break;
					case "synchronized":
						t = BottomTokenType.KEYWORD_SYNCHRONIZED;
						break;
						
					case "this":		t = BottomTokenType.KEYWORD_THIS; break;
					case "throw":		t = BottomTokenType.KEYWORD_THROW; break;
					case "throws":		t = BottomTokenType.KEYWORD_THROWS; break;
					case "transient":	t = BottomTokenType.KEYWORD_TRANSIENT; break;
					case "try":			t = BottomTokenType.KEYWORD_TRY; break;
					case "void":		t = BottomTokenType.KEYWORD_VOID; break;
					case "volatile":	t = BottomTokenType.KEYWORD_VOLATILE; break;
					case "while":		t = BottomTokenType.KEYWORD_WHILE; break;
					case "null":		t = BottomTokenType.LITERAL_NULL; break;
					case "false":		t = BottomTokenType.LITERAL_FALSE; break;
					case "true":		t = BottomTokenType.LITERAL_TRUE; break;
					default:			t = BottomTokenType.IDENTIFIER; break;
				}
				
				return __token(t, s); 
			}
			
			// Consume it
			sb.append((char)__next());
		}
	}
	
	/**
	 * Reads a multi-line comment.
	 *
	 * @throws IOException On read errors.
	 * @since 2017/09/11
	 */
	private BottomToken __getMultiLineComment()
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// {@squirreljme.error AQ0g End of file reached while reading a
			// multi-line comment.}
			int c = __peek();
			if (c < 0)
				throw new TokenizerException("AQ0g");
			
			// Potential end of comment?
			if (c == '*')
			{
				// Need to potentially detect the slash
				c = __next();
				int d = __peek();
				
				// Finish it, do not include the */
				if (d == '/')
				{
					// Eat the slash otherwise divide operators will always
					// follow multi-line comments
					__next();
					return __token(BottomTokenType.COMMENT, sb);
				}
				
				// Just some asterisk
				else
					sb.append((char)c);
			}
			
			// Normal
			else
				sb.append((char)__next());
		}
	}
	
	/**
	 * Decodes a number literal which could be an integer or floating point
	 * value.
	 *
	 * @param __c The initial read character.
	 * @return The decoded token.
	 * @throws IOException On read errors.
	 * @since 201709/11
	 */
	private BottomToken __getNumberLiteral(char __c)
		throws IOException
	{
		// All possibilities are available at start
		boolean isbinint = true,
			isoctint = true,
			isdecint = true,
			ishexint = true,
			isdecfloat = true,
			ishexfloat = true;
		
		// Only decimal values
		if (__c >= '1' && __c <= '9')
			isbinint = isoctint = ishexint = ishexfloat = false;
		
		// Otherwise cannot be an integer. Note that it can still be a decimal
		// float because 0009.7 is possible to be decoded still.
		else
			isdecint = false;
		
		// Resulting token
		StringBuilder sb = new StringBuilder();
		sb.append(__c);
		
		// Literal processing is a bit complex and as such there is much
		// handling for state within this loop. One general thing to remember
		// is that something which appears invalid for one state can be
		// completely valid for another state.
		
		throw new todo.TODO();
	}
	
	/**
	 * Reads a single line comment.
	 *
	 * @return The decoded token.
	 * @throws IOException On read errors.
	 * @since 2017/09/09
	 */
	private BottomToken __getSingleLineComment()
		throws IOException
	{
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// Stop if it is consider the end of line
			int c = __peek();
			if (c < 0 || CharacterTest.isNewline(c))
				return __token(BottomTokenType.COMMENT, sb);
			
			// Otherwise consume it
			sb.append((char)__next());
		}
	}
	
	/**
	 * Decodes a string.
	 *
	 * @return The resulting string.
	 * @throws IOException On read errors.
	 * @since 2018/03/06
	 */
	private BottomToken __getString()
		throws IOException
	{
		StringBuilder sb = new StringBuilder("\"");
		boolean escaped = false;
		for (;;)
		{
			int c = __next();
			if (c == '\\')
				escaped = true;
			else if (!escaped && c == '\"')
			{
				sb.append((char)c);
				return __token(BottomTokenType.LITERAL_STRING, sb);
			}
			
			// Consume it
			sb.append((char)c);
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
	private BottomToken __token(BottomTokenType __t, CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__t == null || __s == null)
			throw new NullPointerException("NARG");
		
		return new BottomToken(__t, __s.toString(), this._atline, this._atcolumn);
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

