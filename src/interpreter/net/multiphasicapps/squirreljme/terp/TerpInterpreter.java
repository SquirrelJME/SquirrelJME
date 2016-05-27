// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

/**
 * This is the base class for interpretation engines.
 *
 * @since 2016/05/12
 */
public abstract class TerpInterpreter
{
	/** The thread of execution. */
	protected final TerpThread thread;
	
	/** The core manager. */
	protected final TerpCore core;
	
	/**
	 * Initializes the base interpreter.
	 *
	 * @param __t The thread of execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/12
	 */
	public TerpInterpreter(TerpThread __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.thread = __t;
		this.core = __t.core();
	}
	
	/**
	 * Performs interpretation as required.
	 *
	 * @param __args The arguments to the method.
	 * @return The return value of the call.
	 * @since 2016/05/12
	 */
	public abstract Object interpret(Object... __args);
}

