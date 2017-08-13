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

import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This contains the state of variables which are located in local variables
 * or placed on the stack.
 *
 * @since 2017/08/12
 */
public final class VariableState
{
	/** The number of local variables. */
	protected final int maxlocals;
	
	/** The number of stack variables. */
	protected final int maxstack;
	
	/** The number of variables. */
	protected final int numvariables;
	
	/** The local variable types. */
	private final JavaType[] _locals;
	
	/** The stack variable types. */
	private final JavaType[] _stack;
	
	/** The top of the stack. */
	private volatile int _top;
	
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
		StackMapTableState state = __smt.get(0);
		for (int i = 0; i < __ml; i++)
			locals[i] = state.getLocal(i);
		
		// Set
		this.maxstack = __ms;
		this.maxlocals = __ml;
		this.numvariables = __ms + __ml;
	}
	
	/**
	 * Obtains the given local variable.
	 *
	 * @param __i The index of the variable to get.
	 * @return The variable at the given index.
	 * @throws JITException If the variable is not within bounds.
	 * @since 2017/08/13
	 */
	public Variable getLocal(int __i)
		throws JITException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the given stack variable.
	 *
	 * @param __i The index of the variable to get.
	 * @return The variable at the given index.
	 * @throws JITException If the variable is not within bounds.
	 * @since 2017/08/13
	 */
	public Variable getStack(int __i)
		throws JITException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of local variables.
	 *
	 * @return The number of local variables.
	 * @since 2017/08/13
	 */
	public int maxLocals()
	{
		return this.maxlocals;
	}
	
	/**
	 * Returns the number of stack variables.
	 *
	 * @return The number of stack variables.
	 * @since 2017/08/13
	 */
	public int maxStack()
	{
		return this.maxstack;
	}
	
	/**
	 * Returns the number of variable which are available.
	 *
	 * @return The number of variables which are recorded.
	 * @since 2017/08/13
	 */
	public int numVariables()
	{
		return this.numvariables;
	}
}

