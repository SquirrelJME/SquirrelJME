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
 * Tests that reading the array causes an out of bounds to occur.
 *
 * @since 2018/12/04
 */
public class TestVMIndexOutOfBounds
	extends TestSupplier<String>
{
	/** Strings to test. */
	private static final String[] _STRINGS =
		{
			"Squirrels are cute!",
			"They really are!",
			"So are red pandas!",
			"I love you so much!",
		};
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public String test()
	{
		return _STRINGS[5];
	}
}

