// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp.rr;

import net.multiphasicapps.squirreljme.ci.CIMethod;
import net.multiphasicapps.squirreljme.terp.InterpreterThread;

/**
 * This is a rerecorded interpreter thread.
 *
 * @since 2016/06/03
 */
public class RRThread
	extends InterpreterThread
{
	/**
	 * Initializes the thread.
	 *
	 * @param __terp The owning interpreter.
	 * @param __ip The owning process.
	 * @param __m The starting method.
	 * @param __args The arguments to the thread.
	 * @since 2016/06/03
	 */
	public RRThread(RRInterpreter __terp,
		RRProcess __ip, CIMethod __m, Object... __args)
	{
		super(__terp, __ip, (__m = __rewriteEntryMethod(__terp, __m)),
			(__args = __rewriteEntryArgs(__terp, __args)));
		
		throw new Error("TODO");
	}
	
	/**
	 * Rewrites the entry method when replaying a stream then writes the
	 * entry method if recording.
	 *
	 * @param __terp The interpreter.
	 * @param __m The original entry method.
	 * @return The rewritten entry method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/03
	 */
	private static final CIMethod __rewriteEntryMethod(RRInterpreter __terp,
		CIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__terp == null || __m == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Rewrites the entry arguments when replaying a stream then writes the
	 * entry arguments if recording.
	 *
	 * @param __terp The interpreter.
	 * @param __args The original entry arguments.
	 * @return The rewritten entry arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/03
	 */
	private static final Object[] __rewriteEntryArgs(RRInterpreter __terp,
		Object... __args)
		throws NullPointerException
	{
		// Check
		if (__terp == null || __args == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

