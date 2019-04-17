// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.nncc;

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
	
	/** End. */
	;
	
	/**
	 * Returns the access type for the given ordinal.
	 *
	 * @param __i The ordinal.
	 * @return The access type.
	 * @throws IllegalArgumentException If it is not valid.
	 * @since 2019/04/17
	 */
	public static final FieldAccessType of(int __i)
		throws IllegalArgumentException
	{
		switch (__i)
		{
			case 0:	return STATIC;
			case 1: return INSTANCE;
		}
		
		// {@squirreljme.error JC3v Unknown access type. (The index)}
		throw new IllegalArgumentException("JC3v " + __i);
	}
}

