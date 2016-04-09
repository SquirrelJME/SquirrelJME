// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This represents the state of a single variable, it may be explicitely
 * generated or implicitely generated.
 *
 * In virtually every case (except for the first instruction) the state of
 * operations are always implicit.
 *
 * @since 2016/03/30
 */
public class CPVariableState
{
	/** Lock. */
	final Object lock;
	
	/** Owning states. */
	protected final CPVariableStates states;
	
	/** The index of this state. */
	protected final int index;
	
	/** Explicit state set? */
	private volatile CPVariableType _explicit;
	
	/** Computed Type? */
	private volatile CPVariableType _comptype;
	
	/** Is the computed value set? */
	private volatile boolean _isvalueset;
	
	/** The computed value identifier. */
	private volatile long _computedval;
	
	/** Does this value exist in the parent state? */
	private volatile boolean _exists;
	
	/**
	 * Initializes the variable state.
	 *
	 * @param __s The owning states.
	 * @param __dx The variable index.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	CPVariableState(CPVariableStates __s, int __dx)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set
		states = __s;
		lock = states.lock;
		index = __dx;
	}
	
	/**
	 * Returns the type of variable which is used at this position.
	 *
	 * @return The variable type used.
	 * @since 2016/03/31
	 */
	public CPVariableType getType()
	{
		// Lock
		synchronized (lock)
		{
			// If there is an explcit type set then use it
			CPVariableType rv = _explicit;
			if (rv != null)
				return rv;
			
			// Computed type?
			rv = _comptype;
			if (rv != null)
				return rv;
			
			// Derived from a source location
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the unique value identifier for this position.
	 *
	 * @return The current unique value stored here.
	 * @since 2016/04/09
	 */
	public long getValue()
	{
		// If the entry state, this is just the index
		if (states._isentrystate)
			return index;
		
		// Lock
		synchronized (lock)
		{
			// Specified value?
			if (_isvalueset)
				return _computedval;
			
			// Derived from source instructions
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the index of this state.
	 *
	 * @return The state's index.
	 * @since 2016/04/09
	 */
	public int index()
	{
		return index;
	}
	
	/**
	 * Returns the states set which holds this variable state.
	 *
	 * @return The owning states.
	 * @since 2016/04/09
	 */
	public CPVariableStates owner()
	{
		return states;
	}
	
	/**
	 * Calculates the value identifier for the current variable for the given
	 * operation.
	 *
	 * @param __op The operation used for the base address.
	 * @return The value identifier.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	long __newValueAt(CPOp __op)
		throws NullPointerException
	{
		return (((long)__op.address()) << 32L) | (long)index;
	}
	
	/**
	 * Sets the type of the variable stored here.
	 *
	 * @param __vt The type of variable stored here.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is not the entry state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	CPVariableState __setType(CPVariableType __vt)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		// Can only change the entry state
		// {@squirreljme.error CP0f Can only set the type for the entry
		// state of a method.}
		if (!states._isentrystate)
			throw new IllegalStateException("CP0f");
		
		// Lock
		synchronized (lock)
		{
			_explicit = __vt;
		}
		
		// Self
		return this;
	}
	
	/**
	 * This makes it so the state exists in the parent state.
	 *
	 * @return {@code this}.
	 * @since 2016/04/09
	 */
	private CPVariableState __mustExist()
	{
		// Lock
		synchronized (lock)
		{
			// Already exists, does not need placement
			if (_exists)
				return this;
			
			// Lock it in
			states.__lockIn(this);
			
			// Now exists
			_exists = true;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Sets the computed type of this variable.
	 *
	 * @param __vt The type to set.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is the entry state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	CPVariableState __setComputedType(CPVariableType __vt)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CP0p Cannot set the type used for the entry
		// state.}
		if (states._isentrystate)
			throw new IllegalStateException("CP0p");
		
		// Lock
		synchronized (lock)
		{
			_comptype = __vt;
			
			// Must exist in the parent
			return __mustExist();
		}
	}
	
	/**
	 * Sets the computed value identifer of this state which specified which
	 * unique variable this method uses (this makes it SSA).
	 *
	 * @param __vi The value identifier to use.
	 * @return {@code this}.
	 * @throws IllegalStateException If this is the entry state.
	 * @since 2016/04/09
	 */
	CPVariableState __setComputedValue(long __vi)
		throws IllegalStateException
	{
		// {@squirreljme.error CP0o Cannot set the computed value for the
		// entry state.}
		if (states._isentrystate)
			throw new IllegalStateException("CP0o");
		
		// Lock
		synchronized (lock)
		{
			_isvalueset = true;
			_computedval = __vi;
			
			// Must exist in the parent
			return __mustExist();
		}
	}
}

