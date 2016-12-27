// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * This class contains conversion from integral types to floating point types
 * implemented completely in software.
 *
 * @since 2016/08/29
 */
public final class SoftInteger
{
	/**
	 * Not used.
	 *
	 * @since 2016/08/29
	 */
	private SoftInteger()
	{
	}
	
	/**
	 * Converts the value to a double.
	 *
	 * @param __i The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long integerToDouble(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to a float.
	 *
	 * @param __i The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int integerToFloat(int __i)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to a double.
	 *
	 * @param __l The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static long longToDouble(long __l)
	{
		throw new Error("TODO");
	}
	
	/**
	 * Converts the value to a float.
	 *
	 * @param __l The value to convert.
	 * @return The converted value.
	 * @since 2016/08/29
	 */
	public static int longToFloat(long __l)
	{
		throw new Error("TODO");
	}
}

