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
 * Binary ranging for deflate huffman fun.
 *
 * @since 2016/03/11
 */
public class RangeBinary
{
	public static void main(String... __args)
	{
		// Read values
		int a = Integer.parseInt(__args[0], 2);
		int b = Integer.parseInt(__args[1], 2);
		
		// Get min and max
		int min = Math.min(a, b);
		int max = Math.max(a, b);
		
		// Go through all values
		for (int i = min; i <= max; i++)
		{
			// True value to calculate
			int actual = i & 0b1111111;
			
			System.err.println(String.format("%7s", Integer.toString(actual, 2)));
		}
	}
}

