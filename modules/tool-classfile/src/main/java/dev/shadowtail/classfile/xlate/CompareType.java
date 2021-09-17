// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.xlate;

/**
 * This represents the type of comparison to perform.
 *
 * @since 2019/03/26
 */
public enum CompareType
{
	/** Equals. */
	EQUALS,
	
	/** Not equals. */
	NOT_EQUALS,
	
	/** Less than. */
	LESS_THAN,
	
	/** Less or equals. */
	LESS_THAN_OR_EQUALS,
	
	/** Greater than. */
	GREATER_THAN,
	
	/** Greater or equals. */
	GREATER_THAN_OR_EQUALS,
	
	/** Always true. */
	TRUE,
	
	/** Always false. */
	FALSE,
	
	/* End. */
	;
	
	/**
	 * Returns the compare type for the given index.
	 *
	 * @param __i The index.
	 * @return The resulting compare type.
	 * @since 2019/04/08
	 */
	public static CompareType of(int __i)
	{
		switch (__i)
		{
			case 0:		return CompareType.EQUALS;
			case 1:		return CompareType.NOT_EQUALS;
			case 2:		return CompareType.LESS_THAN;
			case 3:		return CompareType.LESS_THAN_OR_EQUALS;
			case 4:		return CompareType.GREATER_THAN;
			case 5:		return CompareType.GREATER_THAN_OR_EQUALS;
			case 6:		return CompareType.TRUE;
			case 7:		return CompareType.FALSE;
		}
		
		// {@squirreljme.error JC1b Invalid compare operation.}
		throw new IllegalArgumentException("JC1b");
	}
}

