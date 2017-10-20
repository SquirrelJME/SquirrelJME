// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bvm;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.cff.JavaType;

/**
 * This contains a single tread of variables and may optionally be used as
 * a stack.
 *
 * If a tread is usable as a stack, it also additionally supports the push and
 * pop operations.
 *
 * @since 2017/10/17
 */
public final class VariableTread
{
	/** The number of variables in the tread. */
	protected final int size;
	
	/** Is this a stack? */
	protected final boolean isstack;
	
	/** Variables. */
	private final Variable[] _vars;
	
	/** Owning variables. */
	private final Reference<Variables> _varsref;
	
	/** The top of the stack. */
	private volatile int _top;
	
	/**
	 * Initializes the variable tread.
	 *
	 * @param __vr Reference to the owning variables.
	 * @param __n The number of variables to store in this tread.
	 * @param __stack Is this tread to be treated as a stack?
	 * @throws IllegalArgumentException If the number of variables to store
	 * is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/17
	 */
	VariableTread(Reference<Variables> __vr, int __n, boolean __stack)
		throws IllegalArgumentException, NullPointerException
	{
		if (__vr == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error JI3y Negative number of variable slots in the
		// tread specified.}
		if (__n < 0)
			throw new IllegalArgumentException("JI3y");
		
		// Set
		this._varsref = __vr;
		this.size = __n;
		this.isstack = __stack;
		this._top = (__stack ? 0 : -1);
		
		// Setup variables
		Reference<VariableTread> selfref = new WeakReference<>(this);
		Variable[] vars = new Variable[__n];
		for (int i = 0; i < __n; i++)
			vars[i] = new Variable(selfref, i);
		this._vars = vars;
	}
	
	/**
	 * Clears the value at the given index.
	 *
	 * @param __i The index of the value to set.
	 * @return The value which at the given location.
	 * @throws ParserException If the index is out of bounds.
	 * @since 2017/10/20
	 */
	public DataValue clearValue(int __i)
		throws ParserException
	{
		return __set(__i, null, null);
	}
	
	/**
	 * Returns the current limit of the tread which may be the number of
	 * variables or the size of the stack if this is the stack.
	 *
	 * @return The limit of the tread, the number of variables it can store
	 * at the current time.
	 * @since 2017/10/20
	 */
	public int limit()
	{
		return (this.isstack ? this._top : this.size);
	}
	
	/**
	 * Sets the given slot to the specified value.
	 *
	 * @param __i The index of the value to set.
	 * @param __t The type of value to set there.
	 * @return The resulting data value from the set.
	 * @throws NullPointerException On null arguments.
	 * @throws ParserException If the value to set is not valid or the index
	 * is out of bounds.
	 * @since 2017/10/20
	 */
	public DataValue setNewValue(int __i, JavaType __t)
		throws NullPointerException, ParserException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return __set(__i, __t, null);
	}
	
	/**
	 * Sets the given slot to the specified value which is not initialized.
	 *
	 * @param __i The index of the value to set.
	 * @param __t The type of value to set there.
	 * @param __k The initialization key which determines which variables will
	 * become initialized when the any one of those variables are initialized.
	 * @return The resulting data value from the set.
	 * @throws NullPointerException On null arguments.
	 * @throws ParserException If the value to set is not valid or the index
	 * is out of bounds.
	 * @since 2017/10/20
	 */
	public DataValue setNewValue(int __i, JavaType __t, InitializationKey __k)
		throws NullPointerException, ParserException
	{
		if (__t == null || __k == null)
			throw new NullPointerException("NARG");
		
		return __set(__i, __t, __k);
	}
	
	/**
	 * Returns the size of the tread.
	 *
	 * @return The tread size, the maximum number of variables it can store.
	 * @since 2017/10/20
	 */
	public int size()
	{
		return this.size;
	}
	
	/**
	 * Internally sets the given slot to the given value or potentially
	 * clears it.
	 *
	 * @param __i The index of the value to set.
	 * @param __t The type of value to set there.
	 * @param __k The initialization key which determines which variables will
	 * become initialized when the any one of those variables are initialized.
	 * @return The resulting data value from the set or clear operation.
	 * @throws ParserException If the value to set is not valid or the index
	 * is out of bounds.
	 * @since 2017/10/20
	 */
	DataValue __set(int __i, JavaType __t, InitializationKey __k)
		throws ParserException
	{
		// {@squirreljme.error JI3z Cannot set the given index because it
		// exceeds the limit of the tread. (The index to set; The limit of the
		// tread)}
		int limit = limit();
		if (__i < 0 || __i >= limit)
			throw new ParserException(String.format("JI3z %d %d", __i, limit));
		
		throw new todo.TODO();
	}
}

