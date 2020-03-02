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
 * Tests integer decoding.
 *
 * @since 2018/11/11
 */
public class TestIntegerDecode
	extends TestSupplier<Integer>
{
	/** Values to test. */
	private static final String[] _VALUES =
		new String[]
		{
			"0x1234",
			"0X1234",
			"#1234",
			"01234",
			"1234",
			"-0x1234",
			"-0X1234",
			"-#1234",
			"-01234",
			"-1234",
			"+0x1234",
			"+0X1234",
			"+#1234",
			"+01234",
			"+1234",
		};
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public Integer test()
	{
		int rv = 0;
		for (String s : _VALUES)
		{
			int v = Integer.decode(s);
			rv += v;
			this.secondary(s, v);
		}
		
		return rv;
	}
}

