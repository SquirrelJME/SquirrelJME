// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter;

/**
 * This represents a single link in the chain of operations that a method
 * performs.
 *
 * Each link is unique and has a unique ID.
 *
 * @since 2016/03/24
 */
public class JVMOperatorLink
{
	/**
	 * The operation being performed on another link, if applicable.
	 *
	 * @since 2016/03/24
	 */
	public static enum Operation
	{
		/** Set of a value. */
		SET,
		
		/** Add. */
		ADD,
		
		/** Subtract. */
		SUBTRACT,
		
		/** Multiply. */
		MULTIPLY,
		
		/** Division. */
		DIVIDE,
		
		/** Remainder. */
		REMAINDER,
		
		/** Binary AND. */
		BINARY_AND,
		
		/** Binary OR. */
		BINARY_OR,
		
		/** Binary XOR. */
		BINARY_XOR,
		
		/** Shift left. */
		SHIFT_LEFT,
		
		/** Logical shift right. */
		LOGICAL_SHIFT_RIGHT,
		
		/** Arithmetic shift right. */
		ARITHMETIC_SHIFT_RIGHT,
		
		/** End. */
		;
	}
}

