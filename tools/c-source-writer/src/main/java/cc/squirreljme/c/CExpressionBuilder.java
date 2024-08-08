// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.c.std.CFunctionProvider;
import cc.squirreljme.c.std.CTypeProvider;
import cc.squirreljme.runtime.cldc.util.BooleanArrayList;
import cc.squirreljme.runtime.cldc.util.ByteArrayList;
import cc.squirreljme.runtime.cldc.util.CharacterArrayList;
import cc.squirreljme.runtime.cldc.util.DoubleArrayList;
import cc.squirreljme.runtime.cldc.util.FloatArrayList;
import cc.squirreljme.runtime.cldc.util.IntegerArrayList;
import cc.squirreljme.runtime.cldc.util.LongArrayList;
import cc.squirreljme.runtime.cldc.util.ShortArrayList;
import java.io.IOException;
import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * This builds {@link CExpression}.
 *
 * @see CExpression
 * @param <B> The __builder type.
 * @since 2023/06/19
 */
public abstract class CExpressionBuilder
	<B extends CExpressionBuilder<? extends B>>
{
	/** Direct token output. */
	private final Reference<CFile> _direct;
	
	/** The output tokens. */
	final List<String> _tokens;
	
	/**
	 * Initializes the expression __builder.
	 * 
	 * @param __direct Direct token access?
	 * @since 2023/06/24
	 */
	CExpressionBuilder(Reference<CFile> __direct)
	{
		if (__direct != null)
		{
			this._direct = __direct;
			this._tokens = null;
		}
		else
		{
			this._direct = null;
			this._tokens = new ArrayList<>();
		}
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(Object... __values)
		throws IOException
	{
		return this.array(Arrays.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(boolean... __values)
		throws IOException
	{
		return this.array(BooleanArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(byte... __values)
		throws IOException
	{
		return this.array(ByteArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(short... __values)
		throws IOException
	{
		return this.array(ShortArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(char... __values)
		throws IOException
	{
		return this.array(CharacterArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(int... __values)
		throws IOException
	{
		return this.array(IntegerArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(long... __values)
		throws IOException
	{
		return this.array(LongArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(float... __values)
		throws IOException
	{
		return this.array(FloatArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(double... __values)
		throws IOException
	{
		return this.array(DoubleArrayList.asList(__values));
	}
	
	/**
	 * Builds an array.
	 * 
	 * @param __values Values to set.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B array(List<?> __values)
		throws IOException
	{
		// Open block
		this.__add("{");
		
		if (__values != null)
			for (int i = 0, n = __values.size(); i < n; i++)
			{
				if (i > 0)
					this.__add(",");
				
				Object value = __values.get(i);
				if (value instanceof Number)
					this.number((Number)value);
				else if (value instanceof String)
					this.string((String)value);
				else if (value instanceof CExpression)
					this.__add(((CExpression)value).tokens());
				else
					this.__add(Objects.toString(value));
			}
		
		// Close block
		return this.__add("}");
	}
	
	/**
	 * Accesses an array.
	 * 
	 * @param __index The array access index.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B arrayAccess(int __index)
		throws IOException
	{
		this.__add("[");
		this.__add(Integer.toString(__index));
		this.__add("]");
		
		return this.__this();
	}
	
	/**
	 * Accesses an array.
	 * 
	 * @param __expression The array access index.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B arrayAccess(CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		this.__add("[");
		this.__add(__expression.tokens());
		this.__add("]");
		
		return this.__this();
	}
	
	/**
	 * Writes a value cast.
	 *
	 * @param __to The type to cast to.
	 * @param __expression The expression to cast.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public B cast(CType __to, CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__to == null || __expression == null)
			throw new NullPointerException("NARG");
		
		this.__add("(");
		this.__add("(");
		this.__add(__to.declareTokens(null));
		this.__add(")");
		
		this.__add("(");
		this.__add(__expression.tokens());
		this.__add(")");
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * Writes a value cast.
	 *
	 * @param __to The type to cast to.
	 * @param __expression The expression to cast.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public B cast(CTypeProvider __to, CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__to == null)
			throw new NullPointerException("NARG");
		
		return this.cast(__to.type(), __expression);
	}
	
	/**
	 * Compares two values.
	 * 
	 * @param __a The left side expression.
	 * @param __op The operator to use.
	 * @param __b The right side expression.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public B compare(CExpression __a, CComparison __op, CExpression __b)
		throws IOException, NullPointerException
	{
		return this.operate(__a, __op, __b);
	}
	
	/**
	 * Adds a C dereference expression which lead to a struct value.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/19
	 */
	public B dereferenceStruct()
		throws IOException
	{
		this.__add("->");
		
		return this.__this();
	}
	
	/**
	 * Adds an expression directly.
	 * 
	 * @param __expression The expression.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B expression(CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		this.__add(__expression.tokens());
		
		return this.__this();
	}
	
	/**
	 * Performs a function call in the expression.
	 * 
	 * @param __function The function to call.
	 * @param __args The function arguments.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the number of arguments to
	 * the function call is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/08/29
	 */
	public B functionCall(CFunctionProvider __function,
		CExpression... __args)
		throws IOException, IllegalArgumentException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		return this.functionCall(__function.function(), __args);
	}
	
	/**
	 * Performs a function call in the expression.
	 * 
	 * @param __function The function to call.
	 * @param __args The function arguments.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the number of arguments to
	 * the function call is not correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B functionCall(CFunctionType __function,
		CExpression... __args)
		throws IOException, IllegalArgumentException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW21 Number of arguments in function does
		not match call.} */
		List<CVariable> arguments = __function.arguments;
		if (__args.length != arguments.size())
			throw new IllegalArgumentException("CW21");
		
		// Call function
		this.__add(__function.name.identifier);
		
		// Put down all arguments
		this.__add("(");
		for (int i = 0, n = arguments.size(); i < n; i++)
		{
			// Comma split?
			if (i > 0)
				this.__add(",");
			
			this.__add(__args[i].tokens());
		}
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * Identifies an identifier.
	 * 
	 * @param __identifier The identifier to identify.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public B identifier(String __identifier)
		throws IOException, NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		this.__add(__identifier);
		
		return this.__this();
	}
	
	/**
	 * Identifies an identifier.
	 * 
	 * @param __identifier The identifier to identify.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B identifier(CIdentifier __identifier)
		throws IOException, NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		this.__add(__identifier.identifier);
		
		return this.__this();
	}
	
	/**
	 * Identifies a variable.
	 * 
	 * @param __var The variable to identify.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/19
	 */
	public B identifier(CVariable __var)
		throws IOException, NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
		
		return this.identifier(__var.name);
	}
	
	/**
	 * Performs math on two values.
	 * 
	 * @param __a The left side expression.
	 * @param __op The operator to use.
	 * @param __b The right side expression.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public B math(CExpression __a, CMathOperator __op, CExpression __b)
		throws IOException, NullPointerException
	{
		return this.operate(__a, __op, __b);
	}
	
	/**
	 * Negates the given value.
	 *
	 * @param __value The value to negate.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/16
	 */
	public B negative(CExpression __value)
		throws IOException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		this.__add("-");
		this.__add("(");
		this.__add(__value.tokens());
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * Adds the not expression.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B not()
		throws IOException
	{
		this.__add("!");
		
		return this.__this();
	}
	
	/**
	 * Adds the not expression.
	 * 
	 * @param __expression The expression to operate on.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B not(CExpression __expression)
		throws IOException
	{
		this.__add("!");
		this.__add("(");
		this.__add(__expression.tokens());
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * Writes the specified number.
	 * 
	 * @param __value The value to store.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the number is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B number(Number __value)
		throws IOException, IllegalArgumentException, NullPointerException
	{
		return this.number(null, __value);
	}
	
	/**
	 * Writes the specified number.
	 * 
	 * @param __type The type of number this is.
	 * @param __value The value to store.
	 * @return {@code this}.
	 * @throws IllegalArgumentException If the number is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B number(CNumberType __type, Number __value)
		throws IOException, IllegalArgumentException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error CW22 Unsupported number.} */
		if (__value instanceof Float || __value instanceof Double)
			throw new IllegalArgumentException("CW22");
		
		// Prefix/suffix/surround type, possibly?
		String string = Long.toString(__value.longValue(), 10);
		if (__type != null)
		{
			String prefix = __type.prefix();
			String suffix = __type.suffix();
			String surround = __type.surround();
			
			// Base surround?
			if (surround != null)
			{
				this.__add(surround);
				this.__add("(");
			}
			
			// Put in the base number with its prefix and/or suffix
			if (prefix != null)
				if (suffix != null)
					this.__add(prefix + string + suffix);
				else
					this.__add(prefix + string);
			else
				if (suffix != null)
					this.__add(string + suffix);
				else
					this.__add(string);
			
			// End surround?
			if (surround != null)
				this.__add(")");
		}
		
		// Just plain digit
		else
			this.__add(string);
		
		return this.__this();
	}
	
	/**
	 * Operates on two values.
	 * 
	 * @param __a The left side expression.
	 * @param __op The operator to use.
	 * @param __b The right side expression.
	 * @return {@code this}.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public B operate(CExpression __a, COperator __op, CExpression __b)
		throws IOException, NullPointerException
	{
		if (__a == null || __op == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Protect with parenthesis
		this.__add("(");
		this.__add("(");
		this.__add(__a.tokens());
		this.__add(")");
		
		this.__add(__op.token());
		
		// Protect with parenthesis
		this.__add("(");
		this.__add(__b.tokens());
		this.__add(")");
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * Returns a __builder to make an expression in a parenthesis statement.
	 * 
	 * @return The sub expression __builder.
	 * @since 2023/06/24
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public CSubExpressionBuilder<B> parenthesis()
		throws IOException
	{
		this.__add("(");
		return (CSubExpressionBuilder<B>)
			new CSubExpressionBuilder(this, ")");
	}
	
	/**
	 * Wraps a defined statement.
	 * 
	 * @param __identifier The identifier.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B preprocessorDefined(CIdentifier __identifier)
		throws IOException, NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		this.__add("defined");
		this.__add("(");
		this.__add(__identifier.identifier);
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * References an identifier.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B reference()
		throws IOException
	{
		this.__add("&");
		
		return this.__this();
	}
	
	/**
	 * References an identifier.
	 * 
	 * @param __expression The expression
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	public B reference(CExpression __expression)
		throws IOException, NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		this.__add("&");
		this.__add("(");
		this.__add(__expression.tokens());
		this.__add(")");
		
		return this.__this();
	}
	
	/**
	 * Writes the string to the output.
	 * 
	 * @param __string The string to write.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	public B string(String __string)
		throws IOException, NullPointerException
	{
		if (__string == null)
			throw new NullPointerException("NARG");
		
		this.__add(CUtils.quotedString(__string));
		
		return this.__this();
	}
	
	/**
	 * Accesses a struct.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B structAccess()
		throws IOException
	{
		this.__add(".");
		
		return this.__this();
	}
	
	/**
	 * Adds the given token.
	 * 
	 * @param __token The token to add.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	final B __add(String __token)
		throws IOException
	{
		// Use direct output access?
		if (this._direct != null)
		{
			CFile file = this._direct.get();
			if (file == null)
				throw new IllegalStateException("GCGC");
			
			file.token(__token, false);
		}
		
		// Otherwise enqueue
		else
			this._tokens.add(__token);
		
		return this.__this();
	}
	
	/**
	 * Adds the given tokens.
	 * 
	 * @param __tokens The tokens to add.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	final B __add(String... __tokens)
		throws IOException
	{
		for (String token : __tokens)
			this.__add(token);
		return this.__this();
	}
	
	/**
	 * Adds the given tokens.
	 * 
	 * @param __tokens The tokens to add.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	final B __add(Collection<String> __tokens)
		throws IOException
	{
		for (String token : __tokens)
			this.__add(token);
		return this.__this();
	}
	
	/**
	 * Returns {@code this}.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	@SuppressWarnings("unchecked")
	final B __this()
	{
		return (B)this;
	}
	
	/**
	 * Creates an expression __builder.
	 * 
	 * @return The __builder.
	 * @since 2023/06/19
	 */
	public static final CRootExpressionBuilder builder()
	{
		return CExpressionBuilder.__builder(null);
	}
	
	/**
	 * Creates an expression __builder.
	 * 
	 * @param __direct Direct token access?
	 * @return The __builder.
	 * @since 2023/06/24
	 */
	static final CRootExpressionBuilder __builder(CFile __direct)
	{
		return new CRootExpressionBuilder((__direct == null ? null :
			__direct._fileRef));
	}
}
