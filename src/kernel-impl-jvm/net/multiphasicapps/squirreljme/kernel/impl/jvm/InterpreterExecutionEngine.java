// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.impl.jvm;

import net.multiphasicapps.squirreljme.kernel.KernelExecutionEngine;
import net.multiphasicapps.squirreljme.terp.Interpreter;

/**
 * This is the execution engine which wraps the interpreter.
 *
 * @since 2016/05/31
 */
public class InterpreterExecutionEngine
	implements KernelExecutionEngine
{
	/** The interpreter to execute. */
	protected final Interpreter interpreter;
	
	/**
	 * Initializes the execution engine which uses the given interpreter.
	 *
	 * @param __terp The interpreter to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/31
	 */
	public InterpreterExecutionEngine(Interpreter __terp)
		throws NullPointerException
	{
		// Check
		if (__terp == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.interpreter = __terp;
	}
	
	/**
	 * Returns the interpreter that this execution engine uses.
	 *
	 * @return The used interpreter.
	 * @since 2016/05/31
	 */
	public Interpreter interpreter()
	{
		return this.interpreter;
	}
}

