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
 * Tests allocation of a multi-dimensional array.
 *
 * @since 2018/11/03
 */
public class TestMultiANewArray
	extends TestSupplier<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/03
	 */
	@Override
	public String test()
	{
		return new Integer[7][9][12][42][1][3].getClass().getName();
	}
}

