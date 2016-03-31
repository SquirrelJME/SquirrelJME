// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.program;

import java.util.AbstractList;

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
public class VMCVariableStates
	extends AbstractList<VMCVariableState>
{
	/** Lock. */
	final Object lock;	
	
	/** The owning program. */
	protected final VMCProgram program;
	
	/** The address of the operation this carries a state for. */
	protected final int address;
	
	/** Is this used as output? */
	protected final boolean isoutput;
	
	/** Is this the entry state? */
	final boolean _isentrystate;
	
	/** Explicit states, for entry usage. */
	private final VMCVariableState[] _explicit;
	
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
	VMCVariableStates(VMCProgram __prg, int __addr, boolean __io)
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
			VMCVariableState[] xpls = new VMCVariableState[n];
			_explicit = xpls;
			
			// Create values
			for (int i = 0; i < n; i++)
				xpls[i] = new VMCVariableState(this, i);
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
	public VMCVariableState get(int __i)
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// Lock
		synchronized (lock)
		{
			// Explicits?
			VMCVariableState[] xpls = _explicit;
			if (xpls != null)
				return xpls[__i];
			
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
}

