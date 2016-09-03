// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.IOException;
import net.multiphasicapps.io.data.ExtendedDataInputStream;

/**
 * This is used to determine which instruction addresses in the byte code are
 * jumped to, the benefit of this is that the target JIT does not have to
 * remember state for instructions that will never be jumped to.
 *
 * @since 2016/09/03
 */
class __JumpTargetCalc__
{
	/**
	 * Calculates the jump targets.
	 *
	 * @param __dis The source byte codes.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/03
	 */
	__JumpTargetCalc__(ExtendedDataInputStream __dis)
		throws IOException, NullPointerException
	{
		// Check
		if (__dis == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the calculated jump targets.
	 *
	 * @return The jump targets in the method.
	 * @since 2016/09/03
	 */
	public int[] targets()
	{
		throw new Error("TODO");
	}
}

