// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.runtime.cldc.annotation.ImplementationNote;

/**
 * This class provides utility methods which are usually able to handle null
 * values.
 *
 * @since 2016/04/12
 */
public final class Objects
{
	/**
	 * Should not be called at all.
	 *
	 * @since 2016/04/12
	 */
	private Objects()
	{
		throw new todo.OOPS();
	}
	
	/**
	 * This compares two values and possibly may compare it with the given
	 * {@link Comparator}. First, if the input objects are identical (in that
	 * {@code __a == __b} is performed) then {@code 0} is returned. Otherwise,
	 * the values are passed to the specified {@link Comparator}.
	 *
	 * A {@link NullPointerException} may be thrown by the comparator.
	 *
	 * @param <T> The type of value to compare.
	 * @param __a The first value.
	 * @param __b The second value.
	 * @param __c The comparator to use.
	 * @return {@code 0} if {@code __a == __b}, otherwise the value returned
	 * from the {@link Comparator}.
	 * @throws NullPointerException If the objects are not the same object
	 * reference and there is no {@link Comparator}. May also be thrown if the
	 * comparator is unable to handle {@code null} arguments.
	 * @since 2016/04/12
	 */
	public static <T> int compare(T __a, T __b, Comparator<? super T> __c)
		throws NullPointerException
	{
		// The same object?
		if (__a == __b)
			return 0;
		
		// Compare otherwise
		if (__c == null)
			throw new NullPointerException("NARG");
		return __c.compare(__a, __b);
	}
	
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
	 * Checks whether the given input value is not {@code null} and then
	 * returns it, otherwise this throws {@link NullPointerException}.
	 *
	 * @param <T> The type of value to return.
	 * @param __a The value to return.
	 * @return {@code __a}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	public static <T> T requireNonNull(T __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		return __a;
	}
	
	/**
	 * Checks whether the given input value is not {@code null} and then
	 * returns it, otherwise this throws a {@link NullPointerException} with
	 * the given message.
	 *
	 * @param <T> The type of value to return.
	 * @param __a The value to return.
	 * @return {@code __a}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/12
	 */
	public static <T> T requireNonNull(T __a, String __b)
	{
		// Check
		if (__a == null)
			throw new NullPointerException((__b != null ? __b : "NARG"));
		return __a;
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

