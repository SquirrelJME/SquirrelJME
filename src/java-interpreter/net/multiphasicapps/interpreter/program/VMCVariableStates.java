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
 * @since 2016/03/30
 */
public class VMCVariableStates
	extends AbstractList<VMCVariableState>
{
	/** The owning program. */
	protected final VMCProgram program;
	
	/**
	 * Initializes the variable states.
	 *
	 * @param __prg The program which owns this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	VMCVariableStates(VMCProgram __prg)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
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

