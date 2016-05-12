// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.interpreter;

import net.multiphasicapps.narf.bytecode.NBCByteCode;

/**
 * This is the pure interpreter which takes byte code directly for execution.
 *
 * @since 2016/05/12
 */
public class NIInterpreterPure
	extends NIInterpreter
{
	/** The code to be interpreted. */
	protected final NBCByteCode program;
	
	/**
	 * Initializes the interpreter which uses the direct byte code.
	 *
	 * @param __t The thread of execution.
	 * @param __p The byte code to interpret.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public NIInterpreterPure(NIThread __t, NBCByteCode __p)
		throws NullPointerException
	{
		super(__t);
		
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.program = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/12
	 */
	@Override
	public Object interpret(Object... __args)
	{
		throw new Error("TODO");
	}
}

