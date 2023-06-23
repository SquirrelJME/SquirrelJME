// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.out.CTokenOutput;
import cc.squirreljme.runtime.cldc.debug.Debugging;
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
import java.io.OutputStream;
import java.io.Writer;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.List;

/**
 * Represents a C file to be written.
 *
 * @since 2023/06/04
 */
public class CFile
	implements Closeable, CSourceWriter
{
	/** The stream to write to. */
	protected final CTokenOutput out;
	
	/** C Block stack. */
	private final Deque<CBlock> _blocks =
		new ArrayDeque<>();
	
	/** Reference to self, for blocks. */
	final Reference<CFile> _fileRef =
		new WeakReference<>(this);
	
	/** Was the last written token a whitespace? */
	private volatile boolean _lastWhitespace;
	
	/**
	 * Initializes the C source writer.
	 * 
	 * @param __out The stream output.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/28
	 */
	public CFile(CTokenOutput __out)
		throws NullPointerException
	{
		if (__out == null)
			throw new NullPointerException("NARG");
		
		this.out = __out;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(Object... __values)
		throws IOException
	{
		return this.array(Arrays.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(boolean... __values)
		throws IOException
	{
		return this.array(BooleanArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(byte... __values)
		throws IOException
	{
		return this.array(ByteArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(short... __values)
		throws IOException
	{
		return this.array(ShortArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(char... __values)
		throws IOException
	{
		return this.array(CharacterArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(int... __values)
		throws IOException
	{
		return this.array(IntegerArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(long... __values)
		throws IOException
	{
		return this.array(LongArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(float... __values)
		throws IOException
	{
		return this.array(FloatArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter array(double... __values)
		throws IOException
	{
		return this.array(DoubleArrayList.asList(__values));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
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
		if (this.out instanceof AutoCloseable)
			try
			{
				((AutoCloseable)this.out).close();
			}
			catch (Exception __e)
			{
				if (__e instanceof RuntimeException)
					throw (RuntimeException)__e;
				throw new IOException("CLOS", __e);
			}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CBlock curly()
		throws IOException
	{
		// Output open block
		CBlock rv = new CBlock(this, "}");
		this.token("{");
		
		// Setup new block
		this.__pushBlock(rv, true);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CFunctionBlock declare(CFunctionType __function)
		throws IOException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter declare(CVariable __var)
		throws IOException, NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
			
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public <B extends CBlock> B declare(Class<B> __blockType, CVariable __var)
		throws IOException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter define(CDefinable __what)
		throws IOException, NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		
		
		throw Debugging.todo();
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
		if (this.out instanceof OutputStream)
			((OutputStream)this.out).flush();
		else if (this.out instanceof Writer)
			((Writer)this.out).flush();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public CSourceWriter freshLine()
		throws IOException
	{
		// Emit newline
		this.out.newLine(false);
		
		// Last was whitespace
		this._lastWhitespace = true;
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/30
	 */
	@Override
	public CSourceWriter function(CModifier __modifier, CIdentifier __name,
		CType __returnVal, CVariable... __arguments)
		throws IOException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Write modifiers first
		if (__modifier != null)
			this.token(__modifier);
		
		// Return value?
		this.token((__returnVal == null ? CPrimitiveType.VOID : __returnVal));
		
		// Function name and arguments
		return this.surroundDelimited(__name.toString(), ",",
			(Object[])__arguments);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/31
	 */
	@Override
	public CSourceWriter functionCall(CIdentifier __function, Object... __args)
		throws IOException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		this.surroundDelimited(__function.identifier, ",", __args);
		return this.token(";");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/30
	 */
	@Override
	public CFunctionBlock functionDefine(CModifier __modifier,
		CIdentifier __name, CType __returnVal,
		CVariable... __arguments)
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
		return this.__pushBlock(new CFunctionBlock(this), true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/30
	 */
	@Override
	public CSourceWriter functionPrototype(CModifier __modifier,
		CIdentifier __name, CType __returnVal,
		CVariable... __arguments)
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
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter preprocessorDefine(CIdentifier __symbol,
		Object... __tokens)
		throws IOException, NullPointerException
	{
		if (__symbol == null)
			throw new NullPointerException("NARG");
		
		if (__tokens == null || __tokens.length == 0)
			return this.preprocessorLine(CPPDirective.DEFINE,
				__symbol);
		return this.preprocessorLine(CPPDirective.DEFINE,
			__symbol, __tokens);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CPPBlock preprocessorIf(Object... __condition)
		throws IOException, NullPointerException
	{
		if (__condition == null || __condition.length == 0)
			throw new NullPointerException("NARG");
		
		// Setup new block
		CPPBlock rv = new CPPBlock(this);
		this.__pushBlock(rv, false);
		
		// Start the check
		this.preprocessorLine(CPPDirective.IF, __condition);
		
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter preprocessorInclude(CFileName __fileName)
		throws IOException, NullPointerException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		return this.preprocessorLine(CPPDirective.INCLUDE,
			"\"" + __fileName.fileName + "\"");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public CSourceWriter preprocessorLine(CPPDirective __directive,
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
			this.token("#" + __directive.directive);
			
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
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter preprocessorUndefine(CIdentifier __symbol)
		throws IOException, NullPointerException
	{
		if (__symbol == null)
			throw new NullPointerException("NARG");
		
		return this.preprocessorLine(CPPDirective.UNDEF, __symbol);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/03
	 */
	@Override
	public CSourceWriter returnValue(CExpression __expression)
		throws IOException
	{
		if (__expression == null)
			return this.tokens("return", ";");
		return this.tokens("return", __expression, ";");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter surround(String __prefix, Object... __tokens)
		throws IOException
	{
		if (__prefix == null)
			return this.tokens("(", __tokens, ")");
		return this.tokens(__prefix, "(", __tokens, ")");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 3023/05/30
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2023/06/23
	 */
	@Override
	public CSourceWriter token(CharSequence __token)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.token(__token, false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/23
	 */
	@Override
	public CSourceWriter token(CharSequence __token, boolean __forceNewline)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if empty as there is nothing to print
		int n = __token.length();
		if (n == 0)
			return this;
		
		// Forward
		CTokenOutput out = this.out;
		char startChar = __token.charAt(0);
		if (n == 1)
		{
			// Map single character outputs like this to spaces and whatnot
			if (startChar == ' ')
				out.space();
			else if (startChar == '\t')
				out.tab();
			else if (startChar == '\r' || startChar == '\n')
				out.newLine(__forceNewline);
			else
				out.token(__token, __forceNewline);
		}
		else
			out.token(__token, __forceNewline);
		
		// Did the token end on whitespace or was newline forced?
		char endChar = __token.charAt(n - 1); 
		if (__forceNewline ||
			endChar == '\r' || endChar == '\n' ||
			endChar == ' ' || endChar == '\t')
			this._lastWhitespace = true;
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter token(Object __token)
		throws IllegalArgumentException, IOException
	{
		// null
		if (__token == null)
			return this.token("NULL");
			
		// Primitive arrays
		if (__token instanceof boolean[])
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
		
		// String or character sequences
		else if (__token instanceof CharSequence)
			return this.token((CharSequence)__token);
		
		// A boolean
		else if (__token instanceof Boolean)
			return this.token(((Boolean)__token) ? "JNI_TRUE" : "JNI_FALSE");
			
		// A character
		else if (__token instanceof Character)
			return this.character((Character)__token);
			
		// A number value
		else if (__token instanceof Number)
			return this.number((Number)__token);
		
		// A C Expression
		else if (__token instanceof CExpression)
			return this.token(((CExpression)__token).tokens());
		
		// {@squirreljme.error CW05 Unknown token type. (The type)}
		throw new IllegalArgumentException("CW05 " + __token.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
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
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public CSourceWriter variableSet(CVariable __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		throw Debugging.todo();
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
		
		// {@squirreljme.error CW07 Closing block is not the last opened
		// block.}
		Deque<CBlock> blocks = this._blocks;
		CBlock peek = blocks.peek();
		if (peek != __cBlock)
			throw new IllegalStateException("CW07");
		
		// Remove it
		blocks.pop();
		
		// Write finisher
		__cBlock.__finish();
	}
	
	/**
	 * Pushes the block to the stack.
	 *
	 * @param <B> The type of block to push.
	 * @param __block The block to push.
	 * @param __indentUp Indentation is upped?
	 * @return The block to push.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	<B extends CBlock> B __pushBlock(B __block, boolean __indentUp)
		throws NullPointerException
	{
		if (__block == null)
			throw new NullPointerException("NARG");
		
		// Increase indentation?
		if (__indentUp)
			this.out.indent(1);
		
		// Push block into
		this._blocks.push(__block);
		return __block;
	}
}
