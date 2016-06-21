// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classwriter;

/**
 * This represents byte code that may be output when a class is written to.
 *
 * @since 2016/06/21
 */
public class OutputCode
{
	/** The owning method. */
	protected final OutputMethod method;
	
	/**
	 * Initializes the code output.
	 *
	 * @param __m The method that uses the given code.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/21
	 */
	OutputCode(OutputMethod __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.method = __m;
	}
}

