// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * Tries to fast divide by three.
 *
 * @since 2018/03/24
 */
public class FastDivideByThree
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments.
	 * @since 2018/03/24
	 */
	public static void main(String... __args)
	{
		int n = Integer.parseInt(__args[0]);
		
		System.out.printf("normal: %d%n", n / 3);
		System.out.printf("341s10: %d%n", (n * 341) >> 10);
	}
}

