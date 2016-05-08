// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.codeparse;

import net.multiphasicapps.narf.classinterface.NCIByteBuffer;

/**
 * This calculates the position of all operations.
 *
 * @since 2016/05/08
 */
class __OpPositions__
{
	/** The code buffer. */
	protected final NCIByteBuffer buffer;
	
	/**
	 * Initializes the position calculator.
	 *
	 * @param __bb The buffer to determine positions for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/08
	 */
	__OpPositions__(NCIByteBuffer __bb)
		throws NullPointerException
	{
		// Check
		if (__bb == null)
			throw new NullPointerException("NARG");
		
		// Set
		buffer = __bb;
	}
	
	/**
	 * Returns the positions of all operations.
	 *
	 * @return The operation positions.
	 * @since 2016/05/08
	 */
	public int[] get()
	{
		// Setup initial buffer which matches the number of bytes
		NCIByteBuffer buffer = this.buffer;
		int len = buffer.length();
		int[] build = new int[len];
		
		//
		
		throw new Error("TODO");
	}
}

