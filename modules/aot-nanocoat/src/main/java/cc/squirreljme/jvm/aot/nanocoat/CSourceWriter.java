// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.nanocoat;

import cc.squirreljme.runtime.cldc.util.BooleanArrayList;
import cc.squirreljme.runtime.cldc.util.ByteArrayList;
import cc.squirreljme.runtime.cldc.util.CharacterArrayList;
import cc.squirreljme.runtime.cldc.util.DoubleArrayList;
import cc.squirreljme.runtime.cldc.util.FloatArrayList;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import cc.squirreljme.runtime.cldc.util.LongArrayList;
import cc.squirreljme.runtime.cldc.util.ShortArrayList;
import java.io.Closeable;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * Writer for C source code.
 *
 * @since 2023/05/28
 */
public class CSourceWriter
	implements Closeable
{
	/** Right side threshold. */
	private static final byte _GUTTER_THRESHOLD =
		60;
	
	/** The stream to write to. */
	protected final PrintStream out;
	
	/** Single character buffer. */
	private final char[] _singleBuf =
		new char[1];
	
	/** C Block stack. */
	private final Deque<CBlock> _blocks =
		new ArrayDeque<>();
	
	/** Reference to self, for blocks. */
	final Reference<CSourceWriter> _selfRef =
		new WeakReference<>(this);
	
	/** The current line. */
	private volatile int _line;
	
	/** The current column. */
	private volatile int _column;
	
	/** The last character written. */
	private volatile char _lastChar;
	
	/** Writing preprocessor lines? */
	private volatile boolean _preprocessorLines;
	
	/**
	 * Initializes the C source writer.
	 * 
	 * @param __out The stream output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public CSourceWriter(PrintStream __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(Object... __values)
		throws IOException
	{
		return this.array(Arrays.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(boolean... __values)
		throws IOException
	{
		return this.array(BooleanArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(byte... __values)
		throws IOException
	{
		return this.array(ByteArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(short... __values)
		throws IOException
	{
		return this.array(ShortArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(char... __values)
		throws IOException
	{
		return this.array(CharacterArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(int... __values)
		throws IOException
	{
		return this.array(IntegerArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(long... __values)
		throws IOException
	{
		return this.array(LongArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(float... __values)
		throws IOException
	{
		return this.array(FloatArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(double... __values)
		throws IOException
	{
		return this.array(DoubleArrayList.asList(__values));
	}
	
	/**
	 * Writes the given array to the output.
	 * 
	 * @param __values The values to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter array(List<?> __values)
		throws IOException
	{
		try (CBlock block = this.curly())
		{
			if (__values != null)
				for (int i = 0, n = __values.size(); i < n; i++)
				{
					if (i > 0)
						this.token(",");
					
					this.token(__values.get(i));
				}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Writes the given character.
	 * 
	 * @param __c The character to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter character(char __c)
		throws IOException
	{
		// These characters can be input directly
		if (__c == '\t')
			return this.token("\\t");
		else if (__c == '\r')
			return this.token("\\r");
		else if (__c == '\n')
			return this.token("\\n");
		else if (__c == '\'')
			return this.token("\\'");
		else if (__c == '\"')
			return this.token("\\\"");
		else if (__c >= 0x20 && __c < 0x7F)
			return this.token("'" + __c + "'");
		
		// Fallback to number input
		return this.number(CNumberType.JCHAR, (int)__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void close()
		throws IOException
	{
		this.out.close();
	}
	
	/**
	 * Opens a curly block.
	 * 
	 * @return The block to open.
	 * @throws IOException On 
	 * @since 2023/05/29
	 */
	public CBlock curly()
		throws IOException
	{
		// Setup new block
		CBlock rv = new CBlock(this._selfRef, "}");
		this.__pushBlock(rv);
		
		// Output open block
		this.__out('{');
		
		return rv;
	}
	
	/**
	 * Flushes the output.
	 * 
	 * @throws IOException On flush errors.
	 * @since 2023/05/28
	 */
	public void flush()
		throws IOException
	{
		this.out.flush();
	}
	
	/**
	 * Start on a fresh line.
	 * 
	 * @throws IOException On write errors.
	 * @since 2023/05/28
	 */
	public void freshLine()
		throws IOException
	{
		// Only need to do this if we are not at the line start
		if (this._column > 0)
		{
			// Preprocessor lines can do multi-line if they end in a slash
			// so we need to do that before we return
			if (this._preprocessorLines)
				this.__out('\\');
			
			this.__out('\n');
		}
	}
	
	/**
	 * Writes a function.
	 * 
	 * @param __modifier The function modifier.
	 * @param __name The name of the function.
	 * @param __returnVal The return value.
	 * @param __arguments The arguments to the function.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no name was specified.
	 * @since 2023/05/30
	 */
	public CSourceWriter function(CModifier __modifier, String __name,
		CType __returnVal, CFunctionArgument... __arguments)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Write modifiers first
		if (__modifier != null)
			this.token(__modifier);
		
		// Return value?
		this.token((__returnVal == null ? CBasicType.VOID : __returnVal));
		
		// Function name and arguments
		return this.surroundDelimited(__name, ",",
			(Object[])__arguments);
	}
	
	/**
	 * Defines a function.
	 * 
	 * @param __modifier The function modifier.
	 * @param __name The name of the function.
	 * @param __returnVal The return value.
	 * @param __arguments The arguments to the function.
	 * @return The block for writing functions.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no name was specified.
	 * @since 2023/05/30
	 */
	public CFunctionBlock functionDefine(CModifier __modifier,
		String __name, CType __returnVal, CFunctionArgument... __arguments)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Start on a fresh line, is cleaner
		this.freshLine();
		
		// Start function
		this.function(__modifier, __name, __returnVal, __arguments);
		this.token("{");
		
		// Push block for it
		return this.__pushBlock(new CFunctionBlock(this._selfRef));
	}
	
	/**
	 * Writes a function prototype.
	 * 
	 * @param __modifier The function modifier.
	 * @param __name The name of the function.
	 * @param __returnVal The return value.
	 * @param __arguments The arguments to the function.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no name was specified.
	 * @since 2023/05/30
	 */
	public CSourceWriter functionPrototype(CModifier __modifier, String __name,
		CType __returnVal, CFunctionArgument... __arguments)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Start on a fresh line, is cleaner
		this.freshLine();
		
		this.function(__modifier, __name, __returnVal, __arguments);
		return this.token(";");
	}
	
	/**
	 * Writes the specified number.
	 * 
	 * @param __number The number to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter number(Number __number)
		throws IOException, NullPointerException
	{
		if (__number == null)
			throw new NullPointerException("NARG");
		
		// Determine the type of number this is
		CNumberType type = null;
		for (CNumberType maybe : CNumberType.VALUES)
			if (maybe.classType.isAssignableFrom(__number.getClass()))
			{
				type = maybe;
				break;
			}
		
		// Forward
		return this.number(type, __number);
	}
	
	/**
	 * Writes the specified number.
	 * 
	 * @param __type The type of number this is.
	 * @param __number The number to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter number(CNumberType __type, Number __number)
		throws IOException, NullPointerException
	{
		if (__number == null)
			throw new NullPointerException("NARG");
		
		// Determine the value number
		long value;
		if (__number instanceof Double)
			value = Double.doubleToRawLongBits((Double)__number);
		else if (__number instanceof Float)
			value = Float.floatToRawIntBits((Float)__number);
		else
			value = __number.longValue();
		
		// No type specified, so just use whatever value it is... but never
		// use a prefix when using the preprocessor
		if (__type == null || __type.prefix == null ||
			this._preprocessorLines)
			return this.token(Long.toString(value));
		
		// Prefix it
		return this.surround(__type.prefix, Long.toString(value));
	}
	
	/**
	 * Writes a preprocessor define.
	 * 
	 * @param __symbol The symbol to define.
	 * @param __tokens The tokens to define.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter preprocessorDefine(String __symbol,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		if (__symbol == null)
			throw new NullPointerException("NARG");
		
		if (__tokens == null || __tokens.length == 0)
			return this.preprocessorLine("define", __symbol);
		return this.preprocessorLine("define", __symbol, __tokens);
	}
	
	/**
	 * Adds an if check for preprocessing.
	 * 
	 * @param __condition The tokens to use for the check.
	 * @return The opened block.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CPPBlock preprocessorIf(Object... __condition)
		throws IOException, NullPointerException
	{
		if (__condition == null || __condition.length == 0)
			throw new NullPointerException("NARG");
		
		// Setup new block
		CPPBlock rv = new CPPBlock(this._selfRef);
		this.__pushBlock(rv);
		
		// Start the check
		this.preprocessorLine("if", __condition);
		
		return rv;
	}
	
	/**
	 * Writes a preprocessor include.
	 * 
	 * @param __fileName The file name to use.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter preprocessorInclude(String __fileName)
		throws IOException, NullPointerException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		return this.preprocessorLine("include",
			"\"" + __fileName + "\"");
	}
	
	/**
	 * Outputs a preprocessor line.
	 * 
	 * @param __directive The preprocesor symbol. 
	 * @param __tokens The token to use.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public CSourceWriter preprocessorLine(String __directive,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		if (__directive == null)
			throw new NullPointerException("NARG");
		
		// Always start this directive on a fresh line
		if (this._column > 0)
			this.freshLine();
		
		// Need to restore old state when continuing
		try
		{
			// Indicate we are writing the preprocessor
			this._preprocessorLines = true;
			
			// Write out directive
			this.token("#" + __directive);
			
			// Write tokens for the directive
			if (__tokens != null && __tokens.length > 0)
				this.tokens(__tokens);
		}
		finally
		{
			this._preprocessorLines = false;
		}
		
		// Always end with a fresh line, so we can continue accordingly
		this.freshLine();
		
		// Self
		return this;
	}
	
	/**
	 * Writes a preprocessor undefine.
	 * 
	 * @param __symbol The symbol to undefine.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter preprocessorUndefine(String __symbol)
		throws IOException, NullPointerException
	{
		if (__symbol == null)
			throw new NullPointerException("NARG");
		
		return this.preprocessorLine("undef", __symbol);
	}
	
	/**
	 * Defines a struct.
	 *
	 * @param __modifiers The modifiers used.
	 * @param __structType The struct type.
	 * @param __structName The struct name.
	 * @return The block for being within the struct.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CStructVariableBlock structVariableSet(CModifier __modifiers,
		CBasicType __structType, String __structName)
		throws IOException, NullPointerException
	{
		if (__structType == null || __structName == null)
			throw new NullPointerException("NARG");
		
		// Setup new block
		CStructVariableBlock rv = new CStructVariableBlock(this._selfRef);
		this.__pushBlock(rv);
		
		// Write the variable and the opening
		this.variable(__modifiers, __structType, __structName);
		this.tokens("=", "{");
		
		return rv;
	}
	
	/**
	 * Surrounds the set of tokens with a parenthesis, with an optional
	 * prefix.
	 * 
	 * @param __prefix The of type, may be {@code null}.
	 * @param __tokens The tokens to wrap.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException
	{
		if (__prefix == null)
			return this.tokens("(", __tokens, ")");
		return this.tokens(__prefix, "(", __tokens, ")");
	}
	
	/**
	 * Surround with parenthesis, potentially delimited.
	 * 
	 * @param __prefix The prefix to use.
	 * @param __delim The delimiter to use.
	 * @param __tokens The tokens to wrap and delimit.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException If no delimiter was specified.
	 * @since 3023/05/30
	 */
	public CSourceWriter surroundDelimited(String __prefix, String __delim,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		if (__delim == null)
			throw new NullPointerException("NARG");
		
		// Open
		if (__prefix != null)
			this.token(__prefix);
		this.token("(");
		
		// Go through each item
		if (__tokens != null && __tokens.length > 0)
			for (int i = 0, n = __tokens.length; i < n; i++)
			{
				if (i > 0)
					this.token(__delim);
				this.token(__tokens[i]);
			}
		
		// Close
		return this.token(")");
	}
	
	/**
	 * Writes a single token to the output.
	 *
	 * @param __token The token to write.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If newlines or tabs were printed.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if empty as there is nothing to print
		int n = __token.length();
		if (n == 0)
			return this;
		
		// Single character token?
		char singleChar = (__token.length() == 1 ? __token.charAt(0) : 0);
		
		// If the last character we wrote is not whitespace, then we need to
		// add some space before we write this token
		char lastChar = this._lastChar;
		if (lastChar != ' ' && lastChar != '\t' &&
			lastChar != '\r' && lastChar != '\n')
			if (singleChar != ',')
				this.__out(' ');
		
		// Would this write over the side?
		if (this._column + n > CSourceWriter._GUTTER_THRESHOLD)
		{
			// Is this a string that will be written off the side of the
			// gutter?
			if (__token.charAt(0) == '"' && __token.charAt(1) == '"')
			{
				/*throw Debugging.todo();*/
			}
			
			// Move to fresh line so that all of it is on this line
			this.freshLine();
		}
		
		// Write the output
		PrintStream out = this.out;
		for (int i = 0; i < n; i++)
		{
			char c = __token.charAt(i);
			
			// {@squirreljme.error NC06 Cannot print newlines or tabs.}
			if (c == '\t' || c == '\r' || c == '\n')
				throw new IllegalArgumentException("NC06");
			
			// Output
			this.__out(c);
		}
		
		// Self
		return this;
	}
	
	/**
	 * Writes a single token to the output.
	 *
	 * @param __token The token to write.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the token type is not valid.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException
	{
		// null
		if (__token == null)
			return this.token("NULL");
			
		// Primitive arrays
		else if (__token instanceof boolean[])
			return this.array((boolean[])__token);
		else if (__token instanceof byte[])
			return this.array((byte[])__token);
		else if (__token instanceof short[])
			return this.array((short[])__token);
		else if (__token instanceof int[])
			return this.array((int[])__token);
		else if (__token instanceof long[])
			return this.array((long[])__token);
		else if (__token instanceof float[])
			return this.array((float[])__token);
		else if (__token instanceof double[])
			return this.array((double[])__token);
			
		// Forward writing of arrays
		else if (__token.getClass().isArray())
			return this.tokens((Object[])__token);
			
		// Forward writing of collections
		else if (__token instanceof Collection)
		{
			Collection<?> tokens = (Collection<?>)__token;
			
			return this.tokens(tokens.toArray(new Object[tokens.size()]));
		}
		
		// String
		else if (__token instanceof CharSequence)
			return this.token((CharSequence)__token);
			
		// A type
		else if (__token instanceof CType)
			return this.token(((CType)__token).token());
		
		// Modifiers
		else if (__token instanceof CModifiers)
			return this.token(((CModifiers)__token).modifierTokens());
		
		// Function argument
		else if (__token instanceof CFunctionArgument)
		{
			CFunctionArgument token = (CFunctionArgument)__token;
			return this.tokens(token.type, token.name);
		}
			
		// A boolean
		else if (__token instanceof Boolean)
			return this.token(((Boolean)__token) ? "JNI_TRUE" : "JNI_FALSE");
			
		// A character
		else if (__token instanceof Character)
			return this.character((Character)__token);
			
		// A number value
		else if (__token instanceof Number)
			return this.number((Number)__token);
		
		// {@squirreljme.error NC05 Unknown token type. (The type)}
		throw new IllegalArgumentException("NC05 " + __token.getClass());
	}
	
	/**
	 * Writes the specified tokens to the output.
	 *
	 * @param __tokens The tokens to write.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @since 2023/05/29
	 */
	public CSourceWriter tokens(Object... __tokens)
		throws IOException
	{
		if (__tokens == null || __tokens.length == 0)
			return this;
		
		// Write each token
		for (int i = 0, n = __tokens.length; i < n; i++)
			this.token(__tokens[i]);
		
		// Self
		return this;
	}
	
	/**
	 * Assigns the given variable.
	 * 
	 * @param __target The target variable.
	 * @param __value The value to set.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public CSourceWriter variableAssign(String __target, Object... __value)
		throws IOException, NullPointerException 
	{
		if (__target == null || __value == null || __value.length == 0)
			throw new NullPointerException("NARG");
		
		return this.tokens(__target, "=", __value, ";");
	}
	
	/**
	 * Declares a variable.
	 * 
	 * @param __modifier The modifier.
	 * @param __type The type.
	 * @param __name The name.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	public CSourceWriter variableDeclare(CModifier __modifier, CType __type,
		String __name)
		throws IOException, NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Start on fresh line for readability
		this.freshLine();
		
		this.variable(__modifier, __type, __name);
		return this.token(";");
	}
	
	/**
	 * Writes a variable to the output.
	 *
	 * @param __type The type of the variable.
	 * @param __name The name of the variable.
	 * @param __valueTokens The value tokens of this variable, if any.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter variableSet(CType __type,
		String __name, String... __valueTokens)
		throws IOException, NullPointerException
	{
		return this.variableSet(null, __type, __name,
			__valueTokens);
	}
	
	/**
	 * Writes a variable to the output.
	 *
	 * @param __modifier The modifiers to use, may be {@code null}.
	 * @param __type The type of the variable.
	 * @param __name The name of the variable.
	 * @param __valueTokens The value tokens of this variable, if any.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter variableSet(CModifier __modifier, CType __type,
		String __name, String... __valueTokens)
		throws IOException, NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		// This is nicer when it is on a fresh line
		this.freshLine();
		
		// Write base variable
		this.variable(__modifier, __type, __name);
		
		// Then write the value, if any
		if (__valueTokens == null || __valueTokens.length <= 0)
			return this.tokens(";");
		return this.tokens("=", __valueTokens, ";");
	}
	
	/**
	 * Writes a variable.
	 * 
	 * @param __modifier The modifiers to use.
	 * @param __type The variable type.
	 * @param __name The variable name.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	public CSourceWriter variable(CModifier __modifier, CType __type,
		String __name)
		throws IOException, NullPointerException
	{
		if (__type == null || __name == null)
			throw new NullPointerException("NARG");
		
		// Without modifiers
		List<String> modifiers = (__modifier == null ? null :
			__modifier.modifierTokens());
		
		if (modifiers == null || modifiers.isEmpty())
			return this.tokens(__type.token(), __name);
		return this.tokens(modifiers, __type.token(), __name);
	}
	
	/**
	 * Closes the given block.
	 * 
	 * @param __cBlock The block to close.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/29
	 */
	@SuppressWarnings("resource")
	void __close(CBlock __cBlock)
		throws IOException, NullPointerException
	{
		if (__cBlock == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error NC07 Closing block is not the last opened
		// block.}
		Deque<CBlock> blocks = this._blocks;
		CBlock peek = blocks.peek();
		if (peek != __cBlock)
			throw new IllegalStateException("NC07");
		
		// Remove it
		blocks.pop();
		
		// Write finisher
		__cBlock.__finish(this);
	}
	
	/**
	 * Writes a single character to the output.
	 * 
	 * @param __c The character to write.
	 * @throws IOException On write errors.
	 * @since 2023/05/28
	 */
	private void __out(char __c)
		throws IOException
	{
		// Write to output
		char lastChar = this._lastChar;
		
		// New line?
		if (__c == '\r' || __c == '\n')
		{
			// Do not write double newlines
			if (lastChar == '\r' || lastChar == '\n')
				return;
			
			// Move line ahead
			this._line++;
			this._column = 0;
		}
		
		// Align tabs always to four columns
		else if (__c == '\t')
			this._column += 4 - (this._column % 4);
			
		// Otherwise, increase column size
		else
			this._column++;
		
		// Just write single character, as long as it is not CR
		if (__c != '\r')
		{
			// Debug
			if (__c == '\n')
			{
				System.err.println();
				this.out.println();
			}
			else
			{
				System.err.print(__c);
				this.out.write(__c);
			}
		}
		
		// Store last character for later writes
		this._lastChar = __c;
	}
	
	/**
	 * Pushes the block to the stack.
	 * 
	 * @param <B> The type of block to push.
	 * @param __block The block to push.
	 * @return The block to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	<B extends CBlock> B __pushBlock(B __block)
		throws NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		this._blocks.push(__block);
		return __block;
	}
}
