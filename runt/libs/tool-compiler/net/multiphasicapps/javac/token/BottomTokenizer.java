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

/**
 * This class is the tokenizer which is used to provide tokens.
 *
 * @since 2017/09/04
 */
public class BottomTokenizer
	implements Closeable, LineAndColumn
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
	 * Initializes the merged operator set.
	 *
	 * @since 2018/03/07
	 */
	static
	{
		List<String> operators = BottomTokenizer._OPERATORS;
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
			
			// Dot
			else if (c == '.')
			{
				// If the following character is a number then this is a
				// decimal digit
				int p = this.__peek();
				if (p >= '0' && p <= '9')
					return this.__getNumberLiteral((char)p);
				
				// If it is a dot then it could be ...
				else if (p == '.')
				{
					// {@squirreljme.error AQ0y Invalid elipses, three
					// dots were expected but only two were read.}
					p = this.__peek();
					if (p != '.')
						throw new TokenizerException("AQ0y");
					
					return this.__token(BottomType.SYMBOL_ELLIPSES, "...");
				}
				
				// Otherwise just a dot
				return this.__token(BottomType.SYMBOL_DOT, ".");
			}
			
			// Ternary colon, case, label, or method reference
			else if (c == ':')
			{
				// Could be a double colon
				int peek = this.__peek();
				if (peek == ':')
				{
					this.__next();
					return this.__token(BottomType.SYMBOL_DOUBLE_COLON, "::");
				}
				
				// Only a single one
				return this.__token(BottomType.SYMBOL_COLON, ":");
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
				return this.__decideOperator(c);
			
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
			return __token(BottomType.COMPARE_EQUALS, "==");
		}
		
		// Just an assignment
		else
			return __token(BottomType.OPERATOR_ASSIGN, "=");
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
				return __token(BottomType.OPERATOR_DIVIDE_ASSIGN,
					"/=");
		}
		
		// Divide otherwise
		else
			return __token(BottomType.OPERATOR_DIVIDE, "/");
	}
	
	/**
	 * Decides how to decode a given sequence to read an operator from it.
	 *
	 * @param __c The starting character.
	 * @return The resulting token.
	 * @throws IOException On read errors.
	 * @since 2018/03/06
	 */
	private BottomToken __decideOperator(int __c)
		throws IOException
	{
		List<String> operators = BottomTokenizer._OPERATORS;
		int numops = operators.size();
		int[] merge = BottomTokenizer._OPERATOR_MERGE;
		
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
					this.__next();
				
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
					throw new TokenizerException("AQ11");
				break;
			}
			
			// Need to consume the character before trying again?
			if (needconsume)
				this.__next();
			
			// Peek in next character
			needconsume = true;
			__c = this.__peek();
			
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
			throw new TokenizerException("AQ10");
		
		return this.__tokenOperator(operators.get(foundop));
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
				BottomType t;
				switch (s)
				{
						// {@squirreljme.error AQ0f The specified keywords
						// are reserved and not valid. (The keyword)}
					case "const":
					case "goto":
						throw new TokenizerException(String.format("AQ0f %s",
							__token(BottomType.IDENTIFIER, s)));
					
					case "abstract":	t = BottomType.KEYWORD_ABSTRACT; break;
					case "assert":		t = BottomType.KEYWORD_ASSERT; break;
					case "boolean":		t = BottomType.KEYWORD_BOOLEAN; break;
					case "break":		t = BottomType.KEYWORD_BREAK; break;
					case "byte":		t = BottomType.KEYWORD_BYTE; break;
					case "case":		t = BottomType.KEYWORD_CASE; break;
					case "catch":		t = BottomType.KEYWORD_CATCH; break;
					case "char":		t = BottomType.KEYWORD_CHAR; break;
					case "class":		t = BottomType.KEYWORD_CLASS; break;
					case "continue":	t = BottomType.KEYWORD_CONTINUE; break;
					case "default":		t = BottomType.KEYWORD_DEFAULT; break;
					case "do":			t = BottomType.KEYWORD_DO; break;
					case "double":		t = BottomType.KEYWORD_DOUBLE; break;
					case "else":		t = BottomType.KEYWORD_ELSE; break;
					case "enum":		t = BottomType.KEYWORD_ENUM; break;
					case "extends":		t = BottomType.KEYWORD_EXTENDS; break;
					case "final":		t = BottomType.KEYWORD_FINAL; break;
					case "finally":		t = BottomType.KEYWORD_FINALLY; break;
					case "float":		t = BottomType.KEYWORD_FLOAT; break;
					case "for":			t = BottomType.KEYWORD_FOR; break;
					case "if":			t = BottomType.KEYWORD_IF; break;
					case "implements":
						t = BottomType.KEYWORD_IMPLEMENTS;
						break;
						
					case "import":		t = BottomType.KEYWORD_IMPORT; break;
					case "instanceof":
						t = BottomType.KEYWORD_INSTANCEOF;
						break;
					case "int":			t = BottomType.KEYWORD_INT; break;
					case "interface":	t = BottomType.KEYWORD_INTERFACE; break;
					case "long":		t = BottomType.KEYWORD_LONG; break;
					case "native":		t = BottomType.KEYWORD_NATIVE; break;
					case "new":			t = BottomType.KEYWORD_NEW; break;
					case "package":		t = BottomType.KEYWORD_PACKAGE; break;
					case "private":		t = BottomType.KEYWORD_PRIVATE; break;
					case "protected":	t = BottomType.KEYWORD_PROTECTED; break;
					case "public":		t = BottomType.KEYWORD_PUBLIC; break;
					case "return":		t = BottomType.KEYWORD_RETURN; break;
					case "short":		t = BottomType.KEYWORD_SHORT; break;
					case "static":		t = BottomType.KEYWORD_STATIC; break;
					case "strictfp":	t = BottomType.KEYWORD_STRICTFP; break;
					case "super":		t = BottomType.KEYWORD_SUPER; break;
					case "switch":		t = BottomType.KEYWORD_SWITCH; break;
					case "synchronized":
						t = BottomType.KEYWORD_SYNCHRONIZED;
						break;
						
					case "this":		t = BottomType.KEYWORD_THIS; break;
					case "throw":		t = BottomType.KEYWORD_THROW; break;
					case "throws":		t = BottomType.KEYWORD_THROWS; break;
					case "transient":	t = BottomType.KEYWORD_TRANSIENT; break;
					case "try":			t = BottomType.KEYWORD_TRY; break;
					case "void":		t = BottomType.KEYWORD_VOID; break;
					case "volatile":	t = BottomType.KEYWORD_VOLATILE; break;
					case "while":		t = BottomType.KEYWORD_WHILE; break;
					case "null":		t = BottomType.LITERAL_NULL; break;
					case "false":		t = BottomType.LITERAL_FALSE; break;
					case "true":		t = BottomType.LITERAL_TRUE; break;
					default:			t = BottomType.IDENTIFIER; break;
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
					return __token(BottomType.COMMENT, sb);
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
	 * @since 2017/09/11
	 */
	private BottomToken __getNumberLiteral(char __c)
		throws IOException
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
			int peek = this.__peek();
			if (peek < 0 || CharacterTest.isWhite(peek) ||
				!CharacterTest.isPossibleNumberChar(peek))
			{
				// {@squirreljme.error AQ0u An identifier character cannot
				// follow a numerical literal. (The next character)}
				if (CharacterTest.isIdentifierPart(peek))
					throw new TokenizerException(
						String.format("AQ0u %c", peek));
				break;
			}
			
			// {@squirreljme.error AQ0v Number has multiple decimal points,
			// only one is valid.}
			if (peek == '.')
			{
				if (gotdec)
					throw new TokenizerException("AQ0v");
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
			sb.append((char)this.__next());
			inchars++;
			
			// {@squirreljme.error AQ0x No numberal literals left to put
			// the literal under as they have all been ruled out. (The
			// current string sequence)}
			if (!isbinint && !isbinint && !isoctint && !isdecint &&
				!ishexint && !isdecfloat && !ishexfloat)
				throw new TokenizerException(String.format("AQ0x %s", sb));
		}
		
		// Determine the best type for the token
		BottomType type;
		if (isbinint)
			type = BottomType.LITERAL_BINARY_INTEGER;
		else if (isoctint)
			type = BottomType.LITERAL_OCTAL_INTEGER;
		else if (isdecint)
			type = BottomType.LITERAL_DECIMAL_INTEGER;
		else if (ishexint)
			type = BottomType.LITERAL_HEXADECIMAL_INTEGER;
		else if (isdecfloat)
			type = BottomType.LITERAL_DECIMAL_FLOAT;
		else if (ishexfloat)
			type = BottomType.LITERAL_HEXADECIMAL_FLOAT;
		
		// {@squirreljme.error AQ0w Could not determine type of number
		// literal is used for the given string. (The token string)}
		else
			throw new TokenizerException(
				String.format("AQ0w %s", sb.toString()));
		
		// Use that!
		return __token(type, sb.toString());
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
				return __token(BottomType.COMMENT, sb);
			
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
				return __token(BottomType.LITERAL_STRING, sb);
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
	private BottomToken __token(BottomType __t, CharSequence __s)
		throws NullPointerException
	{
		// Check
		if (__t == null || __s == null)
			throw new NullPointerException("NARG");
		
		return new BottomToken(__t, __s.toString(), this._atline,
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
	private BottomToken __tokenOperator(CharSequence __s)
		throws NullPointerException, TokenizerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Depends on the sequence
		String s = __s.toString();
		BottomType type;
		switch (s)
		{
			case "^":	type = BottomType.OPERATOR_XOR; break;
			case "^=":	type = BottomType.OPERATOR_XOR_ASSIGN; break;
			case "~":	type = BottomType.OPERATOR_COMPLEMENT; break;
			case "<":	type = BottomType.COMPARE_LESS_THAN; break;
			case "<<":	type = BottomType.OPERATOR_SHIFT_LEFT; break;
			case "<<=":	type = BottomType.OPERATOR_SHIFT_LEFT_ASSIGN; break;
			case "<=":	type = BottomType.COMPARE_LESS_OR_EQUAL; break;
			case "=":	type = BottomType.OPERATOR_ASSIGN; break;
			case "==":	type = BottomType.COMPARE_EQUALS; break;
			case ">":	type = BottomType.COMPARE_GREATER_THAN; break;
			case ">=":	type = BottomType.COMPARE_GREATER_OR_EQUAL; break;
			case ">>":	type = BottomType.OPERATOR_SSHIFT_RIGHT; break;
			case ">>=":	type = BottomType.OPERATOR_SSHIFT_RIGHT_ASSIGN; break;
			case ">>>":	type = BottomType.OPERATOR_USHIFT_RIGHT; break;
			case ">>>=":type = BottomType.OPERATOR_USHIFT_RIGHT_ASSIGN; break;
			case "|":	type = BottomType.OPERATOR_OR; break;
			case "|=":	type = BottomType.OPERATOR_OR_ASSIGN; break;
			case "||":	type = BottomType.COMPARE_OR; break;
			case "-":	type = BottomType.OPERATOR_MINUS; break;
			case "->":	type = BottomType.SYMBOL_LAMBDA; break;
			case "-=":	type = BottomType.OPERATOR_MINUS_ASSIGN; break;
			case "--":	type = BottomType.OPERATOR_DECREMENT; break;
			case ",":	type = BottomType.SYMBOL_COMMA; break;
			case ";":	type = BottomType.SYMBOL_SEMICOLON; break;
			case ":":	type = BottomType.SYMBOL_COLON; break;
			case "::":	type = BottomType.SYMBOL_DOUBLE_COLON; break;
			case "!":	type = BottomType.OPERATOR_NOT; break;
			case "!=":	type = BottomType.COMPARE_NOT_EQUALS; break;
			case "?":	type = BottomType.OPERATOR_TERNARY_QUESTION; break;
			case "/":	type = BottomType.OPERATOR_DIVIDE; break;
			case "/=":	type = BottomType.OPERATOR_DIVIDE_ASSIGN; break;
			case ".":	type = BottomType.SYMBOL_DOT; break;
			case "(":	type = BottomType.SYMBOL_OPEN_PARENTHESIS; break;
			case ")":	type = BottomType.SYMBOL_CLOSED_PARENTHESIS; break;
			case "[":	type = BottomType.SYMBOL_OPEN_BRACKET; break;
			case "]":	type = BottomType.SYMBOL_CLOSED_BRACKET; break;
			case "{":	type = BottomType.SYMBOL_OPEN_BRACE; break;
			case "}":	type = BottomType.SYMBOL_CLOSED_BRACE; break;
			case "*":	type = BottomType.OPERATOR_MULTIPLY; break;
			case "*=":	type = BottomType.OPERATOR_MULTIPLY_ASSIGN; break;
			case "&":	type = BottomType.OPERATOR_AND; break;
			case "&=":	type = BottomType.OPERATOR_AND_ASSIGN; break;
			case "&&":	type = BottomType.COMPARE_AND; break;
			case "%":	type = BottomType.OPERATOR_REMAINDER; break;
			case "%=":	type = BottomType.OPERATOR_REMAINDER_ASSIGN; break;
			case "+":	type = BottomType.OPERATOR_PLUS; break;
			case "+=":	type = BottomType.OPERATOR_PLUS_ASSIGN; break;
			case "++":	type = BottomType.OPERATOR_INCREMENT; break;
			case "@":	type = BottomType.SYMBOL_AT; break;
			
				// {@squirreljme.error AQ0z Could not determine the used
				// operator for the given sequence. (The sequence)}
			default:
				throw new TokenizerException(String.format("AQ0z %s", s));
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

