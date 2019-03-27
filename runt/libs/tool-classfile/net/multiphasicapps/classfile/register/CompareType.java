// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

/**
 * This represents the type of comparison to perform.
 *
 * @since 2019/03/26
 */
public enum CompareType
{
	/** Equals. */
	EQUALS,
	
	/** Not equals. */
	NOT_EQUALS,
	
	/** Less than. */
	LESS_THAN,
	
	/** Less or equals. */
	LESS_THAN_OR_EQUALS,
	
	/** Greater than. */
	GREATER_THAN,
	
	/** Greater or equals. */
	GREATER_THAN_OR_EQUALS,
	
	/** End. */
	;
	
	/**
	 * The operation used when comparing against zero.
	 *
	 * @return The operation to use when comparing against zero.
	 * @since 2019/03/27
	 */
	public final int ifZeroOperation()
	{
		switch (this)
		{
			case EQUALS:
				return RegisterOperationType.IFEQ;
			case NOT_EQUALS:
				return RegisterOperationType.IFNE;
			case LESS_THAN:
				return RegisterOperationType.IFLT;
			case LESS_THAN_OR_EQUALS:
				return RegisterOperationType.IFLE;
			case GREATER_THAN:
				return RegisterOperationType.IFGT;
			case GREATER_THAN_OR_EQUALS:
				return RegisterOperationType.IFGE;
		}
		
		// Should not be reached
		throw new todo.OOPS();
	}
}

