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
public interface CExpression
{
	/** Invalid expression. */
	CExpression INVALID_EXPRESSION =
		new CBasicExpression();
	
	/** Empty expression. */
	CExpression EMPTY =
		new CBasicExpression();
	
	/**
	 * Returns the tokens that make up this expression.
	 * 
	 * @return The tokens that make up this expression.
	 * @since 2023/06/23
	 */
	List<String> tokens();
}
