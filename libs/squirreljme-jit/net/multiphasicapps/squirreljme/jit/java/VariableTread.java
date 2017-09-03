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
	/** The location of this tread. */
	protected final VariableLocation location;
	
	/** Is this treated as a stack? */
	protected final boolean isstack;
	
	/** The variable types. */
	private final JavaType[] _types;
	
	/** Initialization keys for entries. */
	private final InitializationKey[] _initkeys;
	
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
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	public VariableTread(VariableLocation __l, int __n, boolean __s)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI2d Cannot initialize a tread with a negative
		// size.}
		if (__n < 0)
			throw new IllegalArgumentException("JI2d");
		
		// Initialize
		this.location = __l;
		this._types = new JavaType[__n];
		this._initkeys = new InitializationKey[__n];
		this._vars = __makeVariables(__n);
		this._tvars = __makeTypedVariables(__n); 
		this.isstack = __s;
		this._top = (__s ? 0 : __n);
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
		// {@squirreljme.error JI2e The specified index is not within bounds
		// of the tread. (The index of the variable)}
		if (__i < 0 || __i >= this._top)
			throw new JITException(String.format("JI2e %d", __i));
		
		Reference<TypedVariable>[] tvars = this._tvars;
		Reference<TypedVariable> ref = tvars[__i];
		TypedVariable rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			tvars[__i] = new WeakReference<>((rv = new TypedVariable(
				this._types[__i], getVariable(__i), this._initkeys[__i])));
		
		return rv;
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
		// {@squirreljme.error JI2f The specified index is not within bounds
		// of the tread. (The index of the variable)}
		if (__i < 0 || __i >= this._top)
			throw new JITException(String.format("JI2f %d", __i));
		
		Reference<Variable>[] vars = this._vars;
		Reference<Variable> ref = vars[__i];
		Variable rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			vars[__i] = new WeakReference<>((rv =
				new Variable(this.location, __i)));
		
		return rv;
	}
	
	/**
	 * Pushes the specified variable to the stack.
	 *
	 * @param __v The variable to push.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JITException If the stack overflows or if the type is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	public final void push(TypedVariable __v)
		throws JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI2h Cannot push a variable to a non-stack
		// tread.}
		if (!this.isstack)
			throw new IllegalStateException("JI2h");
		
		// {@squirreljme.error JI2i Cannot push the given variable to
		// the stack because it has no type. (The variable)}
		if (!__v.hasType())
			throw new JITException(String.format("JI2i %s", __v));
		
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
	 * @throws IndexOutOfBoundsException If the index it out of bounds.
	 * @throws JITException If it could not be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	final void __set(int __i, StackMapTableEntry __smt)
		throws IndexOutOfBoundsException, JITException, NullPointerException
	{
		// Check
		if (__smt == null)
			throw new NullPointerException("NARG");
		
		JavaType jt = __smt.type();
		this._types[__i] = jt;
		this._initkeys[__i] = (jt != null ? (__smt.isInitialized() ? null :
			new InitializationKey((-__i) - 1)) : null);
		this._tvars[__i] = null;
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

