// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

import java.util.Map;
import net.multiphasicapps.squirreljme.jit.VerifiedJITInput;

/**
 * This contains the interpreter which executes Java byte code in a contained
 * environment which is built for SquirrelJME.
 *
 * @since 2017/10/05
 */
public class Interpreter
{
	/** The input for the JIT. */
	protected final VerifiedJITInput input;
	
	/**
	 * Initializes the interpreter to run the given program.
	 *
	 * @param __vji The verified input to use.
	 * @param __props The system properties to use.
	 * @param __boot The MIDlet to enter for execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/05
	 */
	public Interpreter(VerifiedJITInput __vji, Map<String, String> __props,
		String __boot)
		throws NullPointerException
	{
		if (__vji == null || __props == null || __boot == null)
			throw new NullPointerException("NARG");
		
		// Set input
		this.input = __vji;
	}
}

