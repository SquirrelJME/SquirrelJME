// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This enumeration represents a primitive type.
 *
 * @since 2017/10/16
 */
public enum PrimitiveType
{
	/** Byte. */
	BYTE,
	
	/** Character. */
	CHARACTER,
	
	/** Double. */
	DOUBLE,
	
	/** Float. */
	FLOAT,
	
	/** Integer. */
	INTEGER,
	
	/** Long. */
	LONG,
	
	/** Short. */
	SHORT,
	
	/** Boolean. */
	BOOLEAN,
	
	/** End. */
	;
	
	/**
	 * Is this a wide type?
	 *
	 * @return If this is a wide type.
	 * @since 2017/10/16
	 */
	public final boolean isWide()
	{
		return this == LONG || this == DOUBLE;
	}
	
	/**
	 * Does this type promote to int?
	 *
	 * @return If this type promotes to int.
	 * @since 2017/10/16
	 */
	public final boolean promotesToInt()
	{
		switch (this)
		{
			case BYTE:
			case CHARACTER:
			case SHORT:
			case BOOLEAN:
				return true;	
			
			default:
				return false;
		}
	}
}

