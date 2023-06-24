// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.util.Arrays;
import java.util.List;
import net.multiphasicapps.collections.UnmodifiableList;

/**
 * Represents a C expression, which is a group of tokens that does something.
 *
 * @see CExpressionBuilder
 * @since 2023/06/19
 */
public class CExpression
{
	/** Invalid expression. */
	static final CExpression _INVALID_EXPRESSION =
		new CExpression();
	
	/** The tokens used in the expression. */
	protected final List<String> tokens;
	
	/**
	 * Returns the tokens used for the expression.
	 * 
	 * @param __tokens The tokens used for the expression.
	 * @since 2023/06/24
	 */
	CExpression(String... __tokens)
	{
		this.tokens = UnmodifiableList.of(Arrays.asList(__tokens));
	}
	
	/**
	 * Returns the tokens that make up this expression.
	 * 
	 * @return The tokens that make up this expression.
	 * @since 2023/06/23
	 */
	public List<String> tokens()
	{
		return this.tokens;
	}
}
