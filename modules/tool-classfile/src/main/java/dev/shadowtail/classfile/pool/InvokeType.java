// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.pool;

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
	
	/** System invocation type. */
	SYSTEM,
	
	/* End. */
	;
	
	/**
	 * Does this use an instance variable?
	 *
	 * @return If there is an instance variable that is used.
	 * @since 2019/03/20
	 */
	public final boolean hasInstance()
	{
		return !this.isStatic();
	}
	
	/**
	 * Is this a static invocation?
	 *
	 * @return If this is a static invocation.
	 * @since 2019/04/22
	 */
	public final boolean isStatic()
	{
		return this == InvokeType.STATIC || this == InvokeType.SYSTEM;
	}
	
	/**
	 * Returns the invocation type for the given ordinal.
	 *
	 * @param __i The ordinal.
	 * @return The invocation type.
	 * @throws IllegalArgumentException If it is not valid.
	 * @since 2019/04/17
	 */
	public static InvokeType of(int __i)
		throws IllegalArgumentException
	{
		switch (__i)
		{
			case 0:	return InvokeType.STATIC;
			case 1: return InvokeType.SPECIAL;
			case 2: return InvokeType.VIRTUAL;
			case 3: return InvokeType.INTERFACE;
			case 4: return InvokeType.SYSTEM;
		}
		
		// {@squirreljme.error JC16 Unknown invocation type. (The index)}
		throw new IllegalArgumentException("JC16 " + __i);
	}
}

