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

import net.multiphasicapps.narf.library.NLClass;

/**
 * This represents a class which is loaded by the interpreter.
 *
 * @since 2016/04/21
 */
public class InterpreterClass
{
	/** The interpreter core. */
	protected final InterpreterCore core;
	
	/** The based class (if {@code null} is a virtual class). */
	protected final NLClass base;
	
	/**
	 * Initializes an interpreted class.
	 *
	 * @param __core The core.
	 * @param __base The class to base off.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public InterpreterClass(InterpreterCore __core, NLClass __base)
		throws NullPointerException
	{
		// Check
		if (__core == null || __base == null)
			throw new NullPointerException("NARG");
		
		// Set
		core = __core;
		base = __base;
	}
}

