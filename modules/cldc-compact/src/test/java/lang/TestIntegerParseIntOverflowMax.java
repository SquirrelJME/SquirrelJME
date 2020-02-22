// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestSupplier;

/**
 * This tests that {@link Integer#parseInt(int, int)} when the input value
 * overflows it throws an exception, but where each character is the max for
 * the given radix.
 *
 * @since 2018/10/13
 */
public class TestIntegerParseIntOverflowMax
	extends TestSupplier<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/13
	 */
	@Override
	public String test()
	{
		StringBuilder sb = new StringBuilder();
		
		// Check overflow for all radixes
		int rv = 0;
		for (int r = Character.MIN_RADIX; r <= Character.MAX_RADIX; r++)
			try
			{
				// This is a really long value which is valid for all radixes
				// it is either a 33-bit integer or log(33)??
				char c = Character.forDigit(r - 1, r);
				StringBuilder seq = new StringBuilder("-");
				for (int i = 0; i < 33; i++)
					seq.append(c);
				
				// Decode that max value
				Integer.parseInt(seq.toString(), r);
				
				sb.append(Character.toUpperCase(
					Character.forDigit(r, Character.MAX_RADIX)));
			}
			catch (NumberFormatException e)
			{
				sb.append(Character.forDigit(r, Character.MAX_RADIX));
			}
		
		return sb.toString();
	}
}

