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
import java.util.List;

/**
 * Builder for sub-expressions.
 *
 * @param <P> The parent type.
 * @since 2023/06/24
 */
public class CSubExpressionBuilder<P extends CExpressionBuilder<? extends P>>
	extends CExpressionBuilder<CSubExpressionBuilder<? extends P>>
{
	/** The closing token. */
	protected final String closing;
	
	/** The parent builder. */
	protected final P parent;
	
	/**
	 * Initializes the expression builder.
	 * 
	 * @param __parent The parent builder.
	 * @param __closing The closing token.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/06/24
	 */
	CSubExpressionBuilder(P __parent, String __closing)
		throws NullPointerException
	{
		if (__parent == null || __closing == null)
			throw new NullPointerException("NARG");
		
		this.parent = __parent;
		this.closing = __closing;
	}
	
	/**
	 * Closes this expression and returns to the parent.
	 * 
	 * @return The parent expression builder.
	 * @since 2023/06/24
	 */
	public P close()
	{
		List<String> parentTokens = this.parent.tokens;
		
		parentTokens.addAll(this.tokens);
		parentTokens.add(this.closing);
		
		return this.parent;
	}
}
