// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath;

import net.multiphasicapps.squirreljme.ci.CIClass;

/**
 * This verifies that the specified class is properly laid out in the structure
 * required by the virtual machine.
 *
 * @since 2016/05/28
 */
class __Verifier__
{
	/** The source for classes. */
	protected final ClassPath classpath;
	
	/** The class to verify. */
	protected final CIClass verify;
	
	/**
	 * This performs the 
	 *
	 * @param __cp The class path to use when finding classes to verify.
	 * @param __cl The class to be verified.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/28
	 */
	__Verifier__(ClassPath __cp, CIClass __cl)
		throws NullPointerException
	{
		// Check
		if (__cp == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.classpath = __cp;
		this.verify = __cl;
		
		throw new Error("TODO");
	}
}

