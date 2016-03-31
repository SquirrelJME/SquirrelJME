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

import net.multiphasicapps.interpreter.JVMRawException;

/**
 * This represents an exception which is handled within a method.
 *
 * The addressing is logical.
 *
 * @since 2016/03/31
 */
public class VMCException
{
	/** The owning program. */
	protected final VMCProgram program;	
	
	/**
	 * This initializes an exception.
	 *
	 * @param __prg The owning program.
	 * @param __rx The raw exception.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/31
	 */
	VMCException(VMCProgram __prg, JVMRawException __rx)
		throws NullPointerException
	{
		// Check
		if (__prg == null || __rx == null)
			throw new NullPointerException("NARG");
		
		// Set
		program = __prg;
		
		throw new Error("TODO");
	}
}

