// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	@SuppressWarnings({"NumericOverflow", "divzero"})
	@Override
	public Integer test()
	{
		return 42 / 0;
	}
}

