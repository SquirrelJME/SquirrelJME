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
			
			val =
				// Base minimum size of all allocated objects
				8 +
				
				// Index counter multiplied to a multiple of four
				(i * 4) + 
				
				// Steady rise on the small scale
				1024 * (Math.max(0, i - 512)) +
				
				// Big rise on the large scale
				8192 * (Math.max(0, i - 1024)) +
				
				// Very big rise on the large scale
				65536 * (Math.max(0, i - 1536))
				
				/*Math.min(134217727, 1 << ((i >>> 8) - 1)) +*/
				/*Math.min(268435455, 1 << ((i >>> 7) - 1)) +*/
				
				// This one alone seems to work well
				/*Math.max(0, Math.min(536870911, 1 << ((i >>> 6) - 1)))*/
				
				;
			
			// Base size, all objects and allocations are at least this size
			val = 8;
			
			// Very small objects, the usual
			if (i < 1024)
				val += i * 4;
			
			// Smaller gains
			else if (i < 1536)
				val += 4096 + ((i - 1023) * 1024);
			
			// Larger gains
			else if (i < 1792)
				val += 528392 + ((i - 1535) * 4096);
			
			// Larger gains
			else if (i < 2000)
				val += 1576976 + ((i - 1791) * 214795);
			
			// The last amounts maximize usage
			else if (i < 2048)
				val += 2428944 + ((i - 1999) * 44040192);
			
			long x = (long)val;
			
			long clipped = Math.max(0, Math.min(x, Integer.MAX_VALUE));
			
			if (true)
				System.out.println(clipped);
			else
				System.out.printf(
					"%4d: %10d (%7dKiB, %4dMiB, %9d int[], %9d long[])%n",
					i,
					clipped,
					clipped / 1024,
					clipped / 1048576,
					(clipped - 12) / 4,
					(clipped - 12) / 8);
		}
	}
}

