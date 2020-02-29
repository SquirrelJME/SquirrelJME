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
 * This represents when a field is being accessed.
 *
 * @since 2019/03/24
 */
public enum FieldAccessTime
{
	/** Written by constructor or static initializer. */
	INITIALIZER,
	
	/** Written by non-constructor. */
	NORMAL,
	
	/** Read access. */
	READ,
	
	/** End. */
	;
	
	/**
	 * Returns the access time for the given ordinal.
	 *
	 * @param __i The ordinal.
	 * @return The access time.
	 * @throws IllegalArgumentException If it is not valid.
	 * @since 2019/04/17
	 */
	public static final FieldAccessTime of(int __i)
		throws IllegalArgumentException
	{
		switch (__i)
		{
			case 0:	return FieldAccessTime.INITIALIZER;
			case 1: return FieldAccessTime.NORMAL;
			case 2: return FieldAccessTime.READ;
		}
		
		// {@squirreljme.error JC14 Unknown access time. (The index)}
		throw new IllegalArgumentException("JC14 " + __i);
	}
}

