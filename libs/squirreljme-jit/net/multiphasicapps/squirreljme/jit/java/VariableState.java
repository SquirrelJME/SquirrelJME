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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This contains the state of variables which are located in local variables
 * or placed on the stack.
 *
 * @since 2017/08/12
 */
public final class VariableState
{
	/** Local variables. */
	protected final VariableTread locals;
	
	/** Stack variables. */
	protected final VariableTread stack;
	
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
		
		// Initialize treads
		VariableTread locals;
		this.locals =
			(locals = new VariableTread(VariableLocation.LOCAL, __ml, false));
		this.stack = new VariableTread(VariableLocation.STACK, __ms, true);
		
		// Initialize local variables from the stack map state
		StackMapTableState state = __smt.get(0);
		for (int i = 0; i < __ml; i++)
		{
			StackMapTableEntry e = state.getLocal(i);
			if (e != null)
				locals.__set(i, e);
		}
	}
	
	/**
	 * Returns the local variable tread.
	 *
	 * @return The local variable tread.
	 * @since 2017/09/02
	 */
	public VariableTread locals()
	{
		return this.locals;
	}
	
	/**
	 * Returns the number of local variables.
	 *
	 * @return The number of local variables.
	 * @since 2017/08/13
	 */
	public int maxLocals()
	{
		return this.locals.storageSize();
	}
	
	/**
	 * Returns the number of stack variables.
	 *
	 * @return The number of stack variables.
	 * @since 2017/08/13
	 */
	public int maxStack()
	{
		return this.stack.storageSize();
	}
	
	/**
	 * Returns the number of variable which are available.
	 *
	 * @return The number of variables which are recorded.
	 * @since 2017/08/13
	 */
	public int numVariables()
	{
		return maxLocals() + maxStack();
	}
	
	/**
	 * Returns the stack tread.
	 *
	 * @return The stack tread.
	 * @since 2017/09/02
	 */
	public VariableTread stack()
	{
		return this.stack;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public String toString()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("{locals= ");
		sb.append(this.locals);
		sb.append(", stack=");
		sb.append(this.stack);
		sb.append("}");
		return sb.toString();
	}
}

