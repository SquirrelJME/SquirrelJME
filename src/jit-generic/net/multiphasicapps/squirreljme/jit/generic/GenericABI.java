// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This contains the information on a given ABI such as which registers are
 * used and the purpose of their usage.
 *
 * @since 2016/09/01
 */
public final class GenericABI
{
	/**
	 * Initializes the ABI from the given builder.
	 *
	 * @param __b The builder creating this.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/01
	 */
	GenericABI(GenericABIBuilder __b)
		throws NullPointerException
	{
		// Check
		if (__b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

