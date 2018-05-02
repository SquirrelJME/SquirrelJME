// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.syntax;

/**
 * Represents a prefix operation.
 *
 * @since 2018/05/02
 */
public enum PrefixOperatorSyntax
	implements SubExpressionSyntax
{
	/** Increment. */
	INCREMENT,
	
	/** Decrement. */
	DECREMENT,
	
	/** Not. */
	NOT,
	
	/** One's complement. */
	ONES_COMPLEMENT,
	
	/** Positive. */
	POSITIVE,
	
	/** Negative. */
	NEGATIVE,
	
	/** End. */
	;
}

