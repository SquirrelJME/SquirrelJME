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
 * Math functions.
 *
 * @since 2019/11/30
 */
public class Math
{
	/**
	 * Returns the higher of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The higher value.
	 * @since 2018/09/29
	 */
	public static int max(int __a, int __b)
	{
		if (__a > __b)
			return __a;
		return __b;
	}
	
	/**
	 * Returns the lower of the two values.
	 *
	 * @param __a The first.
	 * @param __b The second.
	 * @return The lower value.
	 * @since 2018/09/29
	 */
	public static int min(int __a, int __b)
	{
		if (__a < __b)
			return __a;
		return __b;
	}
}

