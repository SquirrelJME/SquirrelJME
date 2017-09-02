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
 * This represents a tread of variables which appear within variable states.
 *
 * @since 2017/09/02
 */
public final class VariableTread
{
	/** Is this treated as a stack? */
	protected final boolean isstack;
	
	/** The variable types. */
	private final JavaType[] _types;
	
	/** Variable cache. */
	private final Reference<Variable>[] _vars;
	
	/** Typed variable cache (cleared when type changes occur). */
	private final Reference<TypedVariable>[] _tvars;
	
	/** The top of the stack. */
	private volatile int _top;
	
	/**
	 * Initializes the variable tread.
	 *
	 * @param __n The number of entries to use.
	 * @param __s Is this treated as a stack?
	 * @throws IllegalArgumentException If the number of entries is negative.
	 */
	public VariableTread(int __n, boolean __s)
		throws IllegalArgumentException
	{
		// {@squirreljme.error JI2d Cannot initialize a tread with a negative
		// size.}
		if (__n < 0)
			throw new IllegalArgumentException("JI2d");
		
		// Initialize
		this._types = new JavaType[__n];
		this._vars = __makeVariables(__n);
		this._tvars = __makeTypedVariables(__n);
		this.isstack = __s;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the number of entries which are permitted to be active at
	 * one time.
	 *
	 * @return The active count of the entries in the tread.
	 * @since 2017/09/02
	 */
	public final int count()
	{
		if (this.isstack)
			return this._top;
		return this._types.length;
	}
	
	/**
	 * Obtains the given typed variable.
	 *
	 * @param __i The index of the typed variable to get.
	 * @return The typed variable at the given index.
	 * @throws JITException If the variable is not within bounds.
	 * @since 2017/08/13
	 */
	public final TypedVariable getTypedVariable(int __i)
		throws JITException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Obtains the given variable.
	 *
	 * @param __i The index of the variable to get.
	 * @return The variable at the given index.
	 * @throws JITException If the variable is not within bounds.
	 * @since 2017/08/13
	 */
	public final Variable getVariable(int __i)
		throws JITException
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the maximum number of entries which can fit in this tread.
	 *
	 * @return The storage size of this tread.
	 * @since 2017/09/02
	 */
	public final int storageSize()
	{
		return this._types.length;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Sets the variable from the given stack map table state.
	 *
	 * @param __smt The stack map table state.
	 * @throws JITException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	final void __set(StackMapTableEntry __smt)
		throws JITException, NullPointerException
	{
		// Check
		if (__smt == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
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

