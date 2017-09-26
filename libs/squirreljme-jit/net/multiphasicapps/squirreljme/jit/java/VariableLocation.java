// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

/**
 * This represents the location where a variable is stored.
 *
 * @since 2017/08/13
 */
@Deprecated
public enum VariableLocation
{
	/** Local variables. */
	LOCAL,
	
	/** Stack variables. */
	STACK,
	
	/** If the method is synchronized this is the monitor to keep track of. */
	SYNCHRONIZED,
	
	/** The exception that is in the throwing state. */
	THROWING_EXCEPTION,
	
	/** The return value from a method. */
	RETURN_VALUE,
	
	/** End. */
	;
	
	/**
	 * Is this considered a stack?
	 *
	 * @return If this is considered a stack.
	 * @since 2017/09/22
	 */
	public final boolean isStack()
	{
		return this == STACK;
	}
	
	/**
	 * Returns the size of this location.
	 *
	 * @param __ms The maximum number of stack entries.
	 * @param __ml The maximum number of local entries.
	 * @return The size of this location.
	 * @since 2017/09/22
	 */
	public final int size(int __ms, int __ml)
	{
		if (this == STACK)
			return __ms;
		if (this == LOCAL)
			return __ml;
		return 1;
	}
}

