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

/**
 * This is an execution engine that generally is fast because it uses native
 * threads and does not have any forced loss of speed.
 *
 * @since 2016/10/11
 */
public class FastExecutionEngine
	extends ExecutionEngine
{
	/**
	 * {@inheritDoc}
	 * @since 2016/10/11
	 */
	@Override
	public void run()
	{
		// Wait until the launcher process stops executing
		for (;;)
			throw new Error("TODO");
	}
}

