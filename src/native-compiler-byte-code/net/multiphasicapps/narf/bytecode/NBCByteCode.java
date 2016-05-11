// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.bytecode;

import net.multiphasicapps.narf.classinterface.NCIMethod;

/**
 * This class contains the main representation of Java byte code.
 *
 * @since 2016/05/11
 */
public class NBCByteCode
{
	/** The containing method. */
	protected final NCIMethod method;
	
	/**
	 * Initilizes the byte code representation.
	 *
	 * @param __m The containing method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/11
	 */
	public NBCByteCode(NCIMethod __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		method = __m;
		
		throw new Error("TODO");
	}
}

