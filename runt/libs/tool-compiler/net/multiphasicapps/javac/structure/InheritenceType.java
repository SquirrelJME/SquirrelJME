// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.structure;

/**
 * This represents the inheritence type of some class structures.
 *
 * @since 2018/04/24
 */
public enum InheritenceType
{
	/** No inheritence. */
	NONE,
	
	/** Single inheritence. */
	SINGLE,
	
	/** Multiple inheritence. */
	MULTIPLE,
	
	/** End. */
	;
	
	/**
	 * Is the inheritence count valid for the given type?
	 *
	 * @param __n The number of inherited elements.
	 * @return If it is valid or not.
	 * @since 2018/04/30
	 */
	public final boolean isCompatibleCount(int __n)
	{
		switch (this)
		{
			case NONE:
				return __n == 0;
				
			case SINGLE:
				return (__n == 0 || __n == 1);
				
			case MULTIPLE:
				return __n >= 0;
			
			default:
				throw new RuntimeException("OOPS");
		}
	}
}

