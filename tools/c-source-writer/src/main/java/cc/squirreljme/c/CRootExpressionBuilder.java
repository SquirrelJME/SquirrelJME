// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.c;

import java.lang.ref.Reference;
import java.util.List;

/**
 * Root expression __builder.
 *
 * @since 2023/06/24
 */
public class CRootExpressionBuilder
	extends CExpressionBuilder<CRootExpressionBuilder>
{
	/**
	 * Initializes the root token __builder.
	 * 
	 * @param __direct Direct token output?
	 * @since 2023/06/24
	 */
	CRootExpressionBuilder(Reference<CFile> __direct)
	{
		super(__direct);
	}
	
	/**
	 * Builds the C expression.
	 * 
	 * @return The C expression.
	 * @since 2023/06/19
	 */
	public final CExpression build()
	{
		List<String> tokens = this._tokens;
		
		if (tokens == null)
			return CExpression.INVALID_EXPRESSION;
		return new CBasicExpression(tokens.toArray(new String[tokens.size()]));
	}
}
