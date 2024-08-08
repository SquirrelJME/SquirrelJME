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
 * Represents a math operator.
 *
 * @since 2023/07/04
 */
public enum CMathOperator
	implements COperator
{
	/** Add. */
	ADD("+"),
	
	/** Subtract. */
	SUBTRACT("-"),
	
	/** Multiply. */
	MULTIPLY("*"),
	
	/** Divide. */
	DIVIDE("/"),
	
	/** Remainder. */
	REMAINDER("%"),
	
	/** And. */
	AND("&"),
	
	/** Or. */
	OR("|"),
	
	/** Xor. */
	XOR("^"),
	
	/** Bit shift left. */
	SHIFT_LEFT("<<"),
	
	/** Bit shift right. */
	SHIFT_RIGHT(">>"),
	
	/* End. */
	;
	
	/** The token used for the operator. */
	protected String token;
	
	/**
	 * Initializes the operator.
	 * 
	 * @param __token The token used.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/07/04
	 */
	CMathOperator(String __token)
		throws NullPointerException
	{
		if (__token == null)
			throw new NullPointerException("NARG");
		
		this.token = __token;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/04
	 */
	@Override
	public String token()
	{
		return this.token;
	}
}
