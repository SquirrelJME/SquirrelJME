// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import net.multiphasicapps.classfile.PrimitiveType;

/**
 * Represents the data type to use for read/write.
 *
 * @since 2019/03/24
 */
public enum DataType
{
	/** Pointer. */
	POINTER,
	
	/** Byte. */
	BYTE,
	
	/** Short. */
	SHORT,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Float. */
	FLOAT,
	
	/** Double. */
	DOUBLE,
	
	/** End. */
	;
	
	/**
	 * Returns the data type used for the primitive type.
	 *
	 * @param __t The type to use, {@code null} is treated as a pointer.
	 * @return The data type used.
	 * @since 2019/03/24
	 */
	public static final DataType of(PrimitiveType __t)
	{
		if (__t == null)
			return POINTER;
		
		switch (__t)
		{
			case BYTE:		return BYTE;
			case CHARACTER:
			case SHORT:		return SHORT;
			case INTEGER:	return INTEGER;
			case LONG:		return LONG;
			case FLOAT:		return FLOAT;
			case DOUBLE:	return DOUBLE;
			
			default:
				throw new todo.OOPS(__t.toString());
		}
	}
}

