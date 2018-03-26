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
 * This is for my sanity to make sure I got this correct.
 *
 * @since 2018/03/26
 */
public class UByteMaxFlip
{
	public static void main(String... __args)
	{
		for (int i = 0; i < 256; i++)
		{
			System.out.printf("%02x %02x%n",
				255 - i, 0xFF ^ i);
		}
	}
}

