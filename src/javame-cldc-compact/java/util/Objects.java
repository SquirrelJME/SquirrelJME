// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package java.util;

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
		throw new RuntimeException("WTFX");
	}
	
	public static <T> int compare(T __a, T __b, Comparator<? super T> __c)
	{
		throw new Error("TODO");
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
		
		// Both sides are null?
		if (na && nb)
			return true;
		
		// One side is null, but the other is not
		if (na != nb)
			return false;
		
		// Standard equals
		return __a.equals(__b);
	}
	
	public static int hashCode(Object __a)
	{
		throw new Error("TODO");
	}
	
	public static <T> T requireNonNull(T __a)
	{
		throw new Error("TODO");
	}
	
	public static <T> T requireNonNull(T __a, String __b)
	{
		throw new Error("TODO");
	}
	
	public static String toString(Object __a)
	{
		throw new Error("TODO");
	}
	
	public static String toString(Object __a, String __b)
	{
		throw new Error("TODO");
	}
}

