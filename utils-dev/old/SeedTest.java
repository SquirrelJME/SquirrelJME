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
 * This tests the PRNG used for Object hashcodes
 *
 * @since 2017/12/10
 */
public class SeedTest
{
	public static void main(String... __args)
	{
		int seed = 0x917F_25C9;
		for (int i = 0, n = Math.max(1, Integer.parseInt(
			(__args != null && __args.length >= 1 ? __args[0] : "10"))); i < n;
			i++)
		{
			seed = (seed >>> 1) ^ (-(seed & 1) & 0x80200003);
			System.out.printf("0x%08x%n", seed);
		}
	}
}

