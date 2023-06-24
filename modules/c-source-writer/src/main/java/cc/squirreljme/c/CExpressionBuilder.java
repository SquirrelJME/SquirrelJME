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
	 * Adds a C dereference expression which lead to a struct value.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/19
	 */
	public B dereferenceStruct()
	{
		throw Debugging.todo();
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
		
		throw Debugging.todo();
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
