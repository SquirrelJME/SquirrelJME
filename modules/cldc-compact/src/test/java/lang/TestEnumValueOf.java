// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import lang.extra.ABasicEnum;
import lang.extra.AClassEnum;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests enumerations.
 *
 * @since 2018/12/08
 */
public class TestEnumValueOf
	extends TestSupplier<String>
{
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public String test()
	{
		return ABasicEnum.A.name() +
			Enum.<ABasicEnum>valueOf(ABasicEnum.class, "B") +
			AClassEnum.B.boop() +
			Enum.<AClassEnum>valueOf(AClassEnum.class, "C").boop();
	}
}

