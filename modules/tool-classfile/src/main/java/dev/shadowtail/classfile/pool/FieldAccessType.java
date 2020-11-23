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
 * Represents how a field is being accessed.
 *
 * @since 2019/03/24
 */
public enum FieldAccessType
{
	/** Static. */
	STATIC,
	
	/** Instance. */
	INSTANCE,
	
	/* End. */
	;
	
	/**
	 * Is this field access static?
	 *
	 * @return If this is a static field access.
	 * @since 2019/04/22
	 */
	public final boolean isStatic()
	{
		return this == FieldAccessType.STATIC;
	}
	
	/**
	 * Returns the access type for the given ordinal.
	 *
	 * @param __i The ordinal.
	 * @return The access type.
	 * @throws IllegalArgumentException If it is not valid.
	 * @since 2019/04/17
	 */
	public static FieldAccessType of(int __i)
		throws IllegalArgumentException
	{
		switch (__i)
		{
			case 0:	return FieldAccessType.STATIC;
			case 1: return FieldAccessType.INSTANCE;
		}
		
		// {@squirreljme.error JC15 Unknown access type. (The index)}
		throw new IllegalArgumentException("JC15 " + __i);
	}
}

