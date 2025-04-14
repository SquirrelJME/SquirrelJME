// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * Generates 4-bit alpha table.
 *
 * @since 2024/07/13
 */
public class AlphaTable
{
	/**
	 * Main entry point.
	 *
	 * @param __args Ignored.
	 * @since 2024/07/13
	 */
	public static void main(String... __args)
	{
		for (int ah = 0x00; ah < 0x100; ah += 0x10)
		{
			// Full value for A
			int dX = ah | (ah >>> 4);
			
			for (int bh = 0x00; bh < 0x100; bh += 0x10)
			{
				// Full value for B
				int sX = bh | (bh >>> 4);
				
				// Convert alpha to float
				double dD = (dX / 255.0);
				double sD = (sX / 255.0);
				
				double mD = sD + dD - (sD * dD);
				
				System.out.printf("");
				
				int iD = Math.max(0, Math.min(255,
					(int)Math.round(mD * 255.0)));
				
				System.out.printf("%02");
				
				/*int da = ca = sa + da - sjme_fixed_mul(sa, da);*/
			}
		}
	}
}
