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
	/** The number of local variables. */
	protected final int maxlocals;
	
	/** The number of stack variables. */
	protected final int maxstack;
	
	/** The number of variables. */
	protected final int numvariables;
	
	/** The local variable types. */
	private final JavaType[] _locals;
	
	/** Local variable cache. */
	private final Reference<Variable>[] _vlocals;
	
	/** Local typed variable cache (cleared when type changes occur). */
	private final Reference<TypedVariable>[] _tlocals;
	
	/** The stack variable types. */
	private final JavaType[] _stack;
	
	/** Stack variable cache. */
	private final Reference<Variable>[] _vstack;
	
	/** Stack typed variable cache (cleared when type changes occur). */
	private final Reference<TypedVariable>[] _tstack;
	
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
		JavaType[] locals;
		this._locals = (locals = new JavaType[__ml]);
		this._vlocals = __makeVariables(__ml);
		this._tlocals = __makeTypedVariables(__ml);
		this._stack = new JavaType[__ms];
		this._vstack = __makeVariables(__ms);
		this._tstack = __makeTypedVariables(__ms);
		
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
		// {@squirreljme.error JI23 The specified local variable is not within
		// the bounds of the local variable set. (The local variable index)}
		if (__i < 0 || __i >= this.maxlocals)
			throw new JITException(String.format("JI23 %d", __i));
		
		// Cache if needed
		Reference<Variable>[] vlocals = this._vlocals;
		Reference<Variable> ref = vlocals[__i];
		Variable rv;
		if (ref == null || null == (rv = ref.get()))
			vlocals[__i] = new WeakReference<>((rv = new Variable(
				VariableLocation.LOCAL, __i)));
		
		return rv;
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
		// {@squirreljme.error JI24 The specified stack variable is not within
		// the bounds of the stack variable set. (The stack variable index)}
		if (__i < 0 || __i >= this._top)
			throw new JITException(String.format("JI24 %d", __i));
		
		// Cache if needed
		Reference<Variable>[] vstack = this._vstack;
		Reference<Variable> ref = vstack[__i];
		Variable rv;
		if (ref == null || null == (rv = ref.get()))
			vstack[__i] = new WeakReference<>((rv = new Variable(
				VariableLocation.STACK, __i)));
		
		return rv;
	}
	
	/**
	 * Obtains the given local typed variable.
	 *
	 * @param __i The index of the typed variable to get.
	 * @return The typed variable at the given index.
	 * @throws JITException If the variable is not within bounds.
	 * @since 2017/08/13
	 */
	public TypedVariable getTypedLocal(int __i)
		throws JITException
	{
		// Get variable first
		Variable var = getLocal(__i);
		
		// Cache if needed
		Reference<TypedVariable>[] tlocal = this._tlocals;
		Reference<TypedVariable> ref = tlocal[__i];
		TypedVariable rv;
		if (ref == null || null == (rv = ref.get()))
			tlocal[__i] = new WeakReference<>((rv = new TypedVariable(
				this._locals[__i], var)));
		
		return rv;
	}
	
	/**
	 * Obtains the given stack typed variable.
	 *
	 * @param __i The index of the typed variable to get.
	 * @return The typed variable at the given index.
	 * @throws JITException If the variable is not within bounds.
	 * @since 2017/08/13
	 */
	public TypedVariable getTypedStack(int __i)
		throws JITException
	{
		// Get variable first
		Variable var = getStack(__i);
		
		// Cache if needed
		Reference<TypedVariable>[] tstack = this._tstack;
		Reference<TypedVariable> ref = tstack[__i];
		TypedVariable rv;
		if (ref == null || null == (rv = ref.get()))
			tstack[__i] = new WeakReference<>((rv = new TypedVariable(
				this._stack[__i], var)));
		
		return rv;
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
	
	/**
	 * Creates an array of typed variable references.
	 *
	 * @param __n The number of elements in the array.
	 * @return An array of references to typed variables.
	 * @since 2017/08/13
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<TypedVariable>[] __makeTypedVariables(int __n)
	{
		return (Reference<TypedVariable>[])((Object)new Reference[__n]);
	}
	
	/**
	 * Creates an array of variable references.
	 *
	 * @param __n The number of elements in the array.
	 * @return An array of references to variables.
	 * @since 2017/08/13
	 */
	@SuppressWarnings({"unchecked"})
	private static Reference<Variable>[] __makeVariables(int __n)
	{
		return (Reference<Variable>[])((Object)new Reference[__n]);
	}
}

