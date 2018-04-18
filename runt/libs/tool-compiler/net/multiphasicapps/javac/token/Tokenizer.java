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
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;
import net.multiphasicapps.javac.FileNameLineAndColumn;

/**
 * This class is the tokenizer which is used to provide tokens for the lexical
 * structure parser.
 *
 * @since 2017/09/04
 */
public class Tokenizer
	implements Closeable, FileNameLineAndColumn, TokenSource
{
	/** Operators used. */
	private static final List<String> _OPERATORS =
		UnmodifiableList.<String>of(Arrays.<String>asList("(", ")", "{", "}",
			"[", "]", ";", ",", ".", "=", ">", "<", "!", "~", "?", ":", "::",
			"==", "<=", ">=", "!=", "&&", "||", "++", "--", "+", "-", "*", "/",
			"&", "|", "^", "%", "<<", ">>", ">>>", "+=", "-=", "*=", "/=",
			"&=", "|=", "^=", "%=", "<<=", ">>=", ">>>=", "->", "@"));
	
	/** Operator character code merge. */
	private static final int[] _OPERATOR_MERGE;
	
	/** Input character source. */
	protected final LogicalReader in;
	
	/** Is there a character waiting? */
	private boolean _nxwaiting;
	
	/** The next character that is waiting. */
	private int _nxchar;
	
	/** The next line. */
	private int _nxline =
		1;
	
	/** The next column. */
	private int _nxcolumn =
		1;
	
	/** The current line. */
	private int _curline =
		1;
	
	/** The current column. */
	private int _curcolumn =
		1;
	
	/** The current token's line. */
	private int _atline =
		1;
	
	/** The current token's column. */
	private int _atcolumn =
		1;
	
	/**
	 * Initializes the merged operator set.
	 *
	 * @since 2018/03/07
	 */
	static
	{
		List<String> operators = Tokenizer._OPERATORS;
		int numops = operators.size();
		int[] merge = new int[numops];
		for (int i = 0; i < numops; i++)
		{
			// Start with empty space
			int val = 0xFF_FF_FF_FF;
			
			// Go through all characters in the operation and shift them in
			String op = operators.get(i);
			for (int j = 0, opn = op.length(); j < opn; j++)
			{
				val <<= 8;
				val |= ((int)op.charAt(j)) & 0x7F;
			}
			
			merge[i] = val;
		}
		
		_OPERATOR_MERGE = merge;
	}
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __is The tokenizer input, it is treated as UTF-8.
	 * @throws NullPointerException On null arguments.
	 * @throws RuntimeException If UTF-8 is not supported, but this should
	 * not occur.
	 * @since 2017/09/04
	 */
	public Tokenizer(String __fn, InputStream __is)
		throws NullPointerException, RuntimeException
	{
		this(__fn, __wrap(__is));
	}
	
	/**
	 * Initializes the tokenizer for Java source code.
	 *
	 * @param __r The reader to read characters from.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/04
	 */
	public Tokenizer(String __fn, Reader __r)
	{
		// Check
		if (__fn == null || __r == null)
			throw new NullPointerException("NARG");
		
		this.in = new LogicalReader(__fn, __r);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public void close()
		throws TokenizerException
	{
		try
		{
			this.in.close();
		}
		catch (IOException e)
		{
			// {@squirreljme.error AQ2t Failed to close the tokenizer.}
			throw new TokenizerException(this, "AQ2t", e);
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final int column()
	{
		return this._atcolumn;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/12
	 */
	@Override
	public final String fileName()
	{
		return this.in.fileName();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/06
	 */
	@Override
	public final int line()
	{
		return this._atline;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/05
	 */
	@Override
	public final Token next()
		throws TokenizerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the next raw token which was read from the input.
	 *
	 * @return The read token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2018/04/18
	 */
	private final Token __decodeToken()
		throws TokenizerException
	{
		// Loop to skip whitespace
		for (;;)
		{
			// Record before read because the read will make the column count
			// and possibly even the row be off by one
			int line = this._curline,
				column = this._curcolumn;
			
			// Stop at EOF
			int c = this.__nextChar();
			if (c < 0)
			{
				// Just keep sending EOF tokens forever
				return __token(TokenType.END_OF_FILE, "");
			}
			
			// Skip whitespace
			if (CharacterTest.isWhite(c))
				continue;
			
			// Token position, used for debugging
			this._atline = line;
			this._atcolumn = column;
			
			// Forward slash: comments, /, or /=.
			if (c == '/')
				return __determineForwardSlash();
			
			// Equal sign
			else if (c == '=')
				return __determineEquals();
			
			// Dot
			else if (c == '.')
			{
				// If the following character is a number then this is a
				// decimal digit
				int p = this.__peekChar();
				if (p >= '0' && p <= '9')
					return this.__getNumberLiteral((char)p);
				
				// If it is a dot then it could be ...
				else if (p == '.')
				{
					// {@squirreljme.error AQ0y Invalid elipses, three
					// dots were expected but only two were read.}
					p = this.__peekChar();
					if (p != '.')
						throw new TokenizerException(this, "AQ0y");
					
					return this.__token(TokenType.SYMBOL_ELLIPSES, "...");
				}
				
				// Otherwise just a dot
				return this.__token(TokenType.SYMBOL_DOT, ".");
			}
			
			// Ternary colon, case, label, or method reference
			else if (c == ':')
			{
				// Could be a double colon
				int peek = this.__peekChar();
				if (peek == ':')
				{
					this.__nextChar();
					return this.__token(TokenType.SYMBOL_DOUBLE_COLON, "::");
				}
				
				// Only a single one
				return this.__token(TokenType.SYMBOL_COLON, ":");
			}
			
			// Identifiers
			else if (CharacterTest.isIdentifierStart((char)c))
				return __getIdentifier((char)c);
			
			// Integer/float literals
			else if (c >= '0' && c <= '9')
				return __getNumberLiteral((char)c);
			
			// String
			else if (c == '\"')
				return this.__getString();
			
			// Is start is a potential symbol
			else if (CharacterTest.isSymbolStart(c))
				return this.__determineOperator(c);
			
			// {@squirreljme.error AQ0e Unknown character while tokenizing the
			// Java source code. (The character; The line; The column)}
			else
				throw new TokenizerException(this,
					String.format("AQ0e %c %d %d", (char)c, line, column));
		}
	}
	
	/**
	 * Decides how to parse an equal sign.
	 *
	 * @return The read token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2017/09/11
	 */
	private Token __determineEquals()
		throws TokenizerException
	{
		// Checking for equality?
		int d = __peekChar();
		if (d == '=')
		{
			__nextChar();
			return __token(TokenType.COMPARE_EQUALS, "==");
		}
		
		// Just an assignment
		else
			return __token(TokenType.OPERATOR_ASSIGN, "=");
	}
	
	/**
	 * Decides what to do with a forward slash.
	 *
	 * @return The read token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2017/09/09
	 */
	private Token __determineForwardSlash()
		throws TokenizerException
	{
		int c = __peekChar();
		
		// Not-divide
		if (c == '*' || c == '/' || c == '=')
		{
			// Eat character
			__nextChar();
			
			// Multi-line comment
			if (c == '*')
				return __getMultiLineComment();
		
			// Single line comment
			else if (c == '/')
				return __getSingleLineComment();
		
			// Divide and assign
			else
				return __token(TokenType.OPERATOR_DIVIDE_ASSIGN,
					"/=");
		}
		
		// Divide otherwise
		else
			return __token(TokenType.OPERATOR_DIVIDE, "/");
	}
	
	/**
	 * Decides how to decode a given sequence to read an operator from it.
	 *
	 * @param __c The starting character.
	 * @return The resulting token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2018/03/06
	 */
	private Token __determineOperator(int __c)
		throws TokenizerException
	{
		List<String> operators = Tokenizer._OPERATORS;
		int numops = operators.size();
		int[] merge = Tokenizer._OPERATOR_MERGE;
		
		// Setup candidate list
		boolean[] candidate = new boolean[numops],
			oldcandidate = new boolean[numops];
		for (int i = 0; i < numops; i++)
		{
			oldcandidate[i] = true;
			candidate[i] = true;
		}
		
		// Setup current fill
		int fill = 0xFF_FF_FF_00 | (__c < 128 ? __c & 0x7F : 0xFE),
			mask = 0x00_00_00_FF;
		
		// Determine which token is used for the character
		boolean needconsume = false;
		int foundop = -1,
			newcandidates = 0,
			tempfoundop = -1;
		for (;;)
		{
			// Get old values which is used to determine a match
			int oldnewcandidates = newcandidates,
				oldtempfoundfop = tempfoundop;
			
			// Clear for a new run;
			newcandidates = 0;
			tempfoundop = -1;
			
			// Determine which characters map
			for (int i = 0; i < numops; i++)
			{
				// Do not check this token
				boolean iscandidate = candidate[i];
				oldcandidate[i] = candidate[i];
				if (!iscandidate)
					continue;
				
				// Cannot be the character
				if ((fill & mask) != (merge[i] & mask))
					candidate[i] = false;
				
				// Could be the characters
				else
				{
					newcandidates++;
					tempfoundop = i;
				}
			}
			
			// Only one character is valid?
			// The check here makes the search for single character sequences
			// and >>>= valid
			if (newcandidates == 1)
			{
				// Need to consume the character?
				if (needconsume)
					this.__nextChar();
				
				foundop = tempfoundop;
				break;
			}
			
			// Nothing is valid, then an old one is valid
			else if (newcandidates == 0)
			{
				// Unmask and unfill
				int unfill = (fill >>> 8) | 0xFF_00_00_00,
					unmask = (mask >>> 8);
				
				// Go through the old candidate list and determine the
				// character to use.
				for (int i = 0; i < numops; i++)
				{
					if (!oldcandidate[i])
						continue;
					
					// Must match the sequence exactly including the upper
					// bits because those are considered invalid
					if (unfill == merge[i])
					{
						foundop = i;
						break;
					}
				}
				
				// {@squirreljme.error AQ11 Could not determine the
				// operator to decode.}
				if (foundop < 0)
					throw new TokenizerException(this, "AQ11");
				break;
			}
			
			// Need to consume the character before trying again?
			if (needconsume)
				this.__nextChar();
			
			// Peek in next character
			needconsume = true;
			__c = this.__peekChar();
			
			// Fill in character
			fill <<= 8;
			fill |= (__c >= 0 && __c < 128 ? __c & 0x7F : 0xFE);
			
			// Already at the maximum mask, nothing else to try
			if (mask == 0xFF_FF_FF_FF)
				break;
			
			// Increase mask to confirm checking area
			mask <<= 8;
			mask |= 0xFF;
		}
		
		// {@squirreljme.error AQ10 Could not decode an operator.}
		if (foundop < 0)
			throw new TokenizerException(this, "AQ10");
		
		return this.__tokenOperator(operators.get(foundop));
	}
	
	/**
	 * Reads an identifier.
	 *
	 * @param __ic The initial character.
	 * @return The read identifier.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2017/09/10
	 */
	private Token __getIdentifier(char __ic)
		throws TokenizerException
	{
		StringBuilder sb = new StringBuilder();
		sb.append(__ic);
		for (;;)
		{
			// Only consider identifier parts
			int c = __peekChar();
			if (c < 0 || !CharacterTest.isIdentifierPart((char)c))
			{
				String s = sb.toString();
				TokenType t;
				switch (s)
				{
						// {@squirreljme.error AQ0f The specified keywords
						// are reserved and not valid. (The keyword)}
					case "const":
					case "goto":
						throw new TokenizerException(this, String.format(
							"AQ0f %s", __token(TokenType.IDENTIFIER, s)));
					
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
					case "null":		t = TokenType.LITERAL_NULL; break;
					case "false":		t = TokenType.LITERAL_FALSE; break;
					case "true":		t = TokenType.LITERAL_TRUE; break;
					default:			t = TokenType.IDENTIFIER; break;
				}
				
				return __token(t, s); 
			}
			
			// Consume it
			sb.append((char)__nextChar());
		}
	}
	
	/**
	 * Reads a multi-line comment.
	 *
	 * @return The comment token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2017/09/11
	 */
	private Token __getMultiLineComment()
		throws TokenizerException
	{
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// {@squirreljme.error AQ0g End of file reached while reading a
			// multi-line comment.}
			int c = __peekChar();
			if (c < 0)
				throw new TokenizerException(this, "AQ0g");
			
			// Potential end of comment?
			if (c == '*')
			{
				// Need to potentially detect the slash
				c = __nextChar();
				int d = __peekChar();
				
				// Finish it, do not include the */
				if (d == '/')
				{
					// Eat the slash otherwise divide operators will always
					// follow multi-line comments
					__nextChar();
					return __token(TokenType.COMMENT, sb);
				}
				
				// Just some asterisk
				else
					sb.append((char)c);
			}
			
			// Normal
			else
				sb.append((char)__nextChar());
		}
	}
	
	/**
	 * Decodes a number literal which could be an integer or floating point
	 * value.
	 *
	 * @param __c The initial read character.
	 * @return The decoded token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2017/09/11
	 */
	private Token __getNumberLiteral(char __c)
		throws TokenizerException
	{
		// Read a decimal point?
		boolean gotdec = (__c == '.');
		
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
		// So this basically just tries to invalidate everything
		for (int inchars = 1;;)
		{
			// If the next character is one of these then it cannot possibly
			// part of a number
			int peek = this.__peekChar();
			if (peek < 0 || CharacterTest.isWhite(peek) ||
				!CharacterTest.isPossibleNumberChar(peek))
			{
				// {@squirreljme.error AQ0u An identifier character cannot
				// follow a numerical literal. (The next character)}
				if (CharacterTest.isIdentifierPart(peek))
					throw new TokenizerException(this, 
						String.format("AQ0u %c", peek));
				break;
			}
			
			// Debug
			todo.DEBUG.note("Literal char: %c", peek);
			
			// {@squirreljme.error AQ0v Number has multiple decimal points,
			// only one is valid.}
			if (peek == '.')
			{
				if (gotdec)
					throw new TokenizerException(this, "AQ0v");
				gotdec = true;
			}
			
			// Binary literal?
			if (isbinint)
			{
				throw new todo.TODO();
			}
			
			// Octal literal?
			if (isoctint)
			{
				throw new todo.TODO();
			}
			
			// Decimal literal?
			if (isdecint)
			{
				throw new todo.TODO();
			}
			
			// Hexadecimal literal?
			if (ishexint)
			{
				throw new todo.TODO();
			}
			
			// Float literal?
			if (isdecfloat)
			{
				throw new todo.TODO();
			}
			
			// Hexfloat literal?
			if (ishexfloat)
			{
				throw new todo.TODO();
			}
			
			// If this point is reached then the character is valid
			sb.append((char)this.__nextChar());
			inchars++;
			
			// {@squirreljme.error AQ0x No numberal literals left to put
			// the literal under as they have all been ruled out. (The
			// current string sequence)}
			if (!isbinint && !isbinint && !isoctint && !isdecint &&
				!ishexint && !isdecfloat && !ishexfloat)
				throw new TokenizerException(this, 
					String.format("AQ0x %s", sb));
		}
		
		// Token only had a length of one so these will never be valid
		// because they require prefixes
		int outlen = sb.length();
		if (outlen == 1)
			isbinint = ishexint = ishexfloat = false;
		
		// Determine the best type for the token
		TokenType type;
		if (isbinint)
			type = TokenType.LITERAL_BINARY_INTEGER;
		else if (isoctint)
			type = TokenType.LITERAL_OCTAL_INTEGER;
		else if (isdecint)
			type = TokenType.LITERAL_DECIMAL_INTEGER;
		else if (ishexint)
			type = TokenType.LITERAL_HEXADECIMAL_INTEGER;
		else if (isdecfloat)
			type = TokenType.LITERAL_DECIMAL_FLOAT;
		else if (ishexfloat)
			type = TokenType.LITERAL_HEXADECIMAL_FLOAT;
		
		// {@squirreljme.error AQ0w Could not determine type of number
		// literal is used for the given string. (The token string)}
		else
			throw new TokenizerException(this, 
				String.format("AQ0w %s", sb.toString()));
		
		// Use that!
		return __token(type, sb.toString());
	}
	
	/**
	 * Reads a single line comment.
	 *
	 * @return The decoded token.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2017/09/09
	 */
	private Token __getSingleLineComment()
		throws TokenizerException
	{
		StringBuilder sb = new StringBuilder();
		for (;;)
		{
			// Stop if it is consider the end of line
			int c = __peekChar();
			if (c < 0 || CharacterTest.isNewline(c))
				return __token(TokenType.COMMENT, sb);
			
			// Otherwise consume it
			sb.append((char)__nextChar());
		}
	}
	
	/**
	 * Decodes a string.
	 *
	 * @return The resulting string.
	 * @throws TokenizerException If the token is not valid.
	 * @since 2018/03/06
	 */
	private Token __getString()
		throws TokenizerException
	{
		StringBuilder sb = new StringBuilder("\"");
		boolean escaped = false;
		for (;;)
		{
			int c = __nextChar();
			if (c == '\\')
				escaped = true;
			else if (!escaped && c == '\"')
			{
				sb.append((char)c);
				return __token(TokenType.LITERAL_STRING, sb);
			}
			
			// Consume it
			sb.append((char)c);
		}
	}
	
	/**
	 * Returns the next character.
	 *
	 * @return The next character.
	 * @throws TokenizerException If the next token could not be read.
	 * @since 2017/09/09
	 */
	private int __nextChar()
		throws TokenizerException
	{
		// Peek character so it is in the buffer
		__peekChar();
		
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
	 * @throws TokenizerException If the next character could not be peeked.
	 * @since 2017/09/09
	 */
	private int __peekChar()
		throws TokenizerException
	{
		try
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
		
		// {@squirreljme.error AQ2u Could not read the next character.}
		catch (IOException e)
		{
			throw new TokenizerException(this, "AQ2u", e);
		}
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
		
		return new Token(__t, __s.toString(), this.in.fileName(), this._atline,
			this._atcolumn);
	}
	
	/**
	 * Parses the specified sequence as an operator.
	 *
	 * @param __s The sequence to decode.
	 * @return The token for the operator.
	 * @throws NullPointerException On null arguments.
	 * @throws TokenizerException If the sequence is not valid.
	 * @since 2018/03/06
	 */
	private Token __tokenOperator(CharSequence __s)
		throws NullPointerException, TokenizerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends on the sequence
		String s = __s.toString();
		TokenType type;
		switch (s)
		{
			case "^":	type = TokenType.OPERATOR_XOR; break;
			case "^=":	type = TokenType.OPERATOR_XOR_ASSIGN; break;
			case "~":	type = TokenType.OPERATOR_COMPLEMENT; break;
			case "<":	type = TokenType.COMPARE_LESS_THAN; break;
			case "<<":	type = TokenType.OPERATOR_SHIFT_LEFT; break;
			case "<<=":	type = TokenType.OPERATOR_SHIFT_LEFT_ASSIGN; break;
			case "<=":	type = TokenType.COMPARE_LESS_OR_EQUAL; break;
			case "=":	type = TokenType.OPERATOR_ASSIGN; break;
			case "==":	type = TokenType.COMPARE_EQUALS; break;
			case ">":	type = TokenType.COMPARE_GREATER_THAN; break;
			case ">=":	type = TokenType.COMPARE_GREATER_OR_EQUAL; break;
			case ">>":	type = TokenType.OPERATOR_SSHIFT_RIGHT; break;
			case ">>=":	type = TokenType.OPERATOR_SSHIFT_RIGHT_ASSIGN; break;
			case ">>>":	type = TokenType.OPERATOR_USHIFT_RIGHT; break;
			case ">>>=":type = TokenType.OPERATOR_USHIFT_RIGHT_ASSIGN; break;
			case "|":	type = TokenType.OPERATOR_OR; break;
			case "|=":	type = TokenType.OPERATOR_OR_ASSIGN; break;
			case "||":	type = TokenType.COMPARE_OR; break;
			case "-":	type = TokenType.OPERATOR_MINUS; break;
			case "->":	type = TokenType.SYMBOL_LAMBDA; break;
			case "-=":	type = TokenType.OPERATOR_MINUS_ASSIGN; break;
			case "--":	type = TokenType.OPERATOR_DECREMENT; break;
			case ",":	type = TokenType.SYMBOL_COMMA; break;
			case ";":	type = TokenType.SYMBOL_SEMICOLON; break;
			case ":":	type = TokenType.SYMBOL_COLON; break;
			case "::":	type = TokenType.SYMBOL_DOUBLE_COLON; break;
			case "!":	type = TokenType.OPERATOR_NOT; break;
			case "!=":	type = TokenType.COMPARE_NOT_EQUALS; break;
			case "?":	type = TokenType.SYMBOL_QUESTION; break;
			case "/":	type = TokenType.OPERATOR_DIVIDE; break;
			case "/=":	type = TokenType.OPERATOR_DIVIDE_ASSIGN; break;
			case ".":	type = TokenType.SYMBOL_DOT; break;
			case "(":	type = TokenType.SYMBOL_OPEN_PARENTHESIS; break;
			case ")":	type = TokenType.SYMBOL_CLOSED_PARENTHESIS; break;
			case "[":	type = TokenType.SYMBOL_OPEN_BRACKET; break;
			case "]":	type = TokenType.SYMBOL_CLOSED_BRACKET; break;
			case "{":	type = TokenType.SYMBOL_OPEN_BRACE; break;
			case "}":	type = TokenType.SYMBOL_CLOSED_BRACE; break;
			case "*":	type = TokenType.OPERATOR_MULTIPLY; break;
			case "*=":	type = TokenType.OPERATOR_MULTIPLY_ASSIGN; break;
			case "&":	type = TokenType.OPERATOR_AND; break;
			case "&=":	type = TokenType.OPERATOR_AND_ASSIGN; break;
			case "&&":	type = TokenType.COMPARE_AND; break;
			case "%":	type = TokenType.OPERATOR_REMAINDER; break;
			case "%=":	type = TokenType.OPERATOR_REMAINDER_ASSIGN; break;
			case "+":	type = TokenType.OPERATOR_PLUS; break;
			case "+=":	type = TokenType.OPERATOR_PLUS_ASSIGN; break;
			case "++":	type = TokenType.OPERATOR_INCREMENT; break;
			case "@":	type = TokenType.SYMBOL_AT; break;
			
				// {@squirreljme.error AQ0z Could not determine the used
				// operator for the given sequence. (The sequence)}
			default:
				throw new TokenizerException(this, 
					String.format("AQ0z %s", s));
		}
		
		// Generate
		return this.__token(type, s);
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

