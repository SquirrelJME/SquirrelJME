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

/**
 * This represents a thread which runs inside of the interpreter.
 *
 * @since 2016/04/21
 */
public class NIThread
{
	/** The owning core. */
	protected final NICore core;
	
	/** The thread this executes from. */
	protected final Thread thread;
	
	/** Special thread? */
	protected final boolean isspecial;
	
	/**
	 * Initializes the interpreter thread which maps a thread to the specified
	 * thread. This is used for the method initalization since it needs a
	 * thread to exist.
	 *
	 * @param __c The owning core.
	 * @param __xt The pre-existing thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/21
	 */
	public NIThread(NICore __c, Thread __xt)
		throws NullPointerException
	{
		// Check
		if (__c == null || __xt == null)
			throw new NullPointerException("NARG");
		
		// Set
		core = __c;
		thread = __xt;
		isspecial = true;
	}
	
	/**
	 * Initializes a new thread which executes the given method with the
	 * specified argument list.
	 *
	 * @param __c The owning virtual machine.
	 * @param __m The method to execute.
	 * @param __a The arguments to the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NIThread(NICore __c, NIMethod __m, Object... __args)
		throws NullPointerException
	{
		// Check
		if (__c == null || __m == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

