// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This contains the state of variables which are located in local variables
 * or placed on the stack.
 *
 * @since 2017/08/12
 */
public final class VariableState
{
	/** The local variable types. */
	private final JavaType[] _locals;
	
	/** The stack variable types. */
	private final JavaType[] _stack;
	
	/** The top of the stack. */
	private final int _top;
	
	/**
	 * Initializes the variable state.
	 *
	 * @param __smt The stack map table.
	 * @param __ms The number of entries on the stack.
	 * @param __ml The number of entries in locals variables.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/12
	 */
	public VariableState(StackMapTable __smt, int __ms, int __ml)
		throws NullPointerException
	{
		// Check
		if (__smt == null)
			throw new NullPointerException("NARG");
		
		// Set
		JavaType[] locals = new JavaType[__ml];
		this._locals = locals;
		this._stack = new JavaType[__ms];
		
		// Initialize locals
		throw new todo.TODO();
	}
}

