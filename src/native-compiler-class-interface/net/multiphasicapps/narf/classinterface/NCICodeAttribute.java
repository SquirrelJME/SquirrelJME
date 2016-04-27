// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

/**
 * This represents a code attribute.
 *
 * @since 2016/04/27
 */
public final class NCICodeAttribute
{
	/** The code attribute data. */
	protected final byte[] data;
	
	/**
	 * Initializes the code attribute with the given attribute data.
	 *
	 * @param __data The code attribute data.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/27
	 */
	public NCICodeAttribute(byte... __data)
		throws NullPointerException
	{
		// Check
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Set
		data = __data;
	}
}

