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
 * A comparison within C.
 *
 * @since 2023/07/03
 */
public enum CComparison
	implements COperator
{
	/** Equals. */
	EQUALS("=="),
	
	/** Not equals. */
	NOT_EQUALS("!="),
	
	/** Less than. */
	LESS_THAN("<"),
	
	/** Less than or equals. */
	LESS_EQUALS("<="),
	
	/** Greater than. */
	GREATER_THAN(">"),
	
	/** Greater than or equals. */
	GREATER_EQUALS(">="),
	
	/* End. */
	;
	
	/** The token used for the operator. */
	public final String token;
	
	/**
	 * Initializes the comparison operator.
	 * 
	 * @param __token The token used for the operator.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/03
	 */
	CComparison(String __token)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		this.token = __token;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/03
	 */
	@Override
	public String token()
	{
		return this.token;
	}
}
