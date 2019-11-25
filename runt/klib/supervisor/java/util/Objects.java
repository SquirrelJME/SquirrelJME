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

