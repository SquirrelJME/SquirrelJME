// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This represents the shared constant pool which all basic namespaces use to
 * store specific constant values.
 *
 * @since 2016/09/11
 */
public class BasicConstantPool
{
	/**
	 * Initializes the basic pool.
	 *
	 * @since 2016/09/11
	 */
	BasicConstantPool()
	{
	}
	
	/**
	 * Adds a string to the constant pool.
	 *
	 * @param __s The string to add.
	 * @return The constant entry for the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	public BasicConstantEntry<String> addString(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

