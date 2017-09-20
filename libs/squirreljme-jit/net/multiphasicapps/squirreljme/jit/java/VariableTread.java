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
	 * Initializes all instances of the given variable.
	 *
	 * @param __ik The variable to initialize.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/20
	 */
	public void initializeVariable(InitializationKey __ik)
		throws NullPointerException
	{
		// Check
		if (__ik == null)
			throw new NullPointerException("NARG");
		
		// Go through all variables and match, just clearing to null means
		// that they become initialized
		InitializationKey[] initkeys = this._initkeys;
		Reference<TypedVariable>[] tvars = this._tvars;
		for (int i = 0, n = this._top; i < n; i++)
			if (__ik.equals(initkeys[i]))
			{
				// Clear both and the typed cache because that has
				// initialization info
				initkeys[i] = null;
				tvars[i] = null;
				
				// Debug
				System.err.printf("DEBUG -- %s becomes initialized%n",
					getTypedVariable(n));
			}
	}
	
	/**
	 * Pops the variable which is at the top of the stack.
	 *
	 * @return The popped variable.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JITException If the stack overflows.
	 * @since 2017/09/16
	 */
	public final TypedVariable pop()
		throws IllegalStateException, JITException
	{
		// {@squirreljme.error JI2m Cannot pop a variable from a non-stack
		// tread.}
		if (!this.isstack)
			throw new IllegalStateException("JI2m");
		
		// {@squirreljme.error JI2n Stack underflow.}
		int top = this._top;
		if (top <= 0)
			throw new JITException("JI2n");
			
		JavaType[] types = this._types;
		InitializationKey[] initkeys = this._initkeys;
		Reference<Variable>[] vars = this._vars;
		Reference<TypedVariable>[] tvars = this._tvars;
		
		// Do not return the top of a type
		int pivot = top - 1;
		JavaType jt = types[pivot];
		if (jt.isTop())
			jt = types[--pivot];
		
		// The work is already done in another method
		TypedVariable rv = getTypedVariable(pivot);
		
		// Clear old stack information
		this._top = pivot;
		types[pivot] = null;
		initkeys[pivot] = null;
		tvars[pivot] = null;
		
		// Debug
		System.err.printf("DEBUG -- Pop %s%n", rv);
		
		return rv;
	}
	
	/**
	 * Pushes the specified variable to the stack.
	 *
	 * @param __v The variable to push.
	 * @return The variable of the pushed stack slot.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JITException If the stack overflows or if the type is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/02
	 */
	public final Variable push(TypedVariable __v)
		throws IllegalStateException, JITException, NullPointerException
	{
		// Check
		if (__v == null)
			throw new NullPointerException("NARG");
			
		// {@squirreljme.error JI2i Cannot push the given variable to
		// the stack because it has no type. (The variable)}
		if (!__v.hasType())
			throw new JITException(String.format("JI2i %s", __v));
		
		return push(__v.type(), __v.initializationKey());
	}
	
	/**
	 * Pushes the specified type and initialization key to the stack.
	 *
	 * @param __t The type to push.
	 * @param __i An optional initialization key for the given object.
	 * @return The variable of the pushed stack slot.
	 * @throws IllegalArgumentException If an initialization key was specified
	 * for a non-object.
	 * @throws IllegalStateException If this is not a stack.
	 * @throws JITException If the stack overflows or if the type is not
	 * valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/09/03
	 */
	public final Variable push(JavaType __t, InitializationKey __i)
		throws IllegalArgumentException, IllegalStateException, JITException,
			NullPointerException
	{
		// Check
		if (__t == null || __i == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI2h Cannot push a variable to a non-stack
		// tread.}
		if (!this.isstack)
			throw new IllegalStateException("JI2h");
		
		// {@squirreljme.error JI2k Non-objects cannot have an initialization
		// key specified for them. (The type; The initialization key)}
		if (__i != null && !__t.isObject())
			throw new IllegalArgumentException(String.format(
				"JI2k %s %s", __t, __t));
		
		JavaType[] types = this._types;
		InitializationKey[] initkeys = this._initkeys;
		Reference<Variable>[] vars = this._vars;
		Reference<TypedVariable>[] tvars = this._tvars;
		
		// {@squirreljme.error JI2j The stack would overflow pushing the
		// specified type. (The type being pushed;
		// The stack limit; The stack size after pushing)}
		int top = this._top,
			w = __t.width(),
			next = top + w,
			limit = types.length;
		if (next > limit)
			throw new JITException(String.format("JI2j %s %d %d", __t, limit,
				next));
		
		// Fill base
		types[top] = __t;
		initkeys[top] = __i;
		tvars[top] = null;
		
		// Fill top of long/double?
		if (__t.isWide())
		{
			int wp = top + 1;
			types[wp] = __t.topType();
			initkeys[wp] = null;
			tvars[wp] = null;
		}
		
		// Set new top
		this._top = next;
		
		// Refer to the top as a variable that can be used for writing
		return new Variable(this.location, top);
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

