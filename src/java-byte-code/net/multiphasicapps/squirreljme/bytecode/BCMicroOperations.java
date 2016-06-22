// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bytecode;

/**
 * This class is used to contain multiple micro-operations which perform work.
 *
 * @since 2016/06/22
 */
public class BCMicroOperations
{
	/**
	 * Initializes the micro operation data.
	 *
	 * @param __uops The micro operations to perform.
	 * @throws BCException If a micro-operation is not valid.
	 * @since 2016/06/22
	 */
	public BCMicroOperations(int... __uops)
		throws BCException
	{
		// Must exist
		if (__uops == null)
			__uops = new int[0];
		
		throw new Error("TODO");
	}
}

