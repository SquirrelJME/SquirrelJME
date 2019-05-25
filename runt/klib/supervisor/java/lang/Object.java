// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This class defines the base class for every class which exists.
 *
 * @since 2019/05/25
 */
public class Object
{
	/**
	 * Returns the hash code for this object.
	 *
	 * @return The object hash code.
	 * @since 2019/05/25
	 */
	public final int hashCode()
	{
		return 1234;
	}
	
	/**
	 * Checks if this object is equal to another object.
	 *
	 * @param __o The object to check.
	 * @return If the objects are equal.
	 * @since 2019/05/25
	 */
	public final boolean equals(Object __o)
	{
		return this == __o;
	}
	
	/**
	 * Returns the string representation of this object.
	 *
	 * @return The string representation.
	 * @since 2019/05/25
	 */
	public final String toString()
	{
		return "AnObject";
	}
}
