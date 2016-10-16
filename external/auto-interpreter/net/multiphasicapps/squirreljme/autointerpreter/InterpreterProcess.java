// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.autointerpreter;

import java.util.Map;

/**
 * This represents a process within the interpreter.
 *
 * @since 2016/10/11
 */
public class InterpreterProcess
{
	/** The owning engine. */
	protected final ExecutionEngine engine;
	
	/**
	 * Initializes the interpreter process.
	 *
	 * @param __e The engine used for execution.
	 * @param __prop The system properties to use.
	 * @param __cp The run-time namespace.
	 * @throws IllegalArgumentException If the class path is empty or if the
	 * main class is not a midlet.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/11
	 */
	public InterpreterProcess(ExecutionEngine __e, Map<String, String> __prop,
		RuntimeNamespace... __cp)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__e == null || __prop == null || __cp == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EO03 The process has no classpath.}
		if (__cp.length <= 0)
			throw new IllegalArgumentException("EO03");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the owning execution engine.
	 *
	 * @return The execution engine owning this.
	 * @since 2016/10/11
	 */
	public final ExecutionEngine engine()
	{
		return this.engine;
	}
}

