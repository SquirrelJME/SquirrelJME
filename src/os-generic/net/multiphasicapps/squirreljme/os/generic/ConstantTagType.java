// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.os.generic;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;
import net.multiphasicapps.squirreljme.jit.base.JITException;

/**
 * This represents the type of tag which is stored in a namespace.
 *
 * @since 2016/08/18
 */
public enum ConstantTagType
{
	/** Nothing. */
	NULL(null),
	
	/** The name of a class. */
	CLASS(ClassNameSymbol.class),
	
	/** End. */
	;
	
	/** Internal values. */
	private static final ConstantTagType[] _VALUES =
		ConstantTagType.values();
	
	/** The type of tag to use. */
	protected final Class<?> type;
	
	/**
	 * Initializes the constant tag type.
	 *
	 * @param __t The class type to use.
	 * @since 2016/08/18
	 */
	private ConstantTagType(Class<?> __t)
	{
		this.type = __t;
	}
	
	/**
	 * This returns the tag type used for a given class.
	 *
	 * @param __t The type of class to check.
	 * @throws JITException If no matching type was found.
	 * @throws NullPointerException On null arguments.
	 * @return The constant tag type representation.
	 */
	public static ConstantTagType ofClass(Class<?> __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Go through all of them
		for (ConstantTagType c : _VALUES)
			if (c.type == __t)
				return c;
		
		// {@squirreljme.error AQ01 Unknown class type. (The class type)}
		throw new JITException(String.format("AQ01 %s", __t));
	}
}

