// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import net.multiphasicapps.squirreljme.classformat.StackMapType;

/**
 * This represents the type of data which could be stored at a given memory
 * address, these map to primitive types.
 *
 * @since 2017/02/08
 */
public enum DataType
{
	/** Byte. */
	BYTE(false, 1),
	
	/** Short. */
	SHORT(false, 2),
	
	/** Integer. */
	INTEGER(false, 4),
	
	/** Long. */
	LONG(false, 8),
	
	/** Float. */
	FLOAT(true, 4),
	
	/** Double. */
	DOUBLE(true, 8),
	
	/** End. */
	;
	
	/** Is this a floating point type? */
	protected final boolean isfloat;
	
	/** The number of bytes stored in the data type. */
	protected final int bytes;
	
	/**
	 * Initializes the data type.
	 *
	 * @param __float {@code true} if a floating point type.
	 * @param __nb The number of bytes that fit in the data type.
	 * @since 2017/02/08
	 */
	private DataType(boolean __float, int __nb)
	{
		// Set
		this.isfloat = __float;
		this.bytes = __nb;
	}
	
	/**
	 * Maps the specified stack type to the data type.
	 *
	 * @param __t The stack type to map.
	 * @return The data type for the given stack type.
	 * @throws JITException If the type could not be mapped.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/20
	 */
	public static DataType of(StackMapType __t)
		throws JITException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__t)
		{
				// Get type
			case INTEGER:	return DataType.INTEGER;
			case LONG:		return DataType.LONG;
			case FLOAT:		return DataType.FLOAT;
			case DOUBLE:	return DataType.DOUBLE;
			
				// {@squirreljme.error ED09 The specified stack type cannot
				// be mapped to a data type. (The input stack type)}
			default:
				throw new JITException(String.format("ED09 %s", __t));
		}
	}
}

