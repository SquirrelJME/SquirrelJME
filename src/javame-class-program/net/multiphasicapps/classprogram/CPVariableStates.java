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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.List;

/**
 * This represents the state of multiple variables within the program.
 *
 * Compared to before, the stack and local variables are combined into a single
 * state for usage simplicity.
 *
 * Some states may be implied based on previous operations.
 *
 * @since 2016/03/30
 */
public class CPVariableStates
	extends AbstractList<CPVariableState>
{
	/** Lock. */
	final Object lock;	
	
	/** The owning program. */
	protected final CPProgram program;
	
	/** The address of the operation this carries a state for. */
	protected final int address;
	
	/** Is this used as output? */
	protected final boolean isoutput;
	
	/** Is this the entry state? */
	final boolean _isentrystate;
	
	/** Explicit states, for entry usage. */
	private final CPVariableState[] _explicit;
	
	/** Has this state been computed properly? */
	volatile boolean _gotcomputed;
	
	/** The position of the top of the stack. */
	private volatile int _stacktop =
		Integer.MIN_VALUE;
	
	/** Reference array of states. */
	private volatile Reference<CPVariableState>[] _staterefs;
	
	/** Locked in states. */
	private volatile CPVariableState[] _locked;
	
	/**
	 * Initializes the variable states.
	 *
	 * @param __prg The program which owns this.
	 * @param __addr The addressed operation which this defines a state for.
	 * @param __io If {@code true} then this represents an output state which
	 * is used as the input.
	 * @throws NullPointerException If no program was specified.
	 * @since 2016/03/30
	 */
	CPVariableStates(CPProgram __prg, int __addr, boolean __io)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
		lock = program.lock;
		address = __addr;
		isoutput = __io;
		_isentrystate = (address == 0 && !isoutput);
		
		// If this is the net
		if (_isentrystate)
		{
			// Setup explicit set
			int n = size();
			CPVariableState[] xpls = new CPVariableState[n];
			_explicit = xpls;
			
			// Create values
			for (int i = 0; i < n; i++)
				xpls[i] = new CPVariableState(this, i);
		}
		
		// No explicit states
		else
			_explicit = null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public CPVariableState get(int __i)
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// Lock
		synchronized (lock)
		{
			// Explicits?
			CPVariableState[] xpls = _explicit;
			if (xpls != null)
				return xpls[__i];
			
			// Locked in variable?
			CPVariableState[] locked = _locked;
			if (locked != null)
			{
				CPVariableState rv = locked[__i];
				
				// If locked in, use it
				if (rv != null)
					return rv;
			}
			
			// Get state references
			Reference<CPVariableState>[] srefs = _staterefs;
			if (srefs == null)
				_staterefs = (srefs = __makeRefArray());
			
			// Get reference for this position
			Reference<CPVariableState> ref = srefs[__i];
			CPVariableState rv;
			
			// Needs initialization?
			if (ref == null || null == (rv = ref.get()))
				srefs[__i] = new WeakReference<>(
					(rv = new CPVariableState(this, __i)));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the top of the stack.
	 *
	 * @return The stack top.
	 * @since 2016/04/09
	 */
	public int getStackTop()
	{
		// Lock
		synchronized (lock)
		{
			// If entry state, is the max local set
			if (_isentrystate)
				return program._maxlocals;
			
			// May potentially need calculation
			int rv = _stacktop;
			if (rv >= 0)
				return rv;
			
			// Must be calculated
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public int size()
	{
		return program._maxlocals + program._maxstack;
	}
	
	/**
	 * Locks in this state so that it does not go away.
	 *
	 * @param __vs The state to lock in.
	 * @return {@code this}
	 * @throws IllegalStateException If the state to be locked in is not owned
	 * by this state.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/09
	 */
	CPVariableStates __lockIn(CPVariableState __vs)
		throws IllegalStateException, NullPointerException
	{
		// Check
		if (__vs == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error CP0r Attempt to add a state which belongs to
		// another set of states.}
		if (this != __vs.owner())
			throw new IllegalStateException("CP0r");
		
		// Lock
		synchronized (lock)
		{
			// Get locked states
			CPVariableState[] locked = _locked;
			if (locked == null)
				_locked = (locked = new CPVariableState[size()]);
			
			// Store it
			locked[__vs.index()] = __vs;
		}
		
		// Self
		return this;
	}
	
	/**
	 * Pushes a new variable onto the stack.
	 *
	 * @return The newly pushed variable.
	 * @throws CPProgramException If the stack overflows.
	 * @since 2016/04/09
	 */
	CPVariableState __push()
		throws CPProgramException
	{
		// Lock
		synchronized (lock)
		{
			// {@squirreljme.error CP0q Cannot push to the stack because it
			// would overflow. (The next position; The top of the stack)}
			int now = getStackTop();
			int next = now + 1;
			int max;
			if (next >= (max = size()))
				throw new CPProgramException(String.format("CP0q %d %d",
					next, max));
			
			// Set the new top and return the current top
			_stacktop = next;
			return get(now);
		}
	}
	
	/**
	 * Makes a new reference array.
	 *
	 * @return The newly created reference array.
	 * @since 2016/04/09
	 */
	@SuppressWarnings({"unchecked"})
	private Reference<CPVariableState>[] __makeRefArray()
	{
		return (Reference<CPVariableState>[])((Object)new Reference[size()]);
	}
}

