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
import net.multiphasicapps.interpreter.JVMVariableType;

/**
 * This represents the state of multiple variables within the program.
 *
 * Compared to before, the stack and local variables are combined into a single
 * state for usage simplicity.
 *
 * @since 2016/03/30
 */
public class VMCVariableStates
	extends AbstractList<VMCVariableState>
{
	/** The owning program. */
	protected final VMCProgram program;
	
	/** The address of the operation this carries a state for. */
	protected final int address;
	
	/** Is this used as output? */
	protected final boolean isoutput;
	
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
		address = __addr;
		isoutput = __io;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public VMCVariableState get(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public int size()
	{
		throw new Error("TODO");
	}
}

