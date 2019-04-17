// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

/**
 * This represents the type of invocation that is performed.
 *
 * @since 2019/03/20
 */
public enum InvokeType
{
	/** Static invoke. */
	STATIC,
	
	/** Special invoke. */
	SPECIAL,
	
	/** Virtual. */
	VIRTUAL,
	
	/** Interface. */
	INTERFACE,
	
	/** End. */
	;
	
	/**
	 * Does this use an instance variable?
	 *
	 * @return If there is an instance variable that is used.
	 * @since 2019/03/20
	 */
	public final boolean hasInstance()
	{
		return this != STATIC;
	}
	
	/**
	 * Returns the invocation type for the given ordinal.
	 *
	 * @param __i The ordinal.
	 * @return The invocation type.
	 * @throws IllegalArgumentException If it is not valid.
	 * @since 2019/04/17
	 */
	public static final InvokeType of(int __i)
		throws IllegalArgumentException
	{
		switch (__i)
		{
			case 0:	return STATIC;
			case 1: return SPECIAL;
			case 2: return VIRTUAL;
			case 3: return INTERFACE;
		}
		
		// {@squirreljme.error JC3w Unknown invocation type. (The index)}
		throw new IllegalArgumentException("JC3w " + __i);
	}
}

