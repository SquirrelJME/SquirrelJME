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
 * Tests division by zero.
 *
 * @since 2018/12/04
 */
public class TestDivideByZero
	extends TestSupplier<Integer>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/04
	 */
	@Override
	public Integer test()
	{
		return 42 / 0;
	}
}

