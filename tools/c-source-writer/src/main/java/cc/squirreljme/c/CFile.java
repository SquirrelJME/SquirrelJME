// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.out.CPivotPoint;
import cc.squirreljme.c.out.CTokenOutput;
import cc.squirreljme.c.std.CFunctionProvider;
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
	
	/** Was the last written token a newline? */
	private volatile boolean _lastNewline;
	
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
		CExpressionBuilder.__builder(this)
			.array(__values);
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
		return this.number(CPrimitiveNumberType.UNSIGNED, (int)__c);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/28
	 */
	@Override
	public void close()
		throws IOException
	{
		// Close the output
		this.out.close();
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
		CBlock rv = new CBlock(this, "}", false);
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
	public CSourceWriter declare(CFunctionType __function)
		throws IOException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		// Emit
		this.tokens(__function.declareTokens(null), ";");
		
		return this;
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
		
		this.tokens(__var.declareTokens(), ";");
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/19
	 */
	@Override
	public <B extends CBlock> B define(Class<B> __blockType, CVariable __var)
		throws IOException, NullPointerException
	{
		if (__blockType == null || __var == null)
			throw new NullPointerException("NARG");
		
		// Get the root type we are using
		CType type = __var.type();
		CType subType = null;
		if (type instanceof CModifiedType)
			subType = ((CModifiedType)type).type;
		else
			subType = type;
		
		// Structure type
		if (subType instanceof CStructType)
		{
			CStructType struct = (CStructType)subType;
			
			// Open struct
			this.tokens(type.declareTokens(null),
				__var.name, "=", "{");
			
			// Setup block
			CStructVariableBlock rv = new CStructVariableBlock(
				this, struct, "};");
			this.__pushBlock(rv, true);
			return __blockType.cast(rv);
		}
		
		// Unknown??
		else
			throw Debugging.todo(__var.getClass());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/04
	 */
	@Override
	public CSourceWriter define(CStructType __what)
		throws IOException, NullPointerException
	{
		if (__what == null)
			throw new NullPointerException("NARG");
		
		// Open struct saying what it is
		this.token(__what.declareTokens(null));
		try (CBlock block = this.curly())
		{
			for (CVariable member : __what.members)
				block.declare(member);
		}
		
		// End semicolon
		this.token(";");
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/30
	 */
	@Override
	public CFunctionBlock define(CFunctionType __function)
		throws IOException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		// Open up function
		this.tokens(__function.declareTokens(null), "{");
		
		// Push block for it
		return this.__pushBlock(new CFunctionBlock(this), true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public CSourceWriter define(CVariable __variable,
		CExpression __expression)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__variable == null || __expression == null)
			throw new NullPointerException("NARG");
		
		// Declare and emit soft newline after declaration, to add some space
		this.tokens(__variable.declareTokens(true), "=",
			__expression, ";");
		return this.newLine(false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter expression(CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		this.token(__expression);
		return this;
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
		// Ignore if there was a newline
		if (this._lastNewline)
			return this;
		
		// Emit newline
		this.out.newLine(true);
		
		// Last was whitespace
		this._lastWhitespace = true;
		this._lastNewline = true;
		
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/31
	 */
	@Override
	public CSourceWriter functionCall(
		CFunctionType __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		// Just forward to expression __builder
		CExpressionBuilder.__builder(this)
			.functionCall(__function, __args)
			.build();
		this.tokens(";");
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/06/24
	 */
	@Override
	public CSourceWriter functionCall(CFunctionProvider __function,
		CExpression... __args)
		throws IOException, NullPointerException
	{
		return this.functionCall(__function.function(), __args);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter gotoLabel(String __target)
		throws IOException, NullPointerException
	{
		return this.gotoLabel(CIdentifier.of(__target));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter gotoLabel(CIdentifier __target)
		throws IOException, NullPointerException
	{
		if (__target == null)
			throw new NullPointerException("NARG");
		
		return this.tokens("goto", __target, ";");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public CPPBlock headerGuard(String __fileName)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.headerGuard(CFileName.of(__fileName));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/08/12
	 */
	@Override
	public CPPBlock headerGuard(CFileName __fileName)
		throws IOException, NullPointerException
	{
		if (__fileName == null)
			throw new NullPointerException("NARG");
		
		// Determine how the header guard should be named
		StringBuilder builder = new StringBuilder(__fileName.fileName);
		builder.insert(0, "SJME_HG_");
		for (int i = 0, n = builder.length(); i < n; i++)
		{
			char c = builder.charAt(i);
			
			// Capitalize letters
			if (c >= 'a' && c <= 'z')
				c = (char)('A' + (c - 'a'));
			
			// Turn slashes and dots to underscores
			else if (c == '/' || c == '.')
				c = '_';
			
			// Ignore otherwise
			else
				continue;
			
			// Replace
			builder.setCharAt(i, c);
		}
		
		// Create identifier
		CIdentifier identifier = CIdentifier.of(builder.toString());
		
		// Define the guard to prevent future use of this header
		CPPBlock block = this.preprocessorIf(CExpressionBuilder.builder()
			.not()
			.preprocessorDefined(identifier)
			.build());
		block.preprocessorDefine(identifier, null);
			
		// Return block for writing
		return block;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter indent(int __by)
		throws IOException
	{
		this.out.indent(__by);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter label(String __label)
		throws IOException, NullPointerException
	{
		return this.label(CIdentifier.of(__label));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/15
	 */
	@Override
	public CSourceWriter label(CIdentifier __label)
		throws IOException, NullPointerException
	{
		if (__label == null)
			throw new NullPointerException("NARG");
		
		return this.tokens(__label, ":");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/19
	 */
	@Override
	public CSourceWriter newLine(boolean __force)
		throws IOException
	{
		this.out.newLine(true);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter number(Number __number)
		throws IOException, NullPointerException
	{
		return this.number(null, __number);
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
		
		CExpressionBuilder.__builder(this)
			.number(__type, __number)
			.build();
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/22
	 */
	@Override
	public CSourceWriter pivot(CPivotPoint __pivot)
		throws IOException, NullPointerException
	{
		if (__pivot == null)
			throw new NullPointerException("NARG");
		
		this.out.pivot(__pivot);
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CSourceWriter preprocessorDefine(CIdentifier __symbol,
		CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__symbol == null)
			throw new NullPointerException("NARG");
		
		if (__expression == null)
			return this.preprocessorLine(CPPDirective.DEFINE, __symbol);
		return this.preprocessorLine(CPPDirective.DEFINE,
			__symbol, __expression);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/05/29
	 */
	@Override
	public CPPBlock preprocessorIf(CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		// Start the check
		this.preprocessorLine(CPPDirective.IF, __expression);
		
		// Setup new block
		CPPBlock rv = new CPPBlock(this);
		this.__pushBlock(rv, false);
		
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
		this.freshLine();
		
		// Write out directive
		this.token("#" + __directive.directive);
		
		// Write tokens for the directive
		if (__tokens != null && __tokens.length > 0)
			this.tokens(__tokens);
		
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
			{
				// Make sure a space is before this token if there was none
				// before...
				if (!this._lastWhitespace)
					out.space();
				
				out.token(__token, __forceNewline);
			}
		}
		else
		{
			// Make sure a space is before this token if there was none
			// before...
			if (!this._lastWhitespace)
				out.space();
				
			out.token(__token, __forceNewline);
		}
		
		// Ends on newline or forced newline?
		char endChar = __token.charAt(n - 1); 
		if (__forceNewline || endChar == '\r' || endChar == '\n')
		{
			if (endChar != '\r' && endChar != '\n')
				out.newLine(true);
			
			this._lastNewline = true;
			this._lastWhitespace = true;
		}
		
		// Normal whitespace
		else if (endChar == ' ' || endChar == '\t')
		{
			this._lastNewline = false;
			this._lastWhitespace = true;
		}
		
		// Clear these otherwise
		else
		{
			this._lastNewline = false;
			this._lastWhitespace = false;
		}
		
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
		
		// A C Expression
		if (__token instanceof CExpression)
		{
			/* {@squirreljme.error CW36 Cannot output token in such way.} */
			if (__token == CExpression.INVALID_EXPRESSION)
				throw new IllegalArgumentException("CW36");
			
			return this.token(((CExpression)__token).tokens());
		}
			
		// A C identifier
		else if (__token instanceof CIdentifier)
			return this.token(((CIdentifier)__token).identifier);
		
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
		
		/* {@squirreljme.error CW05 Unknown token type. (The type)} */
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
		if (__var == null || __value == null)
			throw new NullPointerException("NARG");
		
		return this.variableSet(__var.name, __value);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/03
	 */
	@Override
	public CSourceWriter variableSet(CExpression __var, CExpression __value)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		if (__var == null || __value == null)
			throw new NullPointerException("NARG");
		
		return this.tokens(__var, "=", __value, ";");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public CSourceWriter variableSetViaFunction(CExpression __var,
		CFunctionType __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.variableSet(__var, CExpressionBuilder.builder()
				.functionCall(__function, __args)
			.build());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public CSourceWriter variableSetViaFunction(CExpression __var,
		CFunctionProvider __function, CExpression... __args)
		throws IllegalArgumentException, IOException, NullPointerException
	{
		return this.variableSetViaFunction(__var,
			__function.function(), __args);
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
		
		/* {@squirreljme.error CW07 Closing block is not the last opened
		block.} */
		Deque<CBlock> blocks = this._blocks;
		CBlock peek = blocks.peek();
		if (peek != __cBlock)
			throw new IllegalStateException("CW07");
		
		// Remove it
		blocks.pop();
		
		// Indent before the finisher?
		if (__cBlock.indentBeforeFinish)
			this.out.indent(-(1 + __cBlock.extraIndent));
		
		// Write finisher
		__cBlock.__finish();
		
		// Indent down, with any potential extra indentation that was added
		if (!__cBlock.indentBeforeFinish)
			this.out.indent(-(1 + __cBlock.extraIndent));
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
