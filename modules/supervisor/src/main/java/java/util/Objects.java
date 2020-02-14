// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * Object utilties.
 *
 * @since 2019/11/25
 */
public class Objects
{
	/**
	 * Checks whether two objects are equal to each other, if one of the
	 * values is {@code null} then this returns {@code false}, otherwise
	 * equality is checked. If both values are {@code null} then {@code true}
	 * is returned.
	 *
	 * @param __a The object which gets {@link Object#equals(Object)} called.
	 * @param __b The parameter to that call.
	 * @return {@code true} if they are both equal or both {@code null}.
	 * @since 2016/04/12
	 */
	public static boolean equals(Object __a, Object __b)
	{
		// Which sides are null
		boolean na = (__a == null);
		boolean nb = (__b == null);
		
		// One side is null, but the other is not
		if (na != nb)
			return false;
		
		// Both sides are null (just need to check one)
		else if (na)
			return true;
		
		// Standard equals
		return __a.equals(__b);
	}
	
	/**
	 * Returns the hash code of the given object or {@code 0} if it is
	 * {@code null}.
	 *
	 * @param __a The object to get the hashcode of.
	 * @return The object's hash code or {@code 0} if it is {@code null}.
	 * @since 2016/04/12
	 */
	public static int hashCode(Object __a)
	{
		if (__a == null)
			return 0;
		return __a.hashCode();
	}
	
	/**
	 * Converts the specified object to a string, if the input value is
	 * {@code null} then {@code "null"} is returned.
	 *
	 * @param __a The value to get the string of.
	 * @return The string of the given value or {@code "null"} if the input is
	 * {@code null}.
	 * @since 2016/04/12
	 */
	public static String toString(Object __a)
	{
		if (__a == null)
			return "null";
		return __a.toString();
	}
	
	/**
	 * Converts the specified object to a string, if the input value is
	 * {@code null} then {@code __b} is returned.
	 *
	 * @param __a The object to get the string of.
	 * @param __b The value to return if {@code __a} is {@code null}.
	 * @return The string represention of {@code __a} or else {@code __b} if
	 * the input is {@code null}.
	 * @since 2016/04/12
	 */
	public static String toString(Object __a, String __b)
	{
		if (__a == null)
			return __b;
		return __a.toString();
	}
}

