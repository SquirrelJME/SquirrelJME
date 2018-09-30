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
 * How many size entries can you fit in 11-bits.
 *
 * @since 2018/09/30
 */
public class SizeMap
{
	public static void main(String... __args)
	{
		for (int i = 0; i < 2048; i++)
		{
			double ii = (double)i;
			double val;
			
			//val = Math.pow(2, Math.pow(2, Math.log(ii * 44.0)));
			//val = Math.pow(2, ii);
			
			val = Math.min(536870911, 1 << ((i >>> 6) - 1));
			
			long x = (long)val;
			System.out.printf("%4d: %10d%n", i,
				Math.max(0, Math.min(x, Integer.MAX_VALUE)));
		}
	}
}

