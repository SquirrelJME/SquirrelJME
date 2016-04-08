// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This class contains the loaders and such of workers which perform
 * computations and such.
 *
 * @since 2016/04/08
 */
class __VMWorkers__
{
	/**
	 * Initializes the worker dispatcher.
	 *
	 * @since 2016/04/08
	 */
	__VMWorkers__()
	{
	}
	
	/**
	 * Looks up the worker for the given instruction.
	 *
	 * @param __code The instruction to get the worker for.
	 * @return The worker for the instruction, or {@code null} if invalid.
	 * @since 2016/04/08
	 */
	__Worker__ __lookup(int __code)
	{
		throw new Error("TODO");
	}
	
	/**
	 * This is the actual worker base.
	 *
	 * @since 2016/04/08
	 */
	static abstract class __Worker__
	{
	}
}

