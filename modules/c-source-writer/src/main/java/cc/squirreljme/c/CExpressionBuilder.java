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

/**
 * This builds {@link CExpression}.
 *
 * @see CExpression
 * @since 2023/06/19
 */
public class CExpressionBuilder
{
	/**
	 * Builds the C expression.
	 * 
	 * @return The C expression.
	 * @since 2023/06/19
	 */
	public final CExpression build()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Adds a C dereference expression which lead to a struct value.
	 * 
	 * @return {@code this}.
	 * @since 2023/06/19
	 */
	public CExpressionBuilder dereferenceStruct()
	{
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
	public CExpressionBuilder identifier(CVariable __var)
		throws NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
		
		throw Debugging.todo();
	}
	
	/**
	 * Creates an expression builder.
	 * 
	 * @return The builder.
	 * @since 2023/06/19
	 */
	public static final CExpressionBuilder builder()
	{
		throw Debugging.todo();
	}
}
