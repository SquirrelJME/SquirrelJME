// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.ArrayList;
import java.util.List;

/**
 * This builds {@link CExpression}.
 *
 * @see CExpression
 * @param <B> The builder type.
 * @since 2023/06/19
 */
public abstract class CExpressionBuilder
	<B extends CExpressionBuilder<? extends B>>
{
	/** The output tokens. */
	protected final List<String> tokens =
		new ArrayList<>();
	
	/**
	 * Internal only.
	 * 
	 * @since 2023/06/24
	 */
	CExpressionBuilder()
	{
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
	{
		List<String> tokens = this.tokens;
		
		tokens.add("[");
		tokens.add(Integer.toString(__index));
		tokens.add("]");
		
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
		throws NullPointerException
	{
		if (__expression == null)
			throw new NullPointerException("NARG");
		
		List<String> tokens = this.tokens;
		
		tokens.add("[");
		tokens.addAll(__expression.tokens);
		tokens.add("]");
		
		return this.__this();
	}
	
	/**
	 * Adds a C dereference expression which lead to a struct value.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/19
	 */
	public B dereferenceStruct()
	{
		this.tokens.add("->");
		
		return this.__this();
	}
	
	/**
	 * Adds an expression directly.
	 * 
	 * @param __expression The expression.
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B expression(CExpression __expression)
	{
		throw Debugging.todo();
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
		throws IllegalArgumentException, NullPointerException
	{
		if (__function == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CW21 Number of arguments in function does
		// not match call.}
		if (__args.length != __function.arguments.size())
			throw new IllegalArgumentException("CW21");
		
		throw Debugging.todo();
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
		throws NullPointerException
	{
		if (__identifier == null)
			throw new NullPointerException("NARG");
		
		this.tokens.add(__identifier.identifier);
		
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
		throws NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
		
		return this.identifier(__var.name);
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
		throws IllegalArgumentException, NullPointerException
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
		throws IllegalArgumentException, NullPointerException
	{
		if (__value == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CW22 Unsupported number.}
		if (__value instanceof Float || __value instanceof Double)
			throw new IllegalArgumentException("CW22");
		
		List<String> tokens = this.tokens;
		
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
				tokens.add(surround);
				tokens.add("(");
			}
			
			// Put in the base number with its prefix and/or suffix
			if (prefix != null)
				if (suffix != null)
					tokens.add(prefix + string + suffix);
				else
					tokens.add(prefix + string);
			else
				if (suffix != null)
					tokens.add(string + suffix);
				else
					tokens.add(string);
			
			// End surround?
			if (surround != null)
				tokens.add(")");
		}
		
		// Just plain digit
		else
			tokens.add(string);
		
		return this.__this();
	}
	
	/**
	 * Returns a builder to make an expression in a parenthesis statement.
	 * 
	 * @return The sub expression builder.
	 * @since 2023/06/24
	 */
	@SuppressWarnings({"unchecked", "rawtypes"})
	public CSubExpressionBuilder<B> parenthesis()
	{
		this.tokens.add("(");
		return (CSubExpressionBuilder<B>)
			new CSubExpressionBuilder(this, ")");
	}
	
	/**
	 * References a type.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B reference()
	{
		this.tokens.add("&");
		
		return this.__this();
	}
	
	/**
	 * Accesses a struct.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/24
	 */
	public B structAccess()
	{
		this.tokens.add(".");
		
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
	 * Creates an expression builder.
	 * 
	 * @return The builder.
	 * @since 2023/06/19
	 */
	public static final CRootExpressionBuilder builder()
	{
		return new CRootExpressionBuilder();
	}
}
